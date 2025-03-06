package compile.Parser.ASTNodes.Visitors;

import compile.Parser.ASTNodes.Nodes.*;
import compile.SemanticException;
import compile.Types.*;

public class SemanticCheckVisitor extends AutoVisitor {
    @Override
    public void VisitAssign(AssignNode ass) throws SemanticException {
        if(!SymbolTable.SymTable.containsKey(ass.Ident.Name)) {
            SemanticType typ = CalcTypes.CalcType(ass.Expr);
            SymbolTable.SymTable.put(ass.Ident.Name, new SymbolInfo(ass.Ident.Name, KindType.VarName,typ));
        } else {
            if(SymbolTable.SymTable.get(ass.Ident.Name).Kind != KindType.VarName) {
                throw new SemanticException(ass.Ident.Name+" не является именем переменной",ass.Ident.pos);
            }
            SemanticType typ = CalcTypes.CalcType(ass.Expr);
            SemanticType idtype = SymbolTable.SymTable.get(ass.Ident.Name).Typ;
            if(!CalcTypes.AssignComparable(idtype,typ))
                throw new SemanticException("Переменной "+ass.Ident.Name+"типа "+idtype+" нельзя присвоить выражение типа "+typ,ass.Ident.pos);
        }
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
        if(!ifn.ElseStat.equals(null))
            ifn.ElseStat.VisitP(this);
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
            if(!CalcTypes.AssignComparable(sym.Params.get(i),tp))
                throw new SemanticException("Тип аргумента процедуры "+tp.toString()+" не соответствует типу формального параметра "+sym.Params.get(i).toString(),p.Pars.lst.get(i).pos);
        }
        if(sym.Typ!=SemanticType.NoType)
            throw new SemanticException("попытка вызвать функцию "+p.Name.Name+" как процедуру ",p.Name.pos);

    }
}
