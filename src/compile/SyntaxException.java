package compile;

import compile.Lexer.Position;

public class SyntaxException extends BaseCompilerException {
  public SyntaxException(String msg, Position p) {
    super(msg, p);
  }
}