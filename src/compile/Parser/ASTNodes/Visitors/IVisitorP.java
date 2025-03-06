package compile.Parser.ASTNodes.Visitors;

import compile.Parser.ASTNodes.Nodes.*;
import compile.SemanticException;

public interface IVisitorP {
    void VisitNode(Node bin);
    void VisitExprNode(ExprNode bin);
    void VisitStatementNode(StatementNode bin);
    void VisitBinOp(BinOpNode bin);
    void VisitStatementList(StatementListNode stl) throws SemanticException;
    void VisitExprList(ExprListNode exlist);
    void VisitInt(IntNode n);
    void VisitDouble(DoubleNode d);
    void VisitId(IdNode id);
    void VisitAssign(AssignNode ass) throws SemanticException;
    void VisitAssignPlus(AssignPlusNode ass) throws SemanticException;
    void VisitIf(IfNode ifn) throws SemanticException;
    void VisitWhile(WhileNode whn) throws SemanticException;
    void VisitProcCall(ProcCallNode p) throws SemanticException;
    void VisitFuncCall(FuncCallNode f);
    void VisitArray(ArrayLiteral arr);
    void VisitString(StringLiteral str);
}
