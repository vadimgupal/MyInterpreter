package compile.Lexer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lexer extends LexserBase{
    private final Map<String, TokenType> Keywords = new HashMap<>() {{
        put("True", TokenType.tkTrue);
        put("False", TokenType.tkFalse);
        put("if", TokenType.tkIf);
        put("then", TokenType.tkThen);
        put("else", TokenType.tkElse);
        put("while", TokenType.tkWhile);
        put("do", TokenType.tkDo);
    }};

    public Lexer(String code) {
        super(code);
    }

    public Token NextToken() throws LexerException{
        char c = super.NextChar();
        while (Character.isWhitespace(c)) {
            c=super.NextChar();
        }

        Position pos = super.CurrentPosition();
        start = cur - 1;
        Token res = null;
        switch (c){
            case '\0':
                res = new Token(TokenType.Eof, pos, "Eof");
                break;
            case ',' :
                res = new Token(TokenType.Comma, pos, ',');
                break;
            case ')' :
                res = new Token(TokenType.RPar, pos, ')');
                break;
            case '(' :
                res = new Token(TokenType.LPar, pos, '(');
                break;
            case '}' :
                res = new Token(TokenType.RBrace, pos, '}');
                break;
            case '{' :
                res = new Token(TokenType.LBrace, pos, '{');
                break;
            case '+' :
                if(IsMatch('='))
                res = new Token(TokenType.AssignPlus, pos, "+=");
                else
                    res = new Token(TokenType.Plus, pos, "+");
                break;
            case '-' :
                if(IsMatch('='))
                    res = new Token(TokenType.AssignMinus, pos, "-=");
                else
                    res = new Token(TokenType.Minus, pos, "+");
                break;
            case '*' :
                if(IsMatch('='))
                    res = new Token(TokenType.AssignMult, pos, "*=");
                else
                    res = new Token(TokenType.Multiply, pos, "*");
                break;
            case '/' :
                if(IsMatch('/'))
                    while (PeekChar()!='\n' && !IsAtEnd())
                        NextChar();
                else {
                    if (IsMatch('='))
                        res = new Token(TokenType.AssignDiv, pos, "/=");
                    else
                        res = new Token(TokenType.Divide, pos, "/");
                }
                break;
            case ';' :
                res = new Token(TokenType.Semicolon, pos, ";");
                break;
            case '!' :
                if (IsMatch('='))
                    res = new Token(TokenType.NotEqual, pos, "!=");
                else
                    res = new Token(TokenType.tkNot, pos, "!");
                break;
            case '=' :
                if (IsMatch('='))
                    res = new Token(TokenType.Equal, pos, "==");
                else
                    res = new Token(TokenType.Assign, pos, "=");
                break;
            case '>' :
                if (IsMatch('='))
                    res = new Token(TokenType.GreaterEqual, pos, ">=");
                else
                    res = new Token(TokenType.Greater, pos, ">");
                break;
            case '<' :
                if (IsMatch('='))
                    res = new Token(TokenType.LessEqual, pos, "<=");
                else
                    res = new Token(TokenType.Less, pos, "<");
                break;
            case '&' :
                if(IsMatch('&'))
                    res = new Token(TokenType.tkAnd, pos, "&&");
                else throw new LexerException("Неверный символ " + c +" после &", CurrentPosition());
                break;
            case '|' :
                if(IsMatch('|'))
                    res = new Token(TokenType.tkOr, pos, "||");
                else throw new LexerException("Неверный символ " + c +" после |", CurrentPosition());
                break;
            case '\"' :
                res = GetString(pos);
                break;
            case '[':
                res = new Token(TokenType.LBracket,pos,"[");
                break;
            case ']':
                res = new Token(TokenType.RBracket,pos,"]");
                break;
            default:
                if(Character.isDigit(c))
                    res = GetNumber(pos);
                else if(isAlpha(c))
                    res = GetIdentifier(pos);
                else throw new LexerException("Неизвустный символ " + c +" в позиции"+pos.line+", "+pos.column, pos);
                break;
        }
        if(res == null){
            code.substring(start,cur);
        }
        return res;
    }

    private Token GetNumber(Position startPos) {
        while (Character.isDigit(PeekChar()))
            NextChar();
        if(PeekChar() == '.' && Character.isDigit(PeekNextChar()))
        {
            NextChar();
            while (Character.isDigit(PeekChar()))
                NextChar();
            String val = code.substring(start, cur);
            return new Token(TokenType.DoubleLiteral, startPos, Double.parseDouble(val));
        }
        String val1 = code.substring(start, cur);
        return new Token(TokenType.Int, startPos, Integer.parseInt(val1));
    }

    private Token GetString(Position startPos) {
        while (PeekChar() != '\"' && !IsAtEnd())
            NextChar();
        NextChar();
        String val = code.substring(start+1, cur - 1);
        return new Token(TokenType.StringLiteral, startPos, val);
    }

    private Token GetIdentifier(Position startPos) {
        while (IsAlphaNumeric(PeekChar()))
            NextChar();
        String val = code.substring(start, cur);
        TokenType typ = TokenType.Id;
        if(Keywords.containsKey(val))
            typ = Keywords.get(val);
        return new Token(typ, startPos, val);
    }
}
