package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Dictionary;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

public class IdNode extends ExprNode implements LValueNode{
    public String Name;
    public IdNode(String name, Position pos) {
        this.Name = name;
        this.pos = pos;
    }

    @Override
    public Object Eval() {
        return Dictionary.VarValues.get(Name);
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitId(this);
    }
    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitId(this);
    }

    @Override
    public void setValue(Object value) {
        Dictionary.VarValues.put(Name, value);
    }

    @Override
    public Position getPos() {
        return pos;
    }

    @Override
    public String getName() {
        return Name;
    }
}
