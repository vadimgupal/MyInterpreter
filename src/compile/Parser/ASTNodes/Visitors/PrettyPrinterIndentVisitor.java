package compile.Parser.ASTNodes.Visitors;

import compile.Parser.ASTNodes.Nodes.*;

import java.util.stream.Collectors;

public class PrettyPrinterIndentVisitor implements IVisitor<String> {
    private int indent = 0;
    public String Ind() {
        return " ".repeat(Math.max(0, indent));
    }

    public String IndInc() {
        indent += 2;
        return "";
    }

    public String IndDec() {
        indent-=2;
        return "";
    }

    @Override
    public String VisitNode(Node n){
        return Ind() + n.Visit(this);
    }

    @Override
    public String VisitExprNode(ExprNode bin) {
        return bin.Visit(this);
    }

    @Override
    public String VisitStatementNode(StatementNode bin) {
        return Ind()+bin.Visit(this);
    }

    @Override
    public String VisitBinOp(BinOpNode bin) {
        return bin.left.Visit(this) + bin.op + bin.right.Visit(this);
    }

    @Override
    public String VisitStatementList(StatementListNode stl) {
        return Ind()+"{\n"+IndInc()+stl.lst.stream().map(x->x.Visit(this))
                .collect(Collectors.joining(";\n"))+IndDec()+"\n"+Ind()+"}";
    }

    @Override
    public String VisitExprList(ExprListNode exlist) {
        return exlist.lst.stream().map(x->x.Visit(this)).collect(Collectors.joining(","));
    }

    @Override
    public String VisitInt(IntNode n) {
        return String.valueOf(n.Value);
    }

    @Override
    public String VisitDouble(DoubleNode d) {
        return String.valueOf(d.Value);
    }

    @Override
    public String VisitId(IdNode id) {
        return id.Name;
    }

    @Override
    public String VisitAssign(AssignNode ass) {
        return Ind()+ass.LValue.getName()+" = "+ass.Expr.Visit(this);
    }

    @Override
    public String VisitAssignPlus(AssignPlusNode ass) {
        return Ind()+ass.LValue.getName()+" += "+ass.Expr.Visit(this);
    }

    @Override
    public String VisitIf(IfNode ifn) {
        String res="if "+ifn.Condition.Visit(this) + " then "+ifn.ThenStat.Visit(this);
        if (ifn != null)
            res+=" else "+ifn.ElseStat.Visit(this);
        return res;
    }

    @Override
    public String VisitWhile(WhileNode whn) {
        return Ind()+"while "+whn.Condition.Visit(this)+"\n"+whn.Stat.Visit(this);
    }

    @Override
    public String VisitProcCall(ProcCallNode p) {
        return Ind()+p.Name.Name+"("+p.Pars.Visit(this)+")";
    }

    @Override
    public String VisitFuncCall(FuncCallNode f) {
        return f.Name.Name+"("+f.Pars.Visit(this)+")";
    }

    @Override
    public String VisitArray(ArrayLiteral arr) {
        return String.valueOf(arr.value);
    }

    @Override
    public String VisitString(StringLiteral str) {
        return str.value;
    }

    @Override
    public String VisitArrayIndex(ArrayIndexNode ai) {
        return "";
    }
}
