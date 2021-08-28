package com.mcubes.stora_accounts.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Product {

    @Id
    @SequenceGenerator(name = "product_sequence", sequenceName = "product_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_sequence")
    private long id;
    private int userId;
    @ManyToOne(targetEntity = Category.class, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category", referencedColumnName = "id")
    @NotNull(message = "Category not selected")
    private Category category;
    @NotBlank(message = "Product name can't blank")
    private String name;
    @NotNull(message = "Price can't be empty")
    @Range(min = 0, max = 10000000, message = "You have entered invalid price")
    private Float price;
    @NotNull( message = "Stock can't be empty")
    @Range(min = 0, max = 100, message = "You have entered invalid stock")
    private Integer stock;
    private Date lastPurchased;
    private Date lastSold;

}
