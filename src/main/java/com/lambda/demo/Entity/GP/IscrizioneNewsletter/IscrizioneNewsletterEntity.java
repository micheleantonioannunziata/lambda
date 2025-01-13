package com.lambda.demo.Entity.GP.IscrizioneNewsletter;


import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "iscrizione_newsletter")
public class IscrizioneNewsletterEntity {


    @EmbeddedId
    private IscrizioneNewsletterEntityId id;


    @MapsId("idAcquirente")
    @ManyToOne
    @JoinColumn(
            name = "acquirente_id",
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (acquirente_id) REFERENCES acquirente(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private AcquirenteEntity acquirente;


    @MapsId("partitaIvaRivenditore")
    @ManyToOne
    @JoinColumn(
            name = "partita_iva_rivenditore",
            columnDefinition = "CHAR(11)",
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (partita_iva_rivenditore) REFERENCES rivenditore(partita_iva) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private RivenditoreEntity rivenditore;


    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "DATETIME")
    private Date dataIscrizione;


    public void setAcquirente(AcquirenteEntity acquirente) {
        if (this.acquirente != acquirente && acquirente != null) {
            this.acquirente = acquirente;
            if (!acquirente.getIscrizioniNewsletter().contains(this)) acquirente.getIscrizioniNewsletter().add(this);
        }
    }

    public void setRivenditore(RivenditoreEntity rivenditore) {
        if (this.rivenditore != rivenditore && rivenditore != null) {
            this.rivenditore = rivenditore;
            if (!rivenditore.getNewsletters().contains(this)) rivenditore.getNewsletters().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IscrizioneNewsletterEntity that = (IscrizioneNewsletterEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
