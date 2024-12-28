package com.lambda.demo.Entity.GP.Recensione;


import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recensione")
public class RecensioneEntity {


    @EmbeddedId
    private RecensioneEntityId id;

    @MapsId("idAcquirente")
    @ManyToOne
    @JoinColumn(
            name = "acquirente_id",
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (acquirente_id) REFERENCES acquirente(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private AcquirenteEntity acquirente;

    @MapsId("idInserzione")
    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "ram", referencedColumnName = "ram"),
            @JoinColumn(name = "spazio_archiviazione", referencedColumnName = "spazio_archiviazione"),
            @JoinColumn(name = "super_prodotto_id", referencedColumnName = "super_prodotto_id"),
            @JoinColumn(name = "colore", referencedColumnName = "colore"),
            @JoinColumn(name = "partita_iva_rivenditore", referencedColumnName = "partita_iva_rivenditore")
    }, foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (ram, spazio_di_archiviazione, super_prodotto_id, colore, partita_iva_rivenditore) REFERENCES inserzione (ram, spazio_di_archiviazione, super_prodotto_id, colore, partita_iva_rivenditore) ON UPDATE CASCADE ON DELETE CASCADE"
    ))
    private InserzioneEntity inserzione;

    @Check(constraints = "stelle > 0 AND stelle <= 5")
    @Column(nullable = false, columnDefinition = "INT")
    private int stelle;

    @Column(nullable = false)
    private String descrizione;


    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "DATETIME")
    private Date dataOra;

    public void setAcquirente(AcquirenteEntity acquirente) {
        if(this.acquirente != acquirente && acquirente != null) {
            this.acquirente = acquirente;
            if (!acquirente.getRecensioni().contains(this)) acquirente.getRecensioni().add(this);
        }
    }

    public void setInserzione_recensione(InserzioneEntity inserzione) {
        if(this.inserzione != inserzione && inserzione != null) {
            this.inserzione = inserzione;
            if(!inserzione.getRecensioni().contains(this)) inserzione.getRecensioni().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecensioneEntity that = (RecensioneEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
