package com.lambda.demo.Repository.GPR;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
            "WHERE r.id = :#{#rivenditore.id}")
    int updateRivenditoreEntity(RivenditoreEntity rivenditore);
}
