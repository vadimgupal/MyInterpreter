package compile.Parser.ASTNodes.Nodes;

import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;

public abstract class StatementNode extends Node{
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
