package com.lambda.demo.Entity.GP.IscrizioneNewsletter;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IscrizioneNewsletterEntityId implements Serializable {

    private int idAcquirente;
    @Column(name = "partita_iva_rivenditore", columnDefinition = "CHAR(11)")
    private String partitaIvaRivenditore;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IscrizioneNewsletterEntityId that = (IscrizioneNewsletterEntityId) o;
        return getIdAcquirente() == that.getIdAcquirente() && getPartitaIvaRivenditore().equals(that.getPartitaIvaRivenditore());
    }

    @Override
    public int hashCode() {
        int result = getIdAcquirente();
        result = 31 * result + getPartitaIvaRivenditore().hashCode();
        return result;
    }
}
