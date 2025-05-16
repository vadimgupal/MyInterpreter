package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Dictionary;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

public class AssignNode extends StatementNode{
    public LValueNode LValue;
    public ExprNode Expr;

    public AssignNode(LValueNode lvalue, ExprNode ExprNode, Position pos) {
        this.LValue = lvalue;
        this.Expr = ExprNode;
        this.pos = pos;
    }

    @Override
    public void Execute() {
        LValue.setValue(Expr.Eval());
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitAssign(this);
    }
    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitAssign(this);
    }
}
