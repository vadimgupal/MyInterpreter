package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Dictionary;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;

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
