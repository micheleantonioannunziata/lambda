package com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello;

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
public class FormazioneCarrelloEntityId implements Serializable {
    private int idCarrello;
    private InserzioneEntityId idInserzione;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        FormazioneCarrelloEntityId that = (FormazioneCarrelloEntityId) o;
        return getIdCarrello() == that.getIdCarrello() && getIdInserzione().equals(that.getIdInserzione());
    }

    @Override
    public int hashCode() {
        int result = getIdCarrello();
        result = 31 * result + getIdInserzione().hashCode();
        return result;
    }
}
