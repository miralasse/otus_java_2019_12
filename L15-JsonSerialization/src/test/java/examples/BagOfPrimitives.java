package examples;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * BagOfPrimitives.
 *
 * @author Evgeniya_Yanchenko
 */
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class BagOfPrimitives {

    private int intValue;
    private String stringValue;
    private long longValue;


    public BagOfPrimitives(BagOfPrimitives bagOfPrimitives) {
        this.intValue = bagOfPrimitives.intValue;
        this.stringValue = bagOfPrimitives.stringValue;
        this.longValue = bagOfPrimitives.longValue;
    }
}
