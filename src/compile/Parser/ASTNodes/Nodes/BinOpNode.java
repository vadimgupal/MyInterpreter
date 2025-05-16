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

//    private SemanticType getType(Object ex) {
//        if(ex instanceof StringLiteral sl)
//            return SemanticType.StringType;
//        else if(ex instanceof DoubleNode dn)
//            return SemanticType.DoubleType;
//        else if(ex instanceof IntNode i)
//            return SemanticType.IntType;
//        else return SemanticType.ObjectType;
//    }

    @Override
    public Object Eval() {
        Object leftVal  = left.Eval();
        Object rightVal = right.Eval();

        // Строковая конкатенация
        if (leftVal instanceof String && rightVal instanceof String && op == OpType.opPlus) {
            return ((String)leftVal) + ((String)rightVal);
        }

        // --- Новый блок: оба интера -- целочисленное исполнение ---
        if (leftVal  instanceof Integer && rightVal instanceof Integer) {
            int l = (Integer) leftVal;
            int r = (Integer) rightVal;
            return switch (op) {
                case opPlus         -> l + r;
                case opMinus        -> l - r;
                case opMultiply     -> l * r;
                case opDivide       -> l / r;              // целочисленное деление
                case opLess         -> l < r;
                case opGreater      -> l > r;
                case opLessEqual    -> l <= r;
                case opGreaterEqual -> l >= r;
                case opEqual        -> l == r;
                case opNotEqual     -> l != r;
                default             -> throw new RuntimeException("Unsupported op for ints: " + op);
            };
        }

        // --- Старая ветка для смешанных / вещественных ---
        double ld = leftVal  instanceof Integer ? (Integer)leftVal  : (double)leftVal;
        double rd = rightVal instanceof Integer ? (Integer)rightVal : (double)rightVal;
        return switch (op) {
            case opPlus         -> ld + rd;
            case opMinus        -> ld - rd;
            case opMultiply     -> ld * rd;
            case opDivide       -> ld / rd;
            case opLess         -> ld < rd;
            case opGreater      -> ld > rd;
            case opLessEqual    -> ld <= rd;
            case opGreaterEqual -> ld >= rd;
            case opEqual        -> ld == rd;
            case opNotEqual     -> ld != rd;
            default             -> throw new RuntimeException("Unsupported operation: " + op);
        };
    }


    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitBinOp(this);
    }
    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitBinOp(this);
    }
}
