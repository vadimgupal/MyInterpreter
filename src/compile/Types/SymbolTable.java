package compile.Types;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SymbolTable {
    public static final List<SemanticType> NumTypes = List.of(SemanticType.IntType, SemanticType.DoubleType);
    public static Map<String, SymbolInfo> SymTable = new HashMap<>(){{
        put("sqrt",new SymbolInfo("sqrt",KindType.FuncName,List.of(SemanticType.DoubleType),SemanticType.DoubleType));
        put("print", new SymbolInfo("print",KindType.FuncName,List.of(SemanticType.ObjectType),SemanticType.NoType));
    }};
}
