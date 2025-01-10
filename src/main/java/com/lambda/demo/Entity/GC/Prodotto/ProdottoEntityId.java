package com.lambda.demo.Entity.GC.Prodotto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Check;

import java.io.Serializable;


@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class ProdottoEntityId implements Serializable {

    private int superProdottoId;
    @JsonProperty("ram")
    @Check(constraints = "ram IN (1, 2, 4, 6, 8, 12, 16, 24, 32, 64, 128)")
    private int ram;

    @JsonProperty("spazio_archiviazione")
    @Check(constraints = "spazio_archiviazione IN (4, 6, 8, 12, 16, 24, 32, 64, 128, 256, 512, 1024, 2048)")
    @Column(name = "spazio_archiviazione")
    private int spazioArchiviazione;

    @JsonProperty("colore")
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
