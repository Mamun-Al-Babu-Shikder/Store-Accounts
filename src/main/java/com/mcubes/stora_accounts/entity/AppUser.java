package com.mcubes.stora_accounts.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class AppUser {

    @Id
    @SequenceGenerator(name = "appuser_sequence", sequenceName = "user_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appuser_sequence")
    private int id;
    private String name;
    private String email;
    private String password;
    private String storeName;

}
