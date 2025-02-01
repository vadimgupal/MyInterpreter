package compile.Parser.ASTNodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

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
