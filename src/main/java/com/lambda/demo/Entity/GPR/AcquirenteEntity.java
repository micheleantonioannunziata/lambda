package com.lambda.demo.Entity.GPR;


import com.lambda.demo.Entity.GA.Abbonamento.Sottoscrizione.SottoscrizioneEntity;
import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GA.Ordine.OrdineEntity;
import com.lambda.demo.Entity.GA.Permuta.PermutaEntity;
import com.lambda.demo.Entity.GP.IscrizioneNewsletter.IscrizioneNewsletterEntity;
import com.lambda.demo.Entity.GP.Recensione.RecensioneEntity;
import com.lambda.demo.Entity.GP.Wishlist.WishlistEntity;
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
@Table(name="acquirente")
public class AcquirenteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, columnDefinition = "VARCHAR(50)")
    private String nome;

    @Column(columnDefinition = "VARCHAR(50)")
    private String cognome;

    @Column(unique = true, nullable = false, columnDefinition = "VARCHAR(50)")
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, columnDefinition = "int unsigned default 0")
    private int saldo;

    @Column
    private String indirizzo;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT 0")
    private boolean premium;


    @OneToMany(mappedBy = "acquirente")
    private List<PermutaEntity> permute = new ArrayList<>();

    @OneToMany(mappedBy = "acquirente")
    private List<RecensioneEntity> recensioni = new ArrayList<>();

    @OneToMany(mappedBy = "acquirente")
    private List<IscrizioneNewsletterEntity> iscrizioniNewsletter = new ArrayList<>();

    @OneToOne(mappedBy = "acquirente")
    private CarrelloEntity carrello;

    @OneToOne(mappedBy = "acquirente")
    private WishlistEntity wishlist;

    @OneToMany(mappedBy = "acquirente")
    private List<SottoscrizioneEntity> sottoscrizioni = new ArrayList<>();

    @OneToMany(mappedBy = "acquirente", fetch = FetchType.EAGER)
    private List<OrdineEntity> ordini = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AcquirenteEntity that = (AcquirenteEntity) o;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return getId();
    }
}
