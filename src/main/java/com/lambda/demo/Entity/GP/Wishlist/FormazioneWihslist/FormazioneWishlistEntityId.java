package com.lambda.demo.Entity.GP.Wishlist.FormazioneWihslist;


import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class FormazioneWishlistEntityId implements Serializable {
    private int idWishlist;
    private InserzioneEntityId idInserzione;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        FormazioneWishlistEntityId that = (FormazioneWishlistEntityId) o;
        return getIdWishlist() == that.getIdWishlist() && getIdInserzione().equals(that.getIdInserzione());
    }

    @Override
    public int hashCode() {
        int result = getIdWishlist();
        result = 31 * result + getIdInserzione().hashCode();
        return result;
    }
}
