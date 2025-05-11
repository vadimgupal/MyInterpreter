package compile.Parser;

import compile.Lexer.Lexer;
import compile.Lexer.LexerException;
import compile.Lexer.Token;
import compile.Lexer.TokenType;
import compile.SyntaxException;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ParserBase {
    private final Lexer lex;

    protected int current = 0;
    protected Token curToken;

    public ParserBase(Lexer lex) throws LexerException {
        this.lex = lex;
        NextLexem();
    }

    public Token NextLexem() throws LexerException {
        Token res = curToken;
        curToken = lex.NextToken();
        if (!IsAtEnd())
            ++current;
        return res;
    }

    public boolean At(TokenType... types) {
        return Arrays.stream(types).anyMatch(typ->typ.equals((PeekToken().typ)));
    }

    public void Check(TokenType... types) throws SyntaxException {
        if (!At(types))
            ExeptedError(types);
    }

    public boolean IsMatch(TokenType... types) throws LexerException {
        boolean res = At(types);
        if (res)
            NextLexem();
        return res;
    }

    public Token Requires(TokenType... types) throws LexerException, SyntaxException {
        if(At(types))
            return NextLexem();
        else
            ExeptedError(types);
        return null;
    }

    public boolean IsAtEnd() {
        return PeekToken().typ == TokenType.Eof;
    }
    public Token PeekToken() {
        return curToken;
    }

    public Token CurrentToken() {
        return curToken;
    }

    public void ExeptedError(TokenType... types) throws SyntaxException {
        String expectedTypes = Arrays.stream(types)
                .map(TokenType::toString) // Преобразуем каждый TokenType в строку
                .collect(Collectors.joining(" или "));

        String message = String.format("ожидалось %s, но найдено %s", expectedTypes, PeekToken().typ);
        throw new SyntaxException(message, PeekToken().pos);
    }
}
