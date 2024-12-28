package com.lambda.demo.Entity.GA.Ordine;


import com.lambda.demo.Entity.GA.Ordine.Composizione.ComposizioneEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.CreationTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "ordine")
public class OrdineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Check(constraints = "prezzo > 0")
    @Column(nullable = false, columnDefinition = "DECIMAL (10, 2)")
    private double prezzo;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String stato;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "DATETIME")
    private Date dataOra;

    @ManyToOne
    @JoinColumn(
            name = "id_acquirente",
            nullable = false,
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (id_acquirente) REFERENCES acquirente(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private AcquirenteEntity acquirente;


    @OneToMany(mappedBy = "ordine")
    private List<ComposizioneEntity> composizioni = new ArrayList<>();


    public void setAcquirente(AcquirenteEntity acquirente) {
        if(this.acquirente != acquirente && acquirente != null) {
            this.acquirente = acquirente;
            if (!acquirente.getOrdini().contains(this)) acquirente.getOrdini().add(this);
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrdineEntity that = (OrdineEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
