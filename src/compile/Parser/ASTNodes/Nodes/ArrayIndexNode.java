package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Dictionary;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

import java.util.List;

public class ArrayIndexNode extends ExprNode implements LValueNode{
    public final LValueNode arrayName;
    public final ExprNode index;

    public ArrayIndexNode(LValueNode arrayName, ExprNode index, Position pos) {
        this.pos = pos;
        this.arrayName = arrayName;
        this.index = index;
    }

    @Override
    public Object Eval() {
        Object container = ((ExprNode) arrayName).Eval();

        if (!(container instanceof List)) {
            throw new RuntimeException("переменная " + arrayName.getName() + " не является массивом");
        }

        Object idx = index.Eval();
        if (!(idx instanceof Integer)) {
            throw new RuntimeException("индекс массива должен быть целым");
        }

        return ((List<?>)container).get((Integer)idx);
    }

    @Override
    public void setValue(Object value) {
        Object array = Dictionary.VarValues.get(arrayName.getName());

        if (!(array instanceof List)) {
            throw new RuntimeException("переменная " + arrayName.getName() + " не является массивом");
        }

        Object idx = index.Eval();
        if (!(idx instanceof Integer)) {
            throw new RuntimeException("индекс массива должен быть целым");
        }

        ((List<Object>)array).set((Integer)idx, value);
    }

    @Override
    public Position getPos() {
        return pos;
    }

    @Override
    public <T> T Visit(IVisitor<T> v) {
        return v.VisitArrayIndex(this);
    }

    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitArrayIndex(this);
    }

    @Override
    public String getName() {
        return arrayName.getName();
    }
}