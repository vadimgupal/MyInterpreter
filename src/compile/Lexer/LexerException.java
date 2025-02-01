package compile.Lexer;

import compile.BaseCompilerException;

public class LexerException extends BaseCompilerException {
  public LexerException(String msg, Position p) {
    super(msg, p);
  }
}