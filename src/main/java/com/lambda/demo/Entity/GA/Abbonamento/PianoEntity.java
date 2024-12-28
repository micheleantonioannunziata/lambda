package com.lambda.demo.Entity.GA.Abbonamento;


import com.lambda.demo.Entity.GA.Abbonamento.Sottoscrizione.SottoscrizioneEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "piano")
public class PianoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String nome;

    @Check(constraints = "prezzo > 0")
    @Column(nullable = false, columnDefinition = "DECIMAL(10, 2)")
    private double prezzo;

    @Check(constraints = "durata IN(1, 6, 12)")
    @Column(nullable = false, columnDefinition = "INT")
    private int durata;


    @OneToMany(mappedBy = "piano")
    private List<SottoscrizioneEntity> sottoscrizioni = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PianoEntity that = (PianoEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
