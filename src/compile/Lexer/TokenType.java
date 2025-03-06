package compile.Lexer;

public enum TokenType {Int, DoubleLiteral, StringLiteral,
    Id,
    Plus, Minus, Multiply, Divide, Dot,
    Semicolon, LPar, RPar, LBrace, RBrace, Comma, Colon, LBracket, RBracket,
    Assign, AssignPlus, AssignMinus, AssignMult, AssignDiv,
    Equal, Less, LessEqual, Greater, GreaterEqual, NotEqual,
    tkAnd, tkOr, tkNot,
    Eof,
    tkTrue, tkFalse, tkIf, tkThen, tkElse, tkWhile, tkDo
}
