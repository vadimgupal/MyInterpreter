package compile.Parser.ASTNodes.Visitors;

import compile.Parser.ASTNodes.Nodes.*;
import compile.SemanticException;
import compile.Types.*;

public class SemanticCheckVisitor extends AutoVisitor {
    @Override
    public void VisitAssign(AssignNode ass) throws SemanticException {
        SemanticType exprType = CalcTypes.CalcType(ass.Expr);
        if (exprType == SemanticType.BadType) {
            throw new SemanticException(
                    "Несоответствие типов при присваивании",
                    ass.LValue.getPos()
            );
        }

        // 2) Обработка двух видов L-value: IdNode и ArrayIndexNode
        if (ass.LValue instanceof IdNode id) {
            String name = id.Name;

            // 2.1) Объявление новой переменной
            if (!SymbolTable.SymTable.containsKey(name)) {
                SymbolInfo info = new SymbolInfo(name, KindType.VarName, exprType);
                // Если сразу присваивается ArrayLiteral — фиксируем элементный тип
                if (exprType.isArray() && ass.Expr instanceof ArrayLiteral al) {
                    info.ElementTyp = al.value.lst.isEmpty()
                            ? SemanticType.ObjectType
                            : CalcTypes.CalcType(al.value.lst.getFirst());
                }
                SymbolTable.SymTable.put(name, info);
            }
            // 2.2) Существующая переменная
            else {
                SymbolInfo existing = SymbolTable.SymTable.get(name);

                if (existing.Kind != KindType.VarName) {
                    throw new SemanticException(
                            name + " не является именем переменной",
                            id.pos
                    );
                }
                if (!CalcTypes.AssignComparable(existing.Typ, exprType)) {
                    throw new SemanticException(
                            "Переменной " + name + " типа " + existing.Typ +
                                    " нельзя присвоить выражение типа " + exprType,
                            id.pos
                    );
                }
                // Для массивов проверяем элементный тип
                if (existing.Typ.isArray()
                        && exprType.isArray()
                        && ass.Expr instanceof ArrayLiteral al2) {

                    SemanticType newElem = al2.value.lst.isEmpty()
                            ? SemanticType.ObjectType
                            : CalcTypes.CalcType(al2.value.lst.getFirst());

                    if (existing.ElementTyp != null
                            && !CalcTypes.AssignComparable(existing.ElementTyp, newElem)) {
                        throw new SemanticException(
                                "Несовместимость типов элементов массива. Ожидался " +
                                        existing.ElementTyp + ", получен " + newElem,
                                id.pos
                        );
                    }
                }
            }
        }
        else if (ass.LValue instanceof ArrayIndexNode ai) {
            // 2.3.1) Проверяем синтаксис и существование массива + индекс
            ai.arrayName.VisitP(this);
            ai.index.VisitP(this);

            // 2.3.2) Берём информацию о массиве
            String arrName = ai.arrayName.getName();
            SymbolInfo arrInfo = SymbolTable.SymTable.get(arrName);
            if (arrInfo == null || !arrInfo.Typ.isArray()) {
                throw new SemanticException(
                        arrName + " не является массивом",
                        ai.arrayName.getPos()
                );
            }
            // 2.3.3) Сравниваем элементный тип
            SemanticType newElemType = exprType;
            if (arrInfo.ElementTyp != null
                    && !CalcTypes.AssignComparable(arrInfo.ElementTyp, newElemType)) {
                throw new SemanticException(
                        "Несовместимость типов элементов массива. Ожидался " +
                                arrInfo.ElementTyp + ", получен " + newElemType,
                        ai.arrayName.getPos()
                );
            }
            // 2.3.4) Если ещё не было ElementTyp — сохраняем
            if (arrInfo.ElementTyp == null) {
                arrInfo.ElementTyp = newElemType;
            }
        }
        else {
            throw new SemanticException(
                    "Нельзя присваивать в выражение типа " +
                            ass.LValue.getClass().getSimpleName(),
                    ass.LValue.getPos()
            );
        }

        // 3) Рекурсивно проверяем RHS-выражение
        ass.Expr.VisitP(this);
    }

    @Override
    public void VisitAssignPlus(AssignPlusNode ass) throws SemanticException {
        // 1) LValue может быть IdNode или ArrayIndexNode — проверяем оба
        if (ass.LValue instanceof IdNode id) {
            // Проверяем, что переменная определена и это VarName
            if (!SymbolTable.SymTable.containsKey(id.Name)) {
                throw new SemanticException(
                        "Переменная " + id.Name + " не определена",
                        id.pos
                );
            }
            SymbolInfo info = SymbolTable.SymTable.get(id.Name);
            if (info.Kind != KindType.VarName) {
                throw new SemanticException(
                        id.Name + " не является именем переменной",
                        id.pos
                );
            }
        }
        else if (ass.LValue instanceof ArrayIndexNode ai) {
            // Проверяем корректность массива и индекса
            ai.arrayName.VisitP(this);
            ai.index.VisitP(this);
            // Убедимся, что это массив
            SymbolInfo arrInfo = SymbolTable.SymTable.get(ai.arrayName.getName());
            if (arrInfo == null || !arrInfo.Typ.isArray()) {
                throw new SemanticException(
                        ai.arrayName.getName() + " не является массивом",
                        ai.arrayName.getPos()
                );
            }
        }
        else {
            throw new SemanticException(
                    "Нельзя использовать += с " +
                            ass.LValue.getClass().getSimpleName(),
                    ass.LValue.getPos()
            );
        }

        // 2) Получаем тип LValue через CalcType (для a[i] — элемент массива)
        SemanticType leftType = CalcTypes.CalcType((ExprNode) ass.LValue);
        SemanticType rightType = CalcTypes.CalcType(ass.Expr);

        // 3) Проверяем, что это число
        if (!SymbolTable.NumTypes.contains(leftType)) {
            throw new SemanticException(
                    "Операция += не определена для типа " + leftType,
                    ass.LValue.getPos()
            );
        }
        // 4) Проверяем совместимость
        if (!CalcTypes.AssignComparable(leftType, rightType)) {
            throw new SemanticException(
                    "Переменной " + ass.LValue.getName() +
                            " типа " + leftType +
                            " нельзя += выражение типа " + rightType,
                    ass.LValue.getPos()
            );
        }

        // 5) Рекурсивно проверяем RHS
        ass.Expr.VisitP(this);
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
    public void VisitArrayIndex(ArrayIndexNode ai) throws SemanticException {
        // Сначала проверяем имя массива как IdNode
        ai.arrayName.VisitP(this);

        // Проверяем, что тип в таблице — ArrayType
        SymbolInfo info = SymbolTable.SymTable.get(ai.arrayName.getName());
        if (info.Typ.isArray()) {
            throw new SemanticException(ai.arrayName.getName() + " не является массивом", ai.arrayName.getPos());
        }

        // Проверяем индекс как выражение
        ai.index.VisitP(this);
        SemanticType idxType = CalcTypes.CalcType(ai.index);
        if (idxType != SemanticType.IntType) {
            throw new SemanticException("Индекс массива должен быть целым", ai.index.pos);
        }
    }
}
