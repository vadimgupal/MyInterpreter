package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;

import java.util.List;
import java.util.stream.Collectors;

public class ArrayLiteral extends ExprNode {
    public ExprListNode value;
    public ArrayLiteral(ExprListNode val, Position pos) {
        this.value=val;
        this.pos=pos;
    }

    @Override
    public List<Object> Eval() {
        return value.lst.stream().map(x -> x.Eval()).collect(Collectors.toList());
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
