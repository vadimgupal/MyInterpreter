package compile.Parser.ASTNodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

public class IdNode extends ExprNode{
    public String Name;
    public IdNode(String name, Position pos) {
        this.Name = name;
        this.pos = pos;
    }

    @Override
    public double Eval() {
        return Dictionary.VarValues.get(Name);
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitId(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitId(this);
    }
}
