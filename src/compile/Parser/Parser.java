package compile.Parser;

import compile.Lexer.*;
import compile.Parser.ASTNodes.Nodes.*;
import compile.SemanticException;
import compile.SyntaxException;
import compile.Types.OpType;

public class Parser extends ParserBase {
    public Parser(Lexer lex) throws LexerException {
        super(lex);
    }

    public StatementNode MainProgram() throws SyntaxException, LexerException, SemanticException {
        current = 0;
        StatementNode res = StatementList();
        Requires(TokenType.Eof);
        return res;
    }

    public StatementNode StatementList() throws SyntaxException, LexerException, SemanticException {
        StatementListNode stl = new StatementListNode();
        stl.Add(Statement());
        //Check(TokenType.Semicolon, TokenType.Eof);
        while (IsMatch(TokenType.Semicolon)) {
            stl.Add(Statement());
        }
        return stl;
    }
    public StatementNode Statement() throws SyntaxException, LexerException, SemanticException {
        // id = expr
        // id += expr
        // id(exprlist)
        // if expr then stat [else stat]
        // while expr do stat
        // { statlist }
        Position pos = CurrentToken().pos;
        Check(TokenType.Id, TokenType.tkIf, TokenType.tkWhile, TokenType.LBrace);
        if(IsMatch(TokenType.tkIf)) {
            ExprNode cond = Expr();
            Requires(TokenType.tkThen);
            StatementNode thenstat = Statement();
            StatementNode elsestat = IsMatch(TokenType.tkElse) ? Statement() : null;
            return new IfNode(cond, thenstat, elsestat, pos);
        }
        else if(IsMatch(TokenType.tkWhile)) {
            ExprNode cond = Expr();
            Requires(TokenType.tkDo);
            StatementNode stat = Statement();
            return new WhileNode(cond, stat, pos);
        }
        else if(IsMatch(TokenType.LBrace)) {
            StatementNode stl = StatementList();
            Requires(TokenType.RBrace);
            stl.pos = pos;
            return stl;
        }
        else {
            LValueNode lvalue = LValue();
            //IdNode id = Ident();
            if(IsMatch(TokenType.Assign)){
                ExprNode ex = Expr();
                return new AssignNode(lvalue, ex, pos);
            }
            else if(IsMatch(TokenType.AssignPlus)) {
                ExprNode ex = Expr();
                return new AssignPlusNode(lvalue, ex, pos);
            }
            else if(IsMatch(TokenType.LPar)) {
                ExprListNode exlist = ExprList();
                ProcCallNode res = new ProcCallNode((IdNode) lvalue, exlist, pos);
                Requires(TokenType.RPar);
                return res;
            }
            else ExeptedError(TokenType.Assign, TokenType.AssignPlus, TokenType.LPar);
        }
        return null;
    }

    public LValueNode LValue() throws SyntaxException, LexerException, SemanticException {
        Position pos = CurrentToken().pos;
        LValueNode node = Ident();  // Получаем идентификатор

        while (IsMatch(TokenType.LBracket)) {
            ExprNode index = Expr();
            Requires(TokenType.RBracket);
            // теперь базовым узлом становится обращение к элементу
            node = new ArrayIndexNode(node, index, pos);
        }

        // Иначе это просто переменная
        return node;
    }

    public ExprListNode ExprList() throws LexerException, SyntaxException, SemanticException {
        ExprListNode lst = new ExprListNode();
        lst.Add(Expr());
        while(IsMatch(TokenType.Comma)) {
            lst.Add(Expr());
        }
        return lst;
    }

    public IdNode Ident() throws SyntaxException, LexerException {
        Token id = Requires(TokenType.Id);
        return new IdNode(id.value instanceof String ? (String)id.value : null, id.pos);
    }

    public ExprNode Expr() throws LexerException, SyntaxException, SemanticException {
        if(IsMatch(TokenType.LBracket)) {
          ExprListNode exlist = ExprList();
          Requires(TokenType.RBracket);
          return new ArrayLiteral(exlist,exlist.pos);
        }
        ExprNode ex = Comp();
        while(At(TokenType.Greater, TokenType.GreaterEqual,
                TokenType.Less, TokenType.LessEqual,
                TokenType.Equal, TokenType.NotEqual,
                TokenType.GreaterEqual, TokenType.LessEqual)) {
            Token op = NextLexem();
            ExprNode right = Comp();
            ex = new BinOpNode(ex, right, StringToOpType((String) op.value, op.pos), ex.pos);
        }
        return ex;
    }

    public ExprNode Comp() throws LexerException, SyntaxException, SemanticException {
        // term (addop term)*
        ExprNode ex = Term();
        while(At(TokenType.Plus, TokenType.Minus, TokenType.tkOr)) {
            Token op = NextLexem();
            ExprNode right = Term();
            ex = new BinOpNode(ex, right, StringToOpType((String) op.value, op.pos), ex.pos);
        }
        return ex;
    }

    public ExprNode Term() throws LexerException, SyntaxException, SemanticException {
        // Factor (multop Factor)*
        ExprNode ex = Factor();
        while(At(TokenType.Multiply, TokenType.Divide, TokenType.tkAnd)) {
            Token op = NextLexem();
            ExprNode right = Factor();
            ex = new BinOpNode(ex, right, StringToOpType((String) op.value, op.pos), ex.pos);
        }
        return ex;
    }

    public ExprNode Factor() throws LexerException, SyntaxException, SemanticException {
        Position pos = CurrentToken().pos;

        if (At(TokenType.Int)) {
            return new IntNode((int) NextLexem().value, pos);
        }
        if (At(TokenType.DoubleLiteral)) {
            return new DoubleNode((double) NextLexem().value, pos);
        }
        if (At(TokenType.StringLiteral)) {
            return new StringLiteral((String) NextLexem().value, pos);
        }

        // 3) Группа в скобках: (Expr)
        if (IsMatch(TokenType.LPar)) {
            ExprNode inside = Expr();
            Requires(TokenType.RPar);
            return inside;
        }

        // 4) Идентификатор: возможен вызов функции или цепочка [idx]
        if (At(TokenType.Id)) {
            IdNode id = Ident();

            // 4.1) Вызов функции
            if (IsMatch(TokenType.LPar)) {
                ExprListNode exlist = ExprList();
                Requires(TokenType.RPar);
                return new FuncCallNode(id, exlist, id.pos);
            }

            // 4.2) Цепочка индексов: a, a[i], a[i][j], ...
            LValueNode lv = id;
            while (IsMatch(TokenType.LBracket)) {
                ExprNode idx = Expr();
                Requires(TokenType.RBracket);
                lv = new ArrayIndexNode(lv, idx, pos);
            }
            return (ExprNode) lv;
        }

        throw new SyntaxException(
                "Expected literal, (…), id or […], but found " + PeekToken().typ,
                PeekToken().pos
        );
    }


    private OpType StringToOpType(String op, Position pos) throws SemanticException {
        return switch (op) {
            case "+" -> OpType.opPlus;
            case "-" -> OpType.opMinus;
            case "*" -> OpType.opMultiply;
            case "/" -> OpType.opDivide;
            case "<" -> OpType.opLess;
            case ">" -> OpType.opGreater;
            case ">=" -> OpType.opGreaterEqual;
            case "<=" -> OpType.opLessEqual;
            case "==" -> OpType.opEqual;
            case "!=" -> OpType.opNotEqual;
            case "&&" -> OpType.opAnd;
            case "||" -> OpType.opOr;
            case "!" -> OpType.opNot;
            default -> throw new SemanticException("Операция " + op + " не определена", pos);
        };
    }
}
