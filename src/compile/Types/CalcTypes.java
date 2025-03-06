package compile.Types;

import compile.Parser.ASTNodes.Nodes.*;
import compile.SemanticException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static compile.Types.SymbolTable.NumTypes;
import static compile.Types.SymbolTable.SymTable;

public class CalcTypes {
    public static final List<OpType> ArithmeticOperations = List.of(
            OpType.opPlus,
            OpType.opMinus,
            OpType.opMultiply,
            OpType.opDivide
    );
    public static final List<OpType> CompareOperations = List.of(
            OpType.opEqual,
            OpType.opLess,
            OpType.opLessEqual,
            OpType.opGreater,
            OpType.opGreaterEqual,
            OpType.opNotEqual
    );

    public static final List<OpType> LogicalOperations = List.of(
            OpType.opAnd,
            OpType.opOr,
            OpType.opNot
    );

    public static final Map<OpType,String> OpToStr = new HashMap<>(){{
        put(OpType.opPlus, "+");
        put(OpType.opMinus, "-");
        put(OpType.opMultiply, "*");
        put(OpType.opDivide, "/");
        put(OpType.opEqual, "==");
        put(OpType.opLess, "<");
        put(OpType.opLessEqual, "<=");
        put(OpType.opGreater, ">");
        put(OpType.opGreaterEqual, ">=");
        put(OpType.opNotEqual, "!=");
        put(OpType.opAnd, "&&");
        put(OpType.opOr, "||");
        put(OpType.opNot, "!");
    }};

    public static boolean AssignComparable(SemanticType leftvar, SemanticType rightexpr) {
        if(leftvar.equals(rightexpr))
            return true;
        else if(leftvar.equals(SemanticType.DoubleType) && rightexpr.equals(SemanticType.IntType))
            return true;
        else if(leftvar.equals(SemanticType.ObjectType) &&
                !(rightexpr.equals(SemanticType.NoType) || rightexpr.equals(SemanticType.BadType)))
            return true;
        else
            return false;
    }

    public static SemanticType CalcType(ExprNode ex){
        if (ex instanceof BinOpNode) {
            BinOpNode bin = (BinOpNode) ex;
            SemanticType lt = CalcType(bin.left);
            SemanticType rt = CalcType(bin.right);

            if (ArithmeticOperations.contains(bin.op)) {
                if (!NumTypes.contains(lt) || !NumTypes.contains(rt)) {
                    new SemanticException("Операция " + OpToStr.get(bin.op) + " не определена для типов " + lt + " и " + rt,
                            bin.left.pos);
                    //return SemanticType.NoType;
                } else if (bin.op == OpType.opDivide) {
                    return SemanticType.DoubleType;
                } else if (lt == rt) {
                    return lt;
                } else {
                    return SemanticType.DoubleType;
                }
            } else if (LogicalOperations.contains(bin.op)) {
                if (lt != SemanticType.BoolType || rt != SemanticType.BoolType) {
                    new SemanticException("Операция " + OpToStr.get(bin.op) + " не определена для типов " + lt + " и " + rt, bin.left.pos);
                    //return SemanticType.NoType;
                } else {
                    return SemanticType.BoolType;
                }
            } else if (CompareOperations.contains(bin.op)) {
                if (!NumTypes.contains(lt) || !NumTypes.contains(rt)) {
                    new SemanticException("Операция " + OpToStr.get(bin.op) + " не определена для типов " + lt + " и " + rt, bin.left.pos);
                    //return SemanticType.NoType;
                } else {
                    return SemanticType.BoolType;
                }
            }
        } else if (ex instanceof IdNode) {
            IdNode id = (IdNode) ex;
            if (!SymTable.containsKey(id.Name)) {
                new SemanticException("Идентификатор " + id.Name + " не определен", id.pos);
                //return SemanticType.NoType;
            } else if (SymTable.get(id.Name).Kind != KindType.VarName) {
                new SemanticException("" + id.Name + " не является переменной", id.pos);
                //return SemanticType.NoType;
            } else {
                return SymTable.get(id.Name).Typ;
            }
        } else if (ex instanceof IntNode) {
            return SemanticType.IntType;
        } else if (ex instanceof DoubleNode) {
            return SemanticType.DoubleType;
        } else if (ex instanceof FuncCallNode) {
            FuncCallNode f = (FuncCallNode) ex;
            if (!SymTable.containsKey(f.Name.Name)) {
                new SemanticException("Функция с именем " + f.Name.Name + " не определена", f.Name.pos);
                //return SemanticType.NoType;
            }
            SymbolInfo sym = SymTable.get(f.Name.Name);
            if (sym.Kind != KindType.FuncName) {
                new SemanticException("Данное имя " + f.Name.Name + " не является именем функции", f.Name.pos);
                //return SemanticType.NoType;
            }
            if (sym.Params.size() != f.Pars.lst.size()) {
                new SemanticException("Несоответствие количества параметров при вызове функции " + f.Name.Name, f.Name.pos);
                //return SemanticType.NoType;
            }
            for (int i = 0; i < sym.Params.size(); i++) {
                SemanticType tp = CalcType(f.Pars.lst.get(i));
                if (!AssignComparable(sym.Params.get(i), tp)) {
                    new SemanticException("Тип аргумента функции " + tp + " не соответствует типу формального параметра " + sym.Params.get(i), f.Name.pos);
                    //return SemanticType.NoType;
                }
            }
            if (sym.Typ == SemanticType.NoType) { // Это процедура
                new SemanticException("Попытка вызвать процедуру " + f.Name.Name + " как функцию", f.Name.pos);
                //return SemanticType.NoType;
            }
            return sym.Typ; // тип возвращаемого значения
        }

        // Если ни один из случаев не подошел
        return SemanticType.NoType;
    }
}
