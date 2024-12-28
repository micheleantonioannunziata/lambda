package com.lambda.demo.Entity.GC.Inserzione;

import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class InserzioneEntityId implements Serializable {

    @Column(columnDefinition = "CHAR(11)")
    private String partitaIvaRivenditore;

    private ProdottoEntityId idProdotto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InserzioneEntityId that = (InserzioneEntityId) o;
        return getPartitaIvaRivenditore().equals(that.getPartitaIvaRivenditore()) && getIdProdotto().equals(that.getIdProdotto());
    }

    @Override
    public int hashCode() {
        int result = getPartitaIvaRivenditore().hashCode();
        result = 31 * result + getIdProdotto().hashCode();
        return result;
    }
}
