package compile.Parser;

import compile.Lexer.*;
import compile.Parser.ASTNodes.*;
import compile.SyntaxException;

public class Parser extends ParserBase {
    public Parser(Lexer lex) throws LexerException {
        super(lex);
    }

    public StatementNode MainProgram() throws SyntaxException, LexerException {
        current = 0;
        StatementNode res = StatementList();
        Requires(TokenType.Eof);
        return res;
    }

    public StatementNode StatementList() throws SyntaxException, LexerException {
        StatementListNode stl = new StatementListNode();
        stl.Add(Statement());
        //Check(TokenType.Semicolon, TokenType.Eof);
        while (IsMatch(TokenType.Semicolon)) {
            stl.Add(Statement());
        }
        return stl;
    }
    public StatementNode Statement() throws SyntaxException, LexerException {
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
            StatementNode res = stl;
            res.pos = pos;
            return res;
        }
        else {
            IdNode id = Ident();
            if(IsMatch(TokenType.Assign)){
                ExprNode ex = Expr();
                return new AssignNode(id, ex, pos);
            }
            else if(IsMatch(TokenType.AssignPlus)) {
                ExprNode ex = Expr();
                return new AssignPlusNode(id, ex, pos);
            }
            else if(IsMatch(TokenType.LPar)) {
                ExprListNode exlist = ExprList();
                ProcCallNode res = new ProcCallNode(id, exlist, pos);
                Requires(TokenType.RPar);
                return res;
            }
            else ExeptedError(TokenType.Assign, TokenType.AssignPlus, TokenType.LPar);
        }
        return null;
    }

    public ExprListNode ExprList() throws LexerException, SyntaxException {
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

    public ExprNode Expr() throws LexerException, SyntaxException {
        ExprNode ex = Comp();
        while(At(TokenType.Greater, TokenType.GreaterEqual,
                TokenType.Less, TokenType.LessEqual,
                TokenType.Equal, TokenType.NotEqual)) {
            Token op = NextLexem();
            ExprNode right = Comp();
            ex = new BinOpNode(ex, right, ((String)op.value).charAt(0), ex.pos);
        }
        return ex;
    }

    public ExprNode Comp() throws LexerException, SyntaxException {
        // term (addop term)*
        ExprNode ex = Term();
        while(At(TokenType.Plus, TokenType.Minus, TokenType.tkOr)) {
            Token op = NextLexem();
            ExprNode right = Term();
            ex = new BinOpNode(ex, right, ((String)op.value).charAt(0), ex.pos);
        }
        return ex;
    }

    public ExprNode Term() throws LexerException, SyntaxException {
        // Factor (multop Factor)*
        ExprNode ex = Factor();
        while(At(TokenType.Multiply, TokenType.Divide, TokenType.tkAnd)) {
            Token op = NextLexem();
            ExprNode right = Factor();
            ex = new BinOpNode(ex, right, ((String)op.value).charAt(0), ex.pos);
        }
        return ex;
    }

    public ExprNode Factor() throws LexerException, SyntaxException {
        Position pos = CurrentToken().pos;
        ExprNode res;
        if(At(TokenType.Int)) {
            res = new IntNode((int)NextLexem().value, pos);
        } else if(At(TokenType.DoubleLiteral)) {
            res = new DoubleNode((double)NextLexem().value, pos);
        } else if(IsMatch(TokenType.LPar)) {
            res = Expr();
            Requires(TokenType.RPar);
        } else if(At(TokenType.Id)) {
            IdNode id = Ident();
            if(IsMatch(TokenType.LPar)) {
                ExprListNode exlist = ExprList();
                res = new FuncCallNode(id, exlist, id.pos);
                Requires(TokenType.RPar);
            } else res = id;
        } else throw new SyntaxException("Expected INT or ( or id but "+ PeekToken().typ +" found ", PeekToken().pos);
        return res;
    }
}
