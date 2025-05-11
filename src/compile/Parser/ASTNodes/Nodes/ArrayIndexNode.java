package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Dictionary;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

import java.util.List;

public class ArrayIndexNode extends ExprNode {
    public final IdNode arrayName;
    public final ExprNode index;

    public ArrayIndexNode(IdNode arrayName, ExprNode index, Position pos) {
        this.pos = pos;
        this.arrayName = arrayName;
        this.index = index;
    }

    @Override
    public Object Eval() {
        Object array = Dictionary.VarValues.get(arrayName.Name);

        if (!(array instanceof List)) {
            throw new RuntimeException("переменная " + arrayName.Name + " не является массивом");
        }

        Object idx = index.Eval();
        if (!(idx instanceof Integer)) {
            throw new RuntimeException("индекс массива должен быть целым");
        }

        return ((List<?>)array).get((Integer)idx);
    }

    @Override
    public <T> T Visit(IVisitor<T> v) {
        return v.VisitArrayIndex(this);
    }

    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitArrayIndex(this);
    }
}