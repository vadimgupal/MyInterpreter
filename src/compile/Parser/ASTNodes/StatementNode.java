package compile.Parser.ASTNodes;

import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

public class StatementNode extends Node{
    public void Execute(){ };
    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitStatementNode(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitStatementNode(this);
    }
}
