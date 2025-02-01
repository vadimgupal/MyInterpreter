package compile.Lexer;

public class Token extends TokenBase {
    public TokenType typ;
    public Token(TokenType typ, Position pos, Object value) {
        super(pos,value);
        this.typ = typ;
    }
}
