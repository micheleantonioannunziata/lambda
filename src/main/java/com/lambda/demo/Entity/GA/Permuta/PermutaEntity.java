package com.lambda.demo.Entity.GA.Permuta;



import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
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





    @MapsId("prodottoId")
    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "ram", referencedColumnName = "ram"),
            @JoinColumn(name = "spazio_archiviazione", referencedColumnName = "spazio_archiviazione"),
            @JoinColumn(name = "super_prodotto_id", referencedColumnName = "super_prodotto_id"),
            @JoinColumn(name = "colore", referencedColumnName = "colore"),
    }, foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (ram, spazio_archiviazione, super_prodotto_id, colore) REFERENCES prodotto (ram, spazio_archiviazione, super_prodotto_id, colore) ON UPDATE CASCADE ON DELETE CASCADE"
    ))
    private ProdottoEntity prodotto;



    @MapsId("acquirenteId")
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (acquirente_id) REFERENCES acquirente(id) ON UPDATE CASCADE ON DELETE CASCADE"
    ))
    private AcquirenteEntity acquirente;


    @Check(constraints = "stato_batteria > 0 AND stato_batteria <= 100")
    @Column(nullable = false)
    private int statoBatteria;

    @Column(nullable = false)
    private String foto;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String condizioneGenerale;


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
