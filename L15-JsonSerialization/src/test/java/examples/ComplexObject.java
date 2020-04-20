package examples;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.Objects;

/**
 * ComplexObject.
 *
 * @author Evgeniya_Yanchenko
 */
@Data
@AllArgsConstructor
public class ComplexObject {

    private static String staticString = "static string value";

    private transient Integer transientInt;
    private String stringValue;
    private BagOfPrimitives bagOfPrimitives;
    private Map<Long, String> map;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexObject that = (ComplexObject) o;
        return Objects.equals(transientInt, that.transientInt) &&
                Objects.equals(stringValue, that.stringValue) &&
                Objects.equals(bagOfPrimitives, that.bagOfPrimitives) &&
                Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transientInt, stringValue, bagOfPrimitives, map);
    }
}
