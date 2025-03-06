package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Dictionary;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

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
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitAssign(this);
    }
}
