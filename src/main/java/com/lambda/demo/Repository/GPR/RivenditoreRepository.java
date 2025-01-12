package com.lambda.demo.Repository.GPR;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RivenditoreRepository extends JpaRepository<RivenditoreEntity, String> {

    @Query
    RivenditoreEntity findByPartitaIva(String partitaIva);

    @Query
    RivenditoreEntity findByEmail(String email);

    @Query
    RivenditoreEntity findByEmailAndPassword(String email, String password);

    @Query
    RivenditoreEntity findByRagioneSociale(String ragioneSociale);

    @Modifying
    @Query("UPDATE RivenditoreEntity r SET r.ragioneSociale = :#{#rivenditore.ragioneSociale}, " +
            "r.indirizzo = :#{#rivenditore.indirizzo}, " +
            "r.password = :#{#rivenditore.password} " +
            "WHERE r.partitaIva = :#{#rivenditore.partitaIva}")
    int updateRivenditoreEntity(RivenditoreEntity rivenditore);


    @Query(value = """
            SELECT DISTINCT r.*
            FROM super_prodotto sp
                     JOIN prodotto p ON sp.id = p.super_prodotto_id
                     JOIN inserzione i ON i.ram = p.ram
                AND i.spazio_archiviazione = p.spazio_archiviazione
                AND i.colore = p.colore
                AND i.super_prodotto_id = p.super_prodotto_id
                     JOIN rivenditore r ON i.partita_iva_rivenditore = r.partita_iva
            WHERE i.super_prodotto_id = :superProdottoId
            """, nativeQuery = true)
    List<RivenditoreEntity> findDistinctBySuperProdottoId(@Param("superProdottoId") int superProdottoId);

    @Query
    boolean existsByRagioneSociale(String ragioneSociale);

    @Query
    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "delete from rivenditore r where r.email =:email", nativeQuery = true)
    void deleteRivenditoreEntityByEmail(String email);
}