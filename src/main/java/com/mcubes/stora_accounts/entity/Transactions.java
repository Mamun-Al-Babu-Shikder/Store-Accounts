package com.mcubes.stora_accounts.entity;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Transactions {

    public enum Type {
        INCOME, EXPENDITURE
    }

    @Id
    @SequenceGenerator(name = "sequence", sequenceName = "transactions_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequence")
    private long id;
    @NotBlank(message = "Description can't blank")
    private String description;
    /* PRODUCT PURPOSE START */
    private long productId;
    @NotNull( message = "Quantity can't be empty")
    @Range(min = 0, max = 100, message = "You have entered invalid quantity")
    private int quantity;
    /* PRODUCT PURPOSE END */
    @NotNull(message = "Amount can't be empty")
    @Range(min = 0, max = 10000000, message = "You have entered invalid amount")
    private Float amount;
    private Date date;
    @Enumerated(EnumType.STRING)
    private Type type;
    private int userId;

}
