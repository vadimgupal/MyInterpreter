package compile.Parser.ASTNodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitor.IVisitor;
import compile.Parser.ASTNodes.Visitor.IVisitorP;

public class ProcCallNode extends StatementNode {
    public IdNode Name;
    public ExprListNode Pars;

    public ProcCallNode(IdNode Name, ExprListNode Pars, Position pos) {
        this.Name = Name;
        this.Pars = Pars;
        this.pos = pos;
    }

    @Override
    public void Execute() {
        if(Name.Name.toLowerCase().equals("print"))
            System.out.println(Pars.lst.get(0).Eval());
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitProcCall(this);
    }
    @Override
    public void VisitP(IVisitorP v){
        v.VisitProcCall(this);
    }
}
