package compile.Parser.ASTNodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

public class AssignNode extends StatementNode{
    public IdNode Ident;
    public ExprNode Expr;

    public AssignNode(IdNode ident, ExprNode ExprNode, Position pos) {
        this.Ident = ident;
        this.Expr = ExprNode;
        this.pos = pos;
    }

    @Override
    public void Execute() {
        Dictionary.VarValues.put(Ident.Name, Expr.Eval());
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitAssign(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitAssign(this);
    }
}
