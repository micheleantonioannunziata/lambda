package com.lambda.demo.Entity.GA.Ordine.Composizione;

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
public class ComposizioneEntityId implements Serializable {
    private int idOrdine;
    private InserzioneEntityId idInserzione;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComposizioneEntityId that = (ComposizioneEntityId) o;
        return getIdOrdine() == that.getIdOrdine() && getIdInserzione().equals(that.getIdInserzione());
    }

    @Override
    public int hashCode() {
        int result = getIdOrdine();
        result = 31 * result + getIdInserzione().hashCode();
        return result;
    }
}
