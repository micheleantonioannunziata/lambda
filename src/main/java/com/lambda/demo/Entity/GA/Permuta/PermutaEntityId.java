package com.lambda.demo.Entity.GA.Permuta;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PermutaEntityId implements Serializable {

    private int superProdottoId;
    private int acquirenteId;

    @Column(columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime dataOra;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PermutaEntityId that = (PermutaEntityId) o;
        return superProdottoId == that.superProdottoId && acquirenteId == that.acquirenteId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(superProdottoId, acquirenteId);
    }
}
