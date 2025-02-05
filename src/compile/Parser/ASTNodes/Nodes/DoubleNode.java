package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;

public class DoubleNode extends ExprNode {
    public double Value;

    public DoubleNode(double val, Position pos) {
        this.Value = val;
        this.pos = pos;
    }

    @Override
    public double Eval() {
        return Value;
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitDouble(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitDouble(this);
    }
}
