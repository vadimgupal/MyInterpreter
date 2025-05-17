package compile.Parser.ASTNodes.Nodes;

import compile.Lexer.Position;
import compile.Parser.ASTNodes.Dictionary;
import compile.Parser.ASTNodes.Visitors.IVisitor;
import compile.Parser.ASTNodes.Visitors.IVisitorP;
import compile.SemanticException;

import java.util.ArrayList;
import java.util.List;

public class AssignPlusNode extends StatementNode {
    public LValueNode LValue;
    public ExprNode Expr;

    public AssignPlusNode(LValueNode lvalue, ExprNode Expr, Position pos) {
        this.LValue = lvalue;
        this.Expr = Expr;
        this.pos = pos;
    }

    @Override
    public void Execute() {
        // 1) Собираем все индексы и вычленяем «корневое» имя переменной
        LValueNode node = LValue;
        // будем хранить индексы в порядке от корня к листу
        List<ExprNode> indices = new ArrayList<>();
        while (node instanceof ArrayIndexNode ai) {
            // собираем индекс в начало списка
            indices.add(0, ai.index);
            // идём вверх по дереву
            node = ai.arrayName;
        }
        // в node теперь обязательно IdNode
        String varName = ((IdNode) node).Name;

        // 2) Получаем контейнер: либо сам объект, либо вложенный список
        Object container = Dictionary.VarValues.get(varName);
        // если нет ни одного индекса, container — это текущее значение переменной
        // если есть индексы, то контейнер — это список, в котором мы будем менять элемент
        for (int i = 0; i < indices.size() - 1; i++) {
            int idx = (Integer) indices.get(i).Eval();
            // для каждого уровня получаем следующий список
            container = ((List<Object>) container).get(idx);
        }

        // 3) Вычисляем текущее значение и новое
        Number currentNumber;
        if (indices.isEmpty()) {
            // простая переменная
            currentNumber = (Number) container;
        } else {
            // берем последний индекс
            int lastIdx = (Integer) indices.get(indices.size() - 1).Eval();
            currentNumber = (Number) ((List<Object>) container).get(lastIdx);
        }
        // 4) Считываем RHS
        Object rhsVal = Expr.Eval();
        if (!(rhsVal instanceof Number)) {
            throw new RuntimeException("Операция +=: правый операнд не число");
        }
        Number rightNumber = (Number) rhsVal;

        // 5) Вычисляем результат, сохраняя Integer, если оба были Integer
        Number result;
        if (currentNumber instanceof Integer && rightNumber instanceof Integer) {
            result = currentNumber.intValue() + rightNumber.intValue();
        } else {
            result = currentNumber.doubleValue() + rightNumber.doubleValue();
        }
        // 4) Записываем обратно
        if (indices.isEmpty()) {
            // простая переменная
            Dictionary.VarValues.put(varName, result);
        } else {
            int lastIdx = (Integer) indices.getLast().Eval();
            ((List<Object>) container).set(lastIdx, result);
        }
    }


    @Override
    public <T> T Visit(IVisitor<T> v){
        return v.VisitAssignPlus(this);
    }
    @Override
    public void VisitP(IVisitorP v) throws SemanticException {
        v.VisitAssignPlus(this);
    }
}