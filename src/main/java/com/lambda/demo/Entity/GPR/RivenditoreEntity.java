package com.lambda.demo.Entity.GPR;


import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GP.IscrizioneNewsletter.IscrizioneNewsletterEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String ragioneSociale;

    @Column
    private String indirizzo;


    @OneToMany(mappedBy = "rivenditore")
    private List<InserzioneEntity> inserzioni = new ArrayList<>();


    @OneToMany(mappedBy = "rivenditore")
    private List<IscrizioneNewsletterEntity> newsletters = new ArrayList<>();


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
