package otus.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import otus.core.annotation.Id;

import java.math.BigDecimal;

/**
 * Account.
 *
 * @author Evgeniya_Yanchenko
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Account {

    @Id
    private long no;
    private String type;
    private BigDecimal rest;
}
