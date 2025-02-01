package compile.Parser.ASTNodes;

import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

import java.util.ArrayList;
import java.util.List;

public class ExprListNode extends Node{
    public List<ExprNode> lst = new ArrayList<>();
    public void Add(ExprNode ex) {
        lst.add(ex);
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitExprList(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitExprList(this);
    }
}
