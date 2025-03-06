package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;
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
    public Object Eval() {
        Object l=left.Eval();
        Object r= right.Eval();
        return switch (op) {
            case opPlus -> (double)l + (double)r;
            case opMinus -> (double)l - (double)r;
            case opMultiply -> (double)l * (double)r;
            case opDivide -> (double)l / (double)r;
            case opLess -> (double)l < (double)r ? 1 : 0;
            case opGreater -> (double)l > (double)r ? 1 : 0;
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
