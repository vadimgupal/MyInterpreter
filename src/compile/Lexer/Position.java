package compile.Lexer;

public class Position {
    int line;
    int column;

    public Position(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("(").append(getLine()).append(",").append(getColumn()).append(")");
        return sb.toString();
    }
}
