package compile.Parser.ASTNodes.Nodes;

import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

import java.util.ArrayList;
import java.util.List;

public class StatementListNode extends StatementNode{
    public List<StatementNode> lst = new ArrayList<>();
    public void Add(StatementNode st) {
        lst.add(st);
    }

    @Override
    public void Execute() {
        for(StatementNode st : lst)
            st.Execute();
    }
    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitStatementList(this);
    }
    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitStatementList(this);
    }
}
