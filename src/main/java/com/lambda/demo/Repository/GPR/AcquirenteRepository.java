package com.lambda.demo.Repository.GPR;

import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AcquirenteRepository extends JpaRepository<AcquirenteEntity, Integer> {
    @Query
    AcquirenteEntity findByEmail(String email);

    @Query
    AcquirenteEntity findByEmailAndPassword(String email, String password);

    @Modifying
    @Query("UPDATE AcquirenteEntity a SET a.nome = :#{#acquirente.nome}, " +
            "a.cognome = :#{#acquirente.cognome}, " +
            "a.indirizzo = :#{#acquirente.indirizzo}, " +
            "a.password = :#{#acquirente.password} " +
            "WHERE a.id = :#{#acquirente.id}")
    int updateAcquirenteEntity(AcquirenteEntity acquirente);


    @Modifying
    @Query(value = "UPDATE acquirente a SET a.saldo = :saldo WHERE a.id = :id", nativeQuery = true)
    void updateSaldoLambdaPoints(@Param("saldo") int saldo, @Param("id") int acquirenteId);

    @Transactional
    @Modifying
    @Query(value = "DELETE from acquirente a where a.email =:email", nativeQuery = true)
    void deleteAcquirenteEntityByEmail(String email);

}