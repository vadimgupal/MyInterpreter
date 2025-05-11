package compile.Types;

import java.util.List;

public class SymbolInfo {
    public String Name;
    public KindType Kind;
    public SemanticType Typ;
    public SemanticType ElementTyp;
    public List<SemanticType> Params;

    public SymbolInfo(String name, KindType kind, SemanticType type) {
        this.Name = name;
        this.Kind = kind;
        this.Typ = type;
        ElementTyp = null;
        this.Params = null;
    }

    public SymbolInfo(String name, KindType kind, List<SemanticType> params, SemanticType type) {
        this.Name = name;
        this.Kind = kind;
        ElementTyp = null;
        this.Params = params;
        this.Typ = type;
    }
}
