package com.lambda.demo.Entity.GA.Permuta;


import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "permuta")
public class PermutaEntity {

    @EmbeddedId
    private PermutaEntityId id;





    @MapsId("superProdottoId")
    @ManyToOne
    @JoinColumn(name = "super_prodotto_id",
            foreignKey = @ForeignKey(
            foreignKeyDefinition =  "FOREIGN KEY (super_prodotto_id) REFERENCES super_prodotto(id) ON UPDATE CASCADE ON DELETE CASCADE"
    ))
    private SuperProdottoEntity superProdotto;



    @MapsId("acquirenteId")
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (acquirente_id) REFERENCES acquirente(id) ON UPDATE CASCADE ON DELETE CASCADE"
    ))
    private AcquirenteEntity acquirente;


    @Check(constraints = "stato_batteria > 0 AND stato_batteria <= 100")
    @Column(nullable = false)
    private int statoBatteria;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String condizioneGenerale;

    @Check(constraints = "ram IN (2, 4, 6, 8, 12, 16, 24, 32, 64, 128)")
    @Column(nullable = false)
    private int ram;

    @Check(constraints = "spazio_archiviazione IN (4, 6, 8, 12, 16, 24, 32, 64, 128, 256, 512, 1024, 2048)")
    @Column(nullable = false)
    private int spazioArchiviazione;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String colore;

    @Column(nullable = false, columnDefinition = "INT UNSIGNED")
    private int lambdaPoints;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermutaEntity that = (PermutaEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
