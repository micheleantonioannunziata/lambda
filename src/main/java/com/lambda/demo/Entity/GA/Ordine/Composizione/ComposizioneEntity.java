package com.lambda.demo.Entity.GA.Ordine.Composizione;

import com.lambda.demo.Entity.GA.Ordine.OrdineEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "composizione")
public class ComposizioneEntity {

    @EmbeddedId
    private ComposizioneEntityId id;

    @MapsId("idOrdine")
    @ManyToOne
    @JoinColumn(
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY(ordine_id) REFERENCES ordine(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private OrdineEntity ordine;


    @MapsId("idInserzione")
    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "ram", referencedColumnName = "ram"),
            @JoinColumn(name = "spazio_archiviazione", referencedColumnName = "spazio_archiviazione"),
            @JoinColumn(name = "super_prodotto_id", referencedColumnName = "super_prodotto_id"),
            @JoinColumn(name = "colore", referencedColumnName = "colore"),
            @JoinColumn(name = "partita_iva_rivenditore", referencedColumnName = "partita_iva_rivenditore")
    }, foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (ram, spazio_archiviazione, super_prodotto_id, colore, partita_iva_rivenditore) REFERENCES inserzione (ram, spazio_archiviazione, super_prodotto_id, colore, partita_iva_rivenditore) ON UPDATE CASCADE ON DELETE CASCADE"
    ))
    private InserzioneEntity inserzione;

    @Check(constraints = "prezzo_acquisto > 0")
    @Column(nullable = false, columnDefinition = "DECIMAL(10, 2)")
    private double prezzoAcquisto;

    @Check(constraints = "quantita > 0")
    @Column(nullable = false)
    private int quantita;


    public void setInserzione_composizione(InserzioneEntity inserzione) {
        if(this.inserzione != inserzione && inserzione != null){
            this.inserzione = inserzione;
            if(!inserzione.getComposizioni().contains(this)) inserzione.getComposizioni().add(this);
        }

    }

    public void setOrdine(OrdineEntity ordine) {
        if(this.ordine != ordine && ordine != null){
            this.ordine = ordine;
            if(!ordine.getComposizioni().contains(this)) ordine.getComposizioni().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComposizioneEntity that = (ComposizioneEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
