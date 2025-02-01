package compile.Parser.ASTNodes;

import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

public class ExprNode extends Node {
    public double Eval(){
        return 0;
    }
    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitExprNode(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitExprNode(this);
    }
}
