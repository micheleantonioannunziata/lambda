package com.lambda.demo.Entity.GA.Permuta;

import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PermutaEntityId implements Serializable {

    private ProdottoEntityId prodottoId;
    private int acquirenteId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        PermutaEntityId that = (PermutaEntityId) o;
        return getAcquirenteId() == that.getAcquirenteId() && getProdottoId().equals(that.getProdottoId());
    }

    @Override
    public int hashCode() {
        int result = getProdottoId().hashCode();
        result = 31 * result + getAcquirenteId();
        return result;
    }
}
