package compile.Parser.ASTNodes.Visitor;

import compile.Parser.ASTNodes.*;

public interface IVisitor<T> {
    T VisitNode(Node bin);
    T VisitExprNode(ExprNode bin);
    T VisitStatementNode(StatementNode bin);
    T VisitBinOp(BinOpNode bin);
    T VisitStatementList(StatementListNode stl);
    T VisitExprList(ExprListNode exlist);
    T VisitInt(IntNode n);
    T VisitDouble(DoubleNode d);
    T VisitId(IdNode id);
    T VisitAssign(AssignNode ass);
    T VisitAssignPlus(AssignPlusNode ass);
    T VisitIf(IfNode ifn);
    T VisitWhile(WhileNode whn);
    T VisitProcCall(ProcCallNode p);
    T VisitFuncCall(FuncCallNode f);
}
