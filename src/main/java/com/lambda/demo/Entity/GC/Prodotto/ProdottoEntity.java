package com.lambda.demo.Entity.GC.Prodotto;


import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "prodotto")
public class ProdottoEntity {

    @EmbeddedId
    private ProdottoEntityId id;

    @MapsId("superProdottoId")
    @ManyToOne
    @JoinColumn(
            foreignKey = @ForeignKey(
                    name = "super_prodotto_id",
                    foreignKeyDefinition = "FOREIGN KEY (super_prodotto_id) REFERENCES super_prodotto(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private SuperProdottoEntity superProdotto;

    @OneToMany(mappedBy = "prodotto", fetch = FetchType.EAGER)
    private List<InserzioneEntity> inserzioni = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProdottoEntity that = (ProdottoEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
