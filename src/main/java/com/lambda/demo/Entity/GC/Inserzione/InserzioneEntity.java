package com.lambda.demo.Entity.GC.Inserzione;

import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GA.Ordine.Composizione.ComposizioneEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GP.Recensione.RecensioneEntity;
import com.lambda.demo.Entity.GP.Wishlist.FormazioneWihslist.FormazioneWishlistEntity;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="inserzione")
@Check(constraints = "prezzo_base > 0 AND sconto_standard >= 0 AND sconto_premium >= 0 AND sconto_premium > sconto_standard AND quantita > 0")
public class InserzioneEntity {

    @EmbeddedId
    private InserzioneEntityId id;

    @MapsId("partitaIvaRivenditore")
    @ManyToOne
    @JoinColumn(
            name = "partita_iva_rivenditore",
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (partita_iva_rivenditore) REFERENCES rivenditore(partita_iva)"
            )
    )
    private RivenditoreEntity rivenditore;

    @MapsId("idProdotto")
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


    @Column(nullable = false, columnDefinition = "DECIMAL (10, 2)")
    private float prezzoBase;

    @Column(nullable = false)
    private int scontoStandard;

    @Column(nullable = false)
    private int scontoPremium;

    @Column(nullable = false)
    private int quantita;


    @Column(nullable = false)
    @CreationTimestamp
    private Date dataPubblicazione;


    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT 1")
    private boolean disponibilita;


    @OneToMany(mappedBy = "inserzione")
    private List<RecensioneEntity> recensioni = new ArrayList<>();

    @OneToMany(mappedBy = "inserzione")
    private List<FormazioneCarrelloEntity> formazioneCarrello = new ArrayList<>();

    @OneToMany(mappedBy = "inserzione")
    private List<FormazioneWishlistEntity> formazioneWishlist = new ArrayList<>();

    @OneToMany(mappedBy = "inserzione")
    private List<ComposizioneEntity> composizioni = new ArrayList<>();



    public void setRivenditore(RivenditoreEntity rivenditore) {
        if (this.rivenditore != rivenditore && rivenditore != null) {
            this.rivenditore = rivenditore;
            if (!rivenditore.getInserzioni().contains(this)) rivenditore.getInserzioni().add(this);
        }
    }

    public void setProdotto(ProdottoEntity prodotto) {
        if (this.prodotto != prodotto && prodotto != null) {
            this.prodotto = prodotto;
            if (!prodotto.getInserzioni().contains(this)) prodotto.getInserzioni().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InserzioneEntity that = (InserzioneEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
