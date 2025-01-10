package com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "formazione_carrello")
@Check(constraints = "quantita > 0")
public class FormazioneCarrelloEntity {

    @EmbeddedId
    private FormazioneCarrelloEntityId id;

    @MapsId("idCarrello")
    @ManyToOne
    @JoinColumn(
            name = "carrello_id",
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (carrello_id) REFERENCES carrello(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private CarrelloEntity carrello;

    @MapsId("idInserzione")
    @ManyToOne
    @JoinColumns(value = {
            @JoinColumn(name = "ram", referencedColumnName = "ram"),
            @JoinColumn(name = "spazio_archiviazione", referencedColumnName = "spazio_archiviazione"),
            @JoinColumn(name = "super_prodotto_id", referencedColumnName = "super_prodotto_id"),
            @JoinColumn(name = "colore", referencedColumnName = "colore", columnDefinition = "VARCHAR(50)"),
            @JoinColumn(name = "partita_iva_rivenditore", referencedColumnName = "partita_iva_rivenditore", columnDefinition = "CHAR(11)")
    }, foreignKey = @ForeignKey(
            foreignKeyDefinition = "FOREIGN KEY (ram, spazio_archiviazione, super_prodotto_id, colore, partita_iva_rivenditore) REFERENCES inserzione (ram, spazio_archiviazione, super_prodotto_id, colore, partita_iva_rivenditore) ON UPDATE CASCADE ON DELETE CASCADE"
    ))
    private InserzioneEntity inserzione;

    @Column(nullable = false)
    private int quantita;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        FormazioneCarrelloEntity that = (FormazioneCarrelloEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }

    public void setCarrello(CarrelloEntity carrelloEntity){
        List<FormazioneCarrelloEntity> cartItems = carrelloEntity.getCarrelloItems();

        if(!cartItems.contains(this))
            cartItems.add(this);
    }
}
