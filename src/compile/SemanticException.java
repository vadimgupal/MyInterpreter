package compile;

import compile.Lexer.Position;

public class SemanticException extends BaseCompilerException {
  public SemanticException(String msg, Position p) {
    super(msg, p);
  }
}