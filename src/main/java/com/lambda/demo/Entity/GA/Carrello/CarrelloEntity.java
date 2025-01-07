package com.lambda.demo.Entity.GA.Carrello;


import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "carrello")
public class CarrelloEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(
            name = "acquirente_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    foreignKeyDefinition = "FOREIGN KEY (acquirente_id) REFERENCES acquirente(id) ON UPDATE CASCADE ON DELETE CASCADE"
            )
    )
    private AcquirenteEntity acquirente;


    @Check(constraints = "prezzo_provvisorio >= 0")
    @Column(nullable = false, columnDefinition = "DECIMAL (10, 2)")
    private double prezzoProvvisorio;


    //se nello stesso metodo cerchi di accedere a questa lista con fetch.lazy (default) dà errore perché dovresti rieffettuare
    //una query dal db. Questo oggetto non si trovava in realtà nel db ma nella cache di Hibernate, ovvero nel persistent Context
    //Scrivendo fetch.eager si forza il caricamento degli oggetti appartenenti alla lista
    @OneToMany(mappedBy = "carrello", fetch = FetchType.EAGER)
    private List<FormazioneCarrelloEntity> carrelloItems = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarrelloEntity that = (CarrelloEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }

    public List<FormazioneCarrelloEntity> getCarrelloItems(){
        if (this.carrelloItems == null)
            this.carrelloItems = new ArrayList<>();
        return this.carrelloItems;
    }

    public double getPrezzoProvvisorio(){
        BigDecimal bd = new BigDecimal(this.prezzoProvvisorio).setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
