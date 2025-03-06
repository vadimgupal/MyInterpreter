package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;

public class StringLiteral extends ExprNode {
    public String value;

    public StringLiteral(String s, Position pos) {
        this.value = s;
        this.pos = pos;
    }

    @Override
    public Object Eval() {
        return value;
    }


}
