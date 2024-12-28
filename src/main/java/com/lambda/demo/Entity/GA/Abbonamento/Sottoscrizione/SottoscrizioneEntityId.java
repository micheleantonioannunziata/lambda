package com.lambda.demo.Entity.GA.Abbonamento.Sottoscrizione;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class SottoscrizioneEntityId implements Serializable {

    private int idAcquirente;
    private int idPiano;

    @Column(columnDefinition = "DATE DEFAULT (curdate())", insertable = false, updatable = false)
    private Date data;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SottoscrizioneEntityId that = (SottoscrizioneEntityId) o;
        return getIdAcquirente() == that.getIdAcquirente() && getIdPiano() == that.getIdPiano();
    }

    @Override
    public int hashCode() {
        int result = getIdAcquirente();
        result = 31 * result + getIdPiano();
        return result;
    }
}
