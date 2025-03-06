package compile.Types;

import java.util.List;

public class SymbolInfo {
    public String Name;
    public KindType Kind;
    public SemanticType Typ;
    public List<SemanticType> Params;

    public SymbolInfo(String name, KindType kind, SemanticType type) {
        this.Name = name;
        this.Kind = kind;
        this.Typ = type;
        this.Params = null;
    }

    public SymbolInfo(String name, KindType kind, List<SemanticType> params, SemanticType type) {
        this.Name = name;
        this.Kind = kind;
        this.Params = params;
        this.Typ = type;
    }
}
