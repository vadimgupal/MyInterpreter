package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

public interface LValueNode {
    void setValue(Object value);
    Position getPos();
    String getName();
    <T> T Visit(IVisitor<T> v);
    void VisitP(IVisitorP v) throws SemanticException;
}