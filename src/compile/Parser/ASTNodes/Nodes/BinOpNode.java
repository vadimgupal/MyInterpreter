package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.Types.OpType;

public class BinOpNode extends ExprNode{
    public ExprNode left, right;
    public OpType op;
    public BinOpNode(ExprNode l, ExprNode r, OpType o, Position pos) {
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
            case opPlus -> l + r;
            case opMinus -> l - r;
            case opMultiply -> l * r;
            case opDivide -> l / r;
            case opLess -> l < r ? 1 : 0;
            case opGreater -> l > r ? 1 : 0;
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
