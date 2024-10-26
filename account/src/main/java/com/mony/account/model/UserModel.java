package com.mony.account.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ou GenerationType.AUTO, dependendo do banco
    private Long Id;
    @NotNull
    private String nome;
    @Column(length = 11)
    private String cpf;
    @NotNull
    private String email;
    @NotNull
    private String password;

}
