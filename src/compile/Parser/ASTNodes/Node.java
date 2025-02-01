package compile.Parser.ASTNodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

public class Node {
    public Position pos;
    public <T> T Visit(IVisitor<T> v){
        return v.VisitNode(this);
    }
    public void VisitP(IVisitorP v){
        v.VisitNode(this);
    }
}
