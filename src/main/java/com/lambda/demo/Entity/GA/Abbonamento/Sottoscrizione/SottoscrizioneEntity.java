package com.lambda.demo.Entity.GA.Abbonamento.Sottoscrizione;


import com.lambda.demo.Entity.GA.Abbonamento.PianoEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "sottoscrizione")
public class SottoscrizioneEntity {

    @EmbeddedId
    private SottoscrizioneEntityId id;

    @MapsId("idPiano")
    @ManyToOne
    @JoinColumn(
            name = "piano_id",
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (piano_id) REFERENCES piano(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private PianoEntity piano;


    @MapsId("idAcquirente")
    @ManyToOne
    @JoinColumn(
            name = "acquirente_id",
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (acquirente_id) REFERENCES acquirente(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private AcquirenteEntity acquirente;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT 1")
    private boolean attivo;

    public void setAcquirente(AcquirenteEntity acquirente) {
        if(this.acquirente != acquirente && acquirente != null){
            this.acquirente = acquirente;
            if(!acquirente.getSottoscrizioni().contains(this)) acquirente.getSottoscrizioni().add(this);
        }
    }

    public void setPiano(PianoEntity piano) {
        if(this.piano != piano && piano != null){
            this.piano = piano;
            if(!piano.getSottoscrizioni().contains(this)) piano.getSottoscrizioni().add(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        SottoscrizioneEntity that = (SottoscrizioneEntity) o;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}
