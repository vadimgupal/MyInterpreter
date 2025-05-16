package compile.Types;

import java.util.Objects;

/**
 * Параметризованный тип: либо простой (Int, Bool, String и т.п.),
 * либо массив элементного типа elementType!=null.
 */
public final class SemanticType {
    private final String name;
    private final SemanticType elementType;  // null для не-массивов

    private SemanticType(String name, SemanticType elementType) {
        this.name        = name;
        this.elementType = elementType;
    }

    // Базовые простые типы
    public static final SemanticType IntType    = new SemanticType("Int",    null);
    public static final SemanticType DoubleType = new SemanticType("Double", null);
    public static final SemanticType BoolType   = new SemanticType("Bool",   null);
    public static final SemanticType StringType = new SemanticType("String", null);
    public static final SemanticType ObjectType = new SemanticType("Object", null);
    public static final SemanticType NoType     = new SemanticType("NoType", null);
    public static final SemanticType BadType    = new SemanticType("Bad",    null);

    /** factory для массивов из elementType **/
    public static SemanticType arrayOf(SemanticType elemType) {
        return new SemanticType("Array", elemType);
    }

    /** это массив? **/
    public boolean isArray() {
        return elementType != null;
    }

    /** если isArray(), вернуть тип элемента **/
    public SemanticType getElementType() {
        return elementType;
    }

    @Override
    public String toString() {
        return elementType == null
                ? name
                : name + "<" + elementType + ">";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SemanticType)) return false;
        SemanticType t = (SemanticType)o;
        return Objects.equals(name, t.name)
                && Objects.equals(elementType, t.elementType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, elementType);
    }
}
