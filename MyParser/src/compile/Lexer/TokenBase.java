package compile.Lexer;

public class TokenBase {
    public Position pos;
    public Object value;

    public TokenBase(Position pos, Object value) {
        this.pos = pos;
        this.value = value;
    }
}
