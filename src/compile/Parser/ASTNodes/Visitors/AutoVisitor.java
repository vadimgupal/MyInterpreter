package compile.Parser.ASTNodes.Visitors;

import compile.Parser.ASTNodes.Nodes.*;
import compile.SemanticException;

public class AutoVisitor implements IVisitorP {
    @Override
    public void VisitNode(Node bin) {

    }

    @Override
    public void VisitExprNode(ExprNode bin) {

    }

    @Override
    public void VisitStatementNode(StatementNode bin) {

    }

    @Override
    public void VisitBinOp(BinOpNode bin) {
        bin.left.VisitP(this);
        bin.right.VisitP(this);
    }

    @Override
    public void VisitStatementList(StatementListNode stl) throws SemanticException {
        for(StatementNode x : stl.lst)
            x.VisitP(this);
    }

    @Override
    public void VisitExprList(ExprListNode exlist) {
        for(ExprNode x : exlist.lst)
            x.VisitP(this);
    }

    @Override
    public void VisitInt(IntNode n) {

    }

    @Override
    public void VisitDouble(DoubleNode d) {

    }

    @Override
    public void VisitId(IdNode id) {

    }

    @Override
    public void VisitAssign(AssignNode ass) throws SemanticException {
        ass.Ident.VisitP(this);
        ass.Expr.VisitP(this);
    }

    @Override
    public void VisitAssignPlus(AssignPlusNode ass) throws SemanticException {
        ass.Ident.VisitP(this);
        ass.Expr.VisitP(this);
    }

    @Override
    public void VisitIf(IfNode ifn) throws SemanticException {
        ifn.Condition.VisitP(this);
        ifn.ThenStat.VisitP(this);
        if(!ifn.ElseStat.equals(null))
            ifn.ElseStat.VisitP(this);
    }

    @Override
    public void VisitWhile(WhileNode whn) throws SemanticException {
        whn.Condition.VisitP(this);
        whn.Stat.VisitP(this);
    }

    @Override
    public void VisitProcCall(ProcCallNode p) throws SemanticException {
        p.Pars.VisitP(this);
    }

    @Override
    public void VisitFuncCall(FuncCallNode f) {
        f.Pars.VisitP(this);
    }

    @Override
    public void VisitArray(ArrayLiteral arr) {

    }

    @Override
    public void VisitString(StringLiteral str) {

    }
}
