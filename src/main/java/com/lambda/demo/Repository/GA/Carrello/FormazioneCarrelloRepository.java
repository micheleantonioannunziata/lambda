package com.lambda.demo.Repository.GA.Carrello;

import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntityId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FormazioneCarrelloRepository extends JpaRepository<FormazioneCarrelloEntity, FormazioneCarrelloEntityId> {


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO formazione_carrello (carrello_id, quantita, ram, spazio_archiviazione, super_prodotto_id, colore, partita_iva_rivenditore) " +
            "VALUES (:carrelloId, :quantita, :ram, :spazio_archiviazione, :superProdottoId, :colore, :partitaIvaRivenditore) ", nativeQuery = true)
    int insert(@Param("carrelloId") int carrelloId,
               @Param("quantita") int quantita,
               @Param("ram") int ram,
               @Param("spazio_archiviazione") int spazioArchiviazione,
               @Param("superProdottoId") int superProdottoId,
               @Param("colore") String colore,
               @Param("partitaIvaRivenditore") String partitaIvaRivenditore);


}