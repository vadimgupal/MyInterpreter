package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;

public class StringLiteral extends ExprNode {
    public String value;

    public StringLiteral(String s, Position pos) {
        this.value = s;
        this.pos = pos;
    }

    @Override
    public Object Eval() {
        return value;
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitString(this);
    }

    @Override
    public void VisitP(IVisitorP v){
        v.VisitString(this);
    }
}
