package compile.Lexer;

public class LexserBase {
    protected String code;
    protected int line;
    protected int column;
    protected int cur;
    protected int start;
    boolean atEoln;

    public LexserBase(String code) {
        this.code = code;
        line = 1;
        column = 0;
        cur = 0;
        start = 0;
        atEoln = false;
    }

    protected Position CurrentPosition() {
        return new Position(line, column);
    }

    protected boolean IsAtEnd() {
        return cur>=code.length();
    }

    protected char NextChar() {
        char res = PeekChar();
        if(atEoln) {
            atEoln = false;
            ++line;
            column = 0;
        }

        if(res=='\0') {
            return res;
        }

        if(res=='\n') {
            atEoln = true;
        }
        ++cur;
        ++column;
        return res;
    }

    protected boolean IsMatch(char expected) {
        boolean res = PeekChar() == expected;
        if(res) {
            NextChar();
        }
        return res;
    }

    protected char PeekChar() {
        return IsAtEnd() ? '\0' : code.charAt(cur);
    }

    protected char PeekNextChar() {
        int pos = cur + 1;
        return pos>code.length() ? '\0' : code.charAt(pos);
    }

    protected static boolean isAlpha(char c) {
        return Character.toString(c).matches("[A-Za-zА-Яа-яёЁ_]");
    }

    protected static boolean IsAlphaNumeric(char c) {
        return isAlpha(c) || Character.isDigit(c);
    }

    public String[] Lines() {
        return code.split("\\n");
    }
}
