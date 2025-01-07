package com.lambda.demo.Entity.GPR;


import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GP.IscrizioneNewsletter.IscrizioneNewsletterEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Rivenditore")
public class RivenditoreEntity {
    @Id
    @Column(columnDefinition = "CHAR(11)")
    private String partitaIva;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(50)")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true, columnDefinition = "VARCHAR(50)")
    private String ragioneSociale;

    @Column
    private String indirizzo;


    @OneToMany(mappedBy = "rivenditore")
    private List<InserzioneEntity> inserzioni = new ArrayList<>();


    @OneToMany(mappedBy = "rivenditore")
    private List<IscrizioneNewsletterEntity> newsletters = new ArrayList<>();


    public InserzioneEntity getCheapestInsertion(int idSuperProdotto){
        List<InserzioneEntity> inserzioni = getInserzioni();
        if (inserzioni == null) inserzioni = new ArrayList<>();
        List<InserzioneEntity> results = new ArrayList<>();


        for(InserzioneEntity i : inserzioni){
            if(i.getProdotto().getSuperProdotto().getId() == idSuperProdotto) results.add(i);
        }

        results.sort(Comparator.comparingDouble(InserzioneEntity::getPrezzoBase));

        return results.getFirst();
    }

    public List<InserzioneEntity> getInsertionsBySuperProductId(int idSuperProdotto){
        List<InserzioneEntity> inserzioni = getInserzioni();
        if (inserzioni == null) inserzioni = new ArrayList<>();
        List<InserzioneEntity> results = new ArrayList<>();


        for(InserzioneEntity i : inserzioni){
            if(i.getProdotto().getSuperProdotto().getId() == idSuperProdotto) results.add(i);
        }


        return results;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RivenditoreEntity that = (RivenditoreEntity) o;
        return getPartitaIva().equals(that.getPartitaIva());
    }

    @Override
    public int hashCode() {
        return getPartitaIva().hashCode();
    }
}
