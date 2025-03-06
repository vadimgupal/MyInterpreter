package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Dictionary;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

public class AssignPlusNode extends StatementNode {
    public IdNode Ident;
    public ExprNode Expr;

    public AssignPlusNode(IdNode Ident, ExprNode Expr, Position pos) {
        this.Ident = Ident;
        this.Expr = Expr;
        this.pos = pos;
    }

    @Override
    public void Execute() {
        Object currentValue = Dictionary.VarValues.getOrDefault(Ident.Name, 0.0);
        Object newValue = (double)currentValue + (double)Expr.Eval();
        Dictionary.VarValues.put(Ident.Name, newValue);
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitAssignPlus(this);
    }
    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitAssignPlus(this);
    }
}