package com.lambda.demo.Entity.GC;


import com.lambda.demo.Entity.GA.Permuta.PermutaEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.ArrayList;
import java.util.Comparator;
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

    @Column(columnDefinition = "VARCHAR(50)")
    private String cpu;

    @Column(columnDefinition = "VARCHAR(50)")
    private String gpu;

    @Check(constraints = "connettivita IN ('3g', '4g', '5g')")
    @Column(columnDefinition = "VARCHAR(50)")
    private String connettivita;

    @Column(columnDefinition = "DECIMAL (10, 2)")
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

    @OneToMany(mappedBy = "superProdotto")
    private List<PermutaEntity> permute = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuperProdottoEntity that = (SuperProdottoEntity) o;
        return getId() == that.getId();
    }

    public InserzioneEntity getCheapestInsertion() {
        // Creazione di una nuova istanza di InserzioneEntity come valore predefinito
        InserzioneEntity inserzioneEntity = null;

        // Recupero dei prodotti, con gestione del caso in cui sia null
        List<ProdottoEntity> products = getProdotti();
        if (products == null) products = new ArrayList<>();

        // Raccolta di tutte le inserzioni dai prodotti
        List<InserzioneEntity> inserzioni = new ArrayList<>();
        for (ProdottoEntity p : products) {
            if (p.getInserzioni() != null && !p.getInserzioni().isEmpty()) { // Gestisce il caso in cui getInserzioni() sia null
                inserzioni.addAll(p.getInserzioni());
            }
        }

        // Ordinamento delle inserzioni in base al prezzo
        inserzioni.sort(Comparator.comparingDouble(InserzioneEntity::getPrezzoBase));

        // Controllo che la lista delle inserzioni non sia vuota prima di accedere al primo elemento
        if (!inserzioni.isEmpty()) {
            inserzioneEntity = inserzioni.get(0); // Ottieni la più economica
        }

        return inserzioneEntity;
    }




    @Override
    public int hashCode() {
        return getId();
    }
}
