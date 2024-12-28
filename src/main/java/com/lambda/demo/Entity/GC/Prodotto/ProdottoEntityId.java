package com.lambda.demo.Entity.GC.Prodotto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import lombok.*;
import org.hibernate.annotations.Check;


@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ProdottoEntityId implements Serializable {

    private int superProdottoId;
    @Check(constraints = "ram IN (1, 2, 4, 6, 8, 10, 12, 16, 24, 32, 64, 128)")
    private int ram;

    @Check(constraints = "spazio_archiviazione IN (4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048)")
    @Column(name = "spazio_archiviazione")
    private int spazioArchiviazione;
    private String colore;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        ProdottoEntityId that = (ProdottoEntityId) o;
        return getSuperProdottoId() == that.getSuperProdottoId() && getRam() == that.getRam() && getSpazioArchiviazione() == that.getSpazioArchiviazione() && getColore().equals(that.getColore());
    }

    @Override
    public int hashCode() {
        int result = getSuperProdottoId();
        result = 31 * result + getRam();
        result = 31 * result + getSpazioArchiviazione();
        result = 31 * result + getColore().hashCode();
        return result;
    }
}
