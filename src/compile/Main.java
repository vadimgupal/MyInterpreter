package compile;

import compile.Lexer.Lexer;
import compile.Lexer.LexerException;
import compile.Lexer.Token;
import compile.Lexer.TokenType;
import compile.Parser.ASTNodes.Nodes.StatementNode;
import compile.Parser.ASTNodes.Visitors.SemanticCheckVisitor;
import compile.Parser.Parser;

import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Main {
    public static void OutputError(String prefix, BaseCompilerException e, String[] lines) {
        String line = lines[e.getPos().getLine()-1];
        System.out.println(line);
        System.out.println(" ".repeat(e.getPos().getColumn() - 1) + "^");
        System.out.println(prefix + " " + e.getPos().toString() + ":" + e.getMessage());
    }
    public static void main(String[] args) throws LexerException {
        String s= """
                        a=[[1,2],[3,4]];
                        i=0;
                        j=0;
                        while (i < 2) do {
                        j=0;
                        while (j < 2) do {
                        print(a[i][j]);
                        j+=1
                        };
                        i+=1
                        };
                        b=[1,2,3,4];
                        print(b[a[i-1][j-1]-1])
                """;
        Lexer lex = new Lexer(s);
        try{
            Parser par = new Parser(lex);
            StatementNode progr = par.MainProgram();
            progr.VisitP(new SemanticCheckVisitor());
            progr.Execute();
        } catch (LexerException e) {
            OutputError("Lexer error:", e, lex.Lines());
        } catch (SyntaxException e) {
            OutputError("Parser error: ", e, lex.Lines());
        } catch (SemanticException e) {
            OutputError("Semantic error: ", e, lex.Lines());
        }
    }
}