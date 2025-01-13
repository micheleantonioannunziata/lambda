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

    @OneToMany(mappedBy = "categoria", fetch = FetchType.EAGER)
    List<SuperProdottoEntity> superProdotti = new ArrayList<>();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;
    @Column(unique = true, columnDefinition = "VARCHAR(50)")
    private String nome;
    @Column(nullable = false)
    private String immagine;

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
