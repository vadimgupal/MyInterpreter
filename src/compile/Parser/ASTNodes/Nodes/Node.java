package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

public abstract class Node {
    public Position pos;
    public <T> T Visit(IVisitor<T> v){
        return v.VisitNode(this);
    }
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitNode(this);
    }
}
