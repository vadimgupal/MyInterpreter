package compile.Parser.ASTNodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

public class BinOpNode extends ExprNode{
    public ExprNode left, right;
    public char op;
    public BinOpNode(ExprNode l, ExprNode r, char o, Position pos) {
        this.left=l;
        this.right=r;
        this.op=o;
        this.pos=pos;
    }

    @Override
    public double Eval() {
        double l=left.Eval();
        double r= right.Eval();
        return switch (op) {
            case '+' -> l + r;
            case '-' -> l - r;
            case '*' -> l * r;
            case '/' -> l / r;
            case '<' -> l < r ? 1 : 0;
            case '>' -> l > r ? 1 : 0;
            default -> 0;
        };
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitBinOp(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitBinOp(this);
    }
}
