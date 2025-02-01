package compile.Parser.ASTNodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

public class IfNode extends StatementNode {
    public ExprNode Condition;
    public StatementNode ThenStat, ElseStat;
    public IfNode(ExprNode Condition, StatementNode ThenStat, StatementNode ElseStat, Position pos) {
        this.Condition = Condition;
        this.ThenStat = ThenStat;
        this.ElseStat = ElseStat;
        this.pos = pos;
    }

    @Override
    public void Execute() {
        if(Condition.Eval() > 0) {
            ThenStat.Execute();
        } else if (ElseStat != null){
            ElseStat.Execute();
        }
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitIf(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitIf(this);
    }
}
