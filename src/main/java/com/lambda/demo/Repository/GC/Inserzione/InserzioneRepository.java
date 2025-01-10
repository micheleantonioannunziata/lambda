package com.lambda.demo.Repository.GC.Inserzione;

import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InserzioneRepository extends JpaRepository<InserzioneEntity, InserzioneEntityId> {

    @Query
    List<InserzioneEntity> findById_IdProdotto_SuperProdottoId(int idIdProdottoSuperProdottoId);

    @Transactional
    @Modifying
    @Query(value = "insert into inserzione (disponibilita, prezzo_base, quantita, ram, sconto_premium, sconto_standard, spazio_archiviazione, super_prodotto_id, data_pubblicazione, colore, partita_iva_rivenditore) " +
            "VALUES (:#{#inserzione.disponibilita}, :#{#inserzione.prezzoBase}, :#{#inserzione.quantita}, :#{#inserzione.id.idProdotto.ram}, :#{#inserzione.scontoPremium}, :#{#inserzione.scontoStandard}, :#{#inserzione.id.idProdotto.spazioArchiviazione}, :#{#inserzione.id.idProdotto.superProdottoId}, :#{#inserzione.dataPubblicazione}, :#{#inserzione.id.idProdotto.colore}, :#{#inserzione.id.partitaIvaRivenditore})",
            nativeQuery = true)
    void insertInserzione(@Param("inserzione") InserzioneEntity inserzione);


}
