package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

public class WhileNode extends StatementNode {
    public ExprNode Condition;
    public StatementNode Stat;

    public WhileNode(ExprNode Condition, StatementNode Stat, Position pos) {
        this.Condition = Condition;
        this.Stat = Stat;
        this.pos = pos;
    }

    @Override
    public void Execute() {
        while ((double)Condition.Eval() > 0)
            Stat.Execute();
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitWhile(this);
    }
    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitWhile(this);
    }
}
