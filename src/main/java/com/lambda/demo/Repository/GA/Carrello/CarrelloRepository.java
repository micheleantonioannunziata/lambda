package com.lambda.demo.Repository.GA.Carrello;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarrelloRepository extends JpaRepository<CarrelloEntity, Integer> {
    @Query
    CarrelloEntity findByAcquirente(AcquirenteEntity acquirenteEntity);

    @Modifying
    @Query(value = "DELETE FROM carrello WHERE acquirente_id = :acquirenteId", nativeQuery = true)
    void deleteCarrelloByAcquirente(@Param("acquirenteId") int acquirenteId);


    @Query(value = "SELECT * from carrello WHERE acquirente_id = :acquirenteId", nativeQuery = true)
    CarrelloEntity findByAcquirente(@Param("acquirenteId") int acquirenteId);


    @Modifying
    @Transactional
    @Query(value = "INSERT INTO carrello (acquirente_id, prezzo_provvisorio) VALUES (:acquirenteId, :prezzoProvvisorio)", nativeQuery = true)
    int insert(@Param("acquirenteId") int acquirenteId, @Param("prezzoProvvisorio") double prezzoProvvisorio);


}