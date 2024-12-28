package com.lambda.demo.Entity.GC;


import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "superProdotto")
public class SuperProdottoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String marca;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String modello;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String cpu;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String gpu;

    @Check(constraints = "connettivita IN ('3g', '4g', '5g')")
    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String connettivita;

    @Column(nullable = false, columnDefinition = "DECIMAL (10, 2)")
    private float pollici;

    @Column(nullable = false)
    private String immagine;


    @ManyToOne
    @JoinColumn(
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private CategoriaEntity categoria;


    public void setCategoria(CategoriaEntity categoria) {
        if(this.categoria != categoria && categoria != null) {
            this.categoria = categoria;
            if(!categoria.getSuperProdotti().contains(this)) categoria.getSuperProdotti().add(this);
        }
    }


    @OneToMany(mappedBy = "superProdotto")
    private List<ProdottoEntity> prodotti = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuperProdottoEntity that = (SuperProdottoEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
