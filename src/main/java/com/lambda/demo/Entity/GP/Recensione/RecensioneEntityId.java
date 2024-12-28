package com.lambda.demo.Entity.GP.Recensione;

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
public class RecensioneEntityId implements Serializable {

    private int idAcquirente;
    private InserzioneEntityId idInserzione;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        RecensioneEntityId that = (RecensioneEntityId) o;
        return getIdAcquirente() == that.getIdAcquirente() && getIdInserzione().equals(that.getIdInserzione());
    }

    @Override
    public int hashCode() {
        int result = getIdAcquirente();
        result = 31 * result + getIdInserzione().hashCode();
        return result;
    }
}
