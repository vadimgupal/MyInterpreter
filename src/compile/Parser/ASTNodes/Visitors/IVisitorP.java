package compile.Parser.ASTNodes.Visitors;

import compile.Parser.ASTNodes.Nodes.*;

public interface IVisitorP {
    void VisitNode(Node bin);
    void VisitExprNode(ExprNode bin);
    void VisitStatementNode(StatementNode bin);
    void VisitBinOp(BinOpNode bin);
    void VisitStatementList(StatementListNode stl);
    void VisitExprList(ExprListNode exlist);
    void VisitInt(IntNode n);
    void VisitDouble(DoubleNode d);
    void VisitId(IdNode id);
    void VisitAssign(AssignNode ass);
    void VisitAssignPlus(AssignPlusNode ass);
    void VisitIf(IfNode ifn);
    void VisitWhile(WhileNode whn);
    void VisitProcCall(ProcCallNode p);
    void VisitFuncCall(FuncCallNode f);
}
