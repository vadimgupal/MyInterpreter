package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

public class FuncCallNode extends ExprNode {
    public IdNode Name;
    public ExprListNode Pars;
    public FuncCallNode(IdNode name, ExprListNode pars, Position pos) {
        this.Name = name;
        this.Pars = pars;
        this.pos = pos;
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitFuncCall(this);
    }
    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitFuncCall(this);
    }
}
