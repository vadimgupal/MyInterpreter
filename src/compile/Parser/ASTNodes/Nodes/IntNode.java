package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;

public class IntNode extends ExprNode{
    public int Value;
    public IntNode(int val, Position pos) {
        this.Value = val;
        this.pos = pos;
    }

    @Override
    public double Eval() {
        return Value;
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitInt(this);
    }
    public void VisitP(IVisitorP v){
        v.VisitInt(this);
    }
}
