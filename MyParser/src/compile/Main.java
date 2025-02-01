package compile;

import compile.Lexer.Lexer;
import compile.Lexer.LexerException;
import compile.Parser.ASTNodes.StatementNode;
import compile.Parser.ASTNodes.Visitor.PrettyPrinterIndentVisitor;
import compile.Parser.Parser;

public class Main {
    public static void OutputError(String prefix, BaseCompilerException e, String[] lines) {
        String line = lines[e.getPos().getLine()-1];
        System.out.println(line);
        System.out.println(" ".repeat(e.getPos().getColumn() - 1) + "^");
        System.out.println(prefix + " " + e.getPos().toString() + ":" + e.getMessage());
    }
    public static void main(String[] args) throws LexerException {
        String s= """
                        i = 1;
                        sum = 0; 
                        n = 100000000;
                        while i<100000000 do 
                        {
                         sum += 1/i
                          i += 1
                         };
                        Print(sum)
                """;
        Lexer lex = new Lexer(s);
        try{
            Parser par = new Parser(lex);
            StatementNode progr = par.MainProgram();
            //progr.Execute();
            //System.out.println(progr);
            System.out.println(progr.Visit(new PrettyPrinterIndentVisitor()));
        } catch (LexerException e) {
            OutputError("Lexer error:", e, lex.Lines());
        } catch (SyntaxException e) {
            OutputError("Parser error: ", e, lex.Lines());
        }
    }
}