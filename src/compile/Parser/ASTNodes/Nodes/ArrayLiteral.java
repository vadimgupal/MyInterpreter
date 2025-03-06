package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;

import java.util.List;

public class ArrayLiteral extends ExprNode {
    public List<ExprNode> value;
    public ArrayLiteral(List<ExprNode> val, Position pos) {
        this.value=val;
        this.pos=pos;
    }

    @Override
    public List<ExprNode> Eval() {
        return value;
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitArray(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitArray(this);
    }
}
