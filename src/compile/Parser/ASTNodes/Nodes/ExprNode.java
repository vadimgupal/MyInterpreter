package compile.Parser.ASTNodes.Nodes;

import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

public abstract class ExprNode extends Node {
    public Object Eval(){
        return 0;
    }
    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitExprNode(this);
    }
    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitExprNode(this);
    }
}
