package com.lambda.demo.Entity.GC;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categoria")
public class CategoriaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;


    @Column(columnDefinition = "VARCHAR(50)")
    private String nome;

    @OneToMany(mappedBy = "categoria")
    List<SuperProdottoEntity> superProdotti = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoriaEntity categoria = (CategoriaEntity) o;
        return getId() == categoria.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
