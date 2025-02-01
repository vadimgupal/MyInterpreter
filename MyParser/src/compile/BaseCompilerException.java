package compile;

import compile.Lexer.Position;

public class BaseCompilerException extends Exception {
    public BaseCompilerException(String message) {
        super(message);
    }
    private Position pos;

    public BaseCompilerException(String message, Position position) {
        super(message);
        this.pos = position;
    }

    public Position getPos() {
        return pos;
    }
}
