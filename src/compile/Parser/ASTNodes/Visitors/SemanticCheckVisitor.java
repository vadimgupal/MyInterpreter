package compile.Parser.ASTNodes.Visitors;

import compile.Parser.ASTNodes.Nodes.*;
import compile.SemanticException;
import compile.Types.*;

public class SemanticCheckVisitor extends AutoVisitor {
    @Override
    public void VisitAssign(AssignNode ass) throws SemanticException {
        // Сначала вычисляем тип выражения справа
        SemanticType exprType = CalcTypes.CalcType(ass.Expr);
        if (exprType == SemanticType.BadType) {
            throw new SemanticException("Несоответствие типов", ass.Ident.pos);
        }

        // Если переменная не существует - создаем новую запись
        if (!SymbolTable.SymTable.containsKey(ass.Ident.Name)) {
            SymbolInfo info = new SymbolInfo(
                    ass.Ident.Name,
                    KindType.VarName,
                    exprType
            );

            // Для массивов дополнительно сохраняем тип элементов
            if (ass.Expr instanceof ArrayLiteral arrayLiteral &&
                    !arrayLiteral.value.lst.isEmpty()) {
                info.ElementTyp = CalcTypes.CalcType(arrayLiteral.value.lst.getFirst());
            }

            SymbolTable.SymTable.put(ass.Ident.Name, info);
        }
        // Если переменная существует - проверяем типы
        else {
            SymbolInfo existingInfo = SymbolTable.SymTable.get(ass.Ident.Name);

            // Проверяем что это переменная
            if (existingInfo.Kind != KindType.VarName) {
                throw new SemanticException(
                        ass.Ident.Name + " не является именем переменной",
                        ass.Ident.pos
                );
            }

            // Основная проверка совместимости типов
            if (!CalcTypes.AssignComparable(existingInfo.Typ, exprType)) {
                throw new SemanticException(
                        "Переменной " + ass.Ident.Name + " типа " + existingInfo.Typ +
                                " нельзя присвоить выражение типа " + exprType,
                        ass.Ident.pos
                );
            }

            // Для массивов дополнительно проверяем тип элементов
            if (exprType == SemanticType.ArrayType &&
                    ass.Expr instanceof ArrayLiteral arrayLiteral) {

                SemanticType newElementType = !arrayLiteral.value.lst.isEmpty()
                        ? CalcTypes.CalcType(arrayLiteral.value.lst.getFirst())
                        : SemanticType.ObjectType;

                if (existingInfo.ElementTyp != null &&
                        !CalcTypes.AssignComparable(existingInfo.ElementTyp, newElementType)) {
                    throw new SemanticException(
                            "Несовместимость типов элементов массива. Ожидается " +
                                    existingInfo.ElementTyp + ", получен " + newElementType,
                            ass.Ident.pos
                    );
                }
            }
        }

        // Рекурсивно проверяем выражение
        ass.Expr.VisitP(this);
    }

    @Override
    public void VisitAssignPlus(AssignPlusNode ass) throws SemanticException {
        if (!SymbolTable.SymTable.containsKey(ass.Ident.Name)) {
            throw new SemanticException("Переменная " + ass.Ident.Name + " не определена ", ass.Ident.pos);
        } else {
            if(SymbolTable.SymTable.get(ass.Ident.Name).Kind!=KindType.VarName)
                throw new SemanticException(ass.Ident.Name+" не является именем переменной",ass.Ident.pos);
            SemanticType typ = CalcTypes.CalcType(ass.Expr);
            SemanticType idtyp = SymbolTable.SymTable.get(ass.Ident.Name).Typ;
            if(!SymbolTable.NumTypes.contains(idtyp))
                throw new SemanticException("Операция += не определена для типа "+idtyp,ass.Ident.pos);
            if(!CalcTypes.AssignComparable(idtyp,typ))
                throw new SemanticException("Переменной "+ass.Ident.Name+" типа "+idtyp+" нельзя += выражение типа "+typ,ass.Ident.pos);

        }
    }

    @Override
    public void VisitIf(IfNode ifn) throws SemanticException {
        ifn.Condition.VisitP(this);
        SemanticType typ = CalcTypes.CalcType(ifn.Condition);
        if(typ!=SemanticType.BoolType)
            throw new SemanticException("Ожидалось выражение логического типа, а встречено выражение типа "+typ,ifn.Condition.pos);
        ifn.ThenStat.VisitP(this);
        if(ifn.ElseStat != null) {
            ifn.ElseStat.VisitP(this);
        }
    }

    @Override
    public void VisitWhile(WhileNode whn) throws SemanticException {
        whn.Condition.VisitP(this);
        SemanticType typ = CalcTypes.CalcType(whn.Condition);
        if(typ!=SemanticType.BoolType)
            throw new SemanticException("Ожидалось выражение логического типа, а встречено выражение типа "+typ,whn.Condition.pos);
        whn.Stat.VisitP(this);
    }

    @Override
    public void VisitProcCall(ProcCallNode p) throws SemanticException {
        if(!SymbolTable.SymTable.containsKey(p.Name.Name))
            throw new SemanticException("Функция с именем "+p.Name.Name+" не определена ",p.Name.pos);
        SymbolInfo sym = SymbolTable.SymTable.get(p.Name.Name);
        if(sym.Kind!=KindType.FuncName)
            throw new SemanticException("Данное имя "+p.Name.Name+" не является именем функции",p.Name.pos);
        if (sym.Params.size()!=p.Pars.lst.size())
            throw new SemanticException("Несоответствие количества параметров при вызове процедуры "+p.Name.Name,p.Name.pos);
        for(int i=0;i<sym.Params.size();++i){
            SemanticType tp=CalcTypes.CalcType(p.Pars.lst.get(i));
            if(tp==SemanticType.BadType) {
                throw new SemanticException(" не соответствие типов",p.Pars.lst.get(i).pos);
            }
            if(!CalcTypes.AssignComparable(sym.Params.get(i),tp))
                throw new SemanticException("Тип аргумента процедуры "+tp.toString()+" не соответствует типу формального параметра "+sym.Params.get(i).toString(),p.Pars.lst.get(i).pos);
        }
        if(sym.Typ!=SemanticType.NoType)
            throw new SemanticException("попытка вызвать функцию "+p.Name.Name+" как процедуру ",p.Name.pos);
    }

    @Override
    public void VisitArrayIndex(ArrayIndexNode arrIndex) throws SemanticException {
        // Проверяем, что переменная существует
        if (!SymbolTable.SymTable.containsKey(arrIndex.arrayName.Name)) {
            throw new SemanticException("Массив " + arrIndex.arrayName.Name + " не определен", arrIndex.arrayName.pos);
        }

        // Проверяем, что это действительно массив
        SemanticType arrayType = SymbolTable.SymTable.get(arrIndex.arrayName.Name).Typ;
        if (arrayType != SemanticType.ArrayType) {
            throw new SemanticException(arrIndex.arrayName.Name + " не является массивом", arrIndex.arrayName.pos);
        }

        // Проверяем тип индекса
        SemanticType indexType = CalcTypes.CalcType(arrIndex.index);
        if (indexType != SemanticType.IntType) {
            throw new SemanticException("Индекс массива должен быть целым числом", arrIndex.index.pos);
        }

        // Рекурсивно проверяем индексное выражение
        arrIndex.index.VisitP(this);
    }
}
