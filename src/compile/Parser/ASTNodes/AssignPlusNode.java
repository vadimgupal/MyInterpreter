package compile.Parser.ASTNodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

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
        double currentValue = Dictionary.VarValues.getOrDefault(Ident.Name, 0.0);
        double newValue = currentValue + Expr.Eval();
        Dictionary.VarValues.put(Ident.Name, newValue);
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitAssignPlus(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitAssignPlus(this);
    }
}