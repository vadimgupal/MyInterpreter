package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

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
        if(Name.Name.equalsIgnoreCase("print"))
            System.out.println(Pars.lst.getFirst().Eval());
    }

    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitProcCall(this);
    }
    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitProcCall(this);
    }
}
