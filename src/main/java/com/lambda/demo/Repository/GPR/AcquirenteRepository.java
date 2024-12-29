package com.lambda.demo.Repository.GPR;

import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

}