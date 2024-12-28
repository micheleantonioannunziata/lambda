package com.lambda.demo.Entity.GP.Wishlist.FormazioneWihslist;


import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GP.Wishlist.WishlistEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "formazione_wishlist")
public class FormazioneWishlistEntity {

    @EmbeddedId
    private FormazioneWishlistEntityId id;

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

    @MapsId("idWishlist")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "id_wishlist",
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (id_wishlist) REFERENCES wishlist(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private WishlistEntity wishlist;


    public void setWishlist(WishlistEntity wishlist) {
        if (this.wishlist != wishlist && wishlist != null) {
            this.wishlist = wishlist;
            if (!wishlist.getFormazioniWishlist().contains(this)) wishlist.getFormazioniWishlist().add(this);
        }
    }


    public void setInserzione(InserzioneEntity inserzione) {
        if (this.inserzione != inserzione && inserzione != null) {
            this.inserzione = inserzione;
            if (!inserzione.getFormazioneWishlist().contains(this)) inserzione.getFormazioneWishlist().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FormazioneWishlistEntity that = (FormazioneWishlistEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}