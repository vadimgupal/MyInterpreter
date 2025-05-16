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

    public static final List<OpType> StringOperations = List.of(OpType.strPlus);

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

    public static boolean AssignComparable(SemanticType L, SemanticType R) {
        if (L.equals(R)) return true;
        if (L == SemanticType.DoubleType && R == SemanticType.IntType) return true;
        if (L == SemanticType.ObjectType
                && R != SemanticType.NoType && R != SemanticType.BadType) return true;
        // если оба массивы — рекурсивно сравним элементные типы
        if (L.isArray() && R.isArray()) {
            return AssignComparable(L.getElementType(), R.getElementType());
        }
        return false;
    }

    public static SemanticType CalcType(ExprNode ex) throws SemanticException {
        if (ex instanceof BinOpNode bin) {
            SemanticType lt = CalcType(bin.left);
            SemanticType rt = CalcType(bin.right);

            if(lt.equals(SemanticType.StringType) && rt.equals(SemanticType.StringType)) {
                if (bin.op.equals(OpType.opPlus)) {
                    return SemanticType.StringType;
                } else
                    return SemanticType.BadType;
            } else if (ArithmeticOperations.contains(bin.op)) {
                if (!NumTypes.contains(lt) || !NumTypes.contains(rt)) {
                    //new SemanticException("Операция %s не определена для типов %s и %s".formatted(OpToStr.get(bin.op), lt, rt), bin.left.pos);
                    return SemanticType.BadType;
                } else if (bin.op == OpType.opDivide) {
                    return SemanticType.DoubleType;
                } else if (lt == rt) {
                    return lt;
                } else {
                    return SemanticType.DoubleType;
                }
            } else if (LogicalOperations.contains(bin.op)) {
                if (lt != SemanticType.BoolType || rt != SemanticType.BoolType) {
                    //throw new SemanticException("Операция %s не определена для типов %s и %s".formatted(OpToStr.get(bin.op), lt, rt), bin.left.pos);
                    return SemanticType.BadType;
                } else {
                    return SemanticType.BoolType;
                }
            } else if (CompareOperations.contains(bin.op)) {
                if (!NumTypes.contains(lt) || !NumTypes.contains(rt)) {
                    //throw new SemanticException("Операция " + OpToStr.get(bin.op) + " не определена для типов " + lt + " и " + rt, bin.left.pos);
                    return SemanticType.BadType;
                } else {
                    return SemanticType.BoolType;
                }
            }
//            else if (StringOperations.contains(bin.op)) {
//                if(lt != SemanticType.StringType || rt != SemanticType.StringType) {
//                    throw new SemanticException("Операция " + OpToStr.get(bin.op) + " не определена для типов " + lt + " и " + rt, bin.left.pos);
//                } else {
//                    return SemanticType.StringType;
//                }
//            }
        } else if (ex instanceof IdNode id) {
            if (!SymTable.containsKey(id.Name)) {
                //throw new SemanticException("Идентификатор " + id.Name + " не определен", id.pos);
                return SemanticType.BadType;
            } else if (SymTable.get(id.Name).Kind != KindType.VarName) {
                //throw new SemanticException(id.Name + " не является переменной", id.pos);
                return SemanticType.BadType;
            } else {
                return SymTable.get(id.Name).Typ;
            }
        } else if (ex instanceof IntNode) {
            return SemanticType.IntType;
        } else if (ex instanceof DoubleNode) {
            return SemanticType.DoubleType;
        } else if (ex instanceof StringLiteral) {
            return SemanticType.StringType;
        } else if(ex instanceof ArrayLiteral arr) {
            // Проверяем, что все элементы массива имеют совместимые типы
            if (arr.value.lst.isEmpty()) {
                return SemanticType.arrayOf(SemanticType.ObjectType);
            }

            SemanticType firstType = CalcType(arr.value.lst.getFirst());
            for (ExprNode elem : arr.value.lst) {
                SemanticType elemType = CalcType(elem);
                if (!AssignComparable(firstType, elemType)) {
                    return SemanticType.BadType;
                }
            }
            return SemanticType.arrayOf(firstType);
        } else if(ex instanceof ArrayIndexNode arrayIndexNode) {
            SemanticType container  = CalcType((ExprNode) arrayIndexNode.arrayName);
            if(!container.isArray()) {
                return SemanticType.BadType;
            }

            SemanticType indexType = CalcType(arrayIndexNode.index);
            if(indexType != SemanticType.IntType) {
                return SemanticType.BadType;
            }

            return container.getElementType();
        } else if (ex instanceof FuncCallNode f) {
            if (!SymTable.containsKey(f.Name.Name)) {
                //throw new SemanticException("Функция с именем " + f.Name.Name + " не определена", f.Name.pos);
                return SemanticType.BadType;
            }
            SymbolInfo sym = SymTable.get(f.Name.Name);
            if (sym.Kind != KindType.FuncName) {
                //throw new SemanticException("Данное имя " + f.Name.Name + " не является именем функции", f.Name.pos);
                return SemanticType.BadType;
            }
            if (sym.Params.size() != f.Pars.lst.size()) {
                //throw new SemanticException("Несоответствие количества параметров при вызове функции " + f.Name.Name, f.Name.pos);
                return SemanticType.BadType;
            }
            for (int i = 0; i < sym.Params.size(); i++) {
                SemanticType tp = CalcType(f.Pars.lst.get(i));
                if (!AssignComparable(sym.Params.get(i), tp)) {
                    //throw new SemanticException("Тип аргумента функции " + tp + " не соответствует типу формального параметра " + sym.Params.get(i), f.Name.pos);
                    return SemanticType.BadType;
                }
            }
            if (sym.Typ == SemanticType.NoType) { // Это процедура
                //throw new SemanticException("Попытка вызвать процедуру " + f.Name.Name + " как функцию", f.Name.pos);
                return SemanticType.BadType;
            }
            return sym.Typ;
        }

        return SemanticType.BadType;
    }
}
