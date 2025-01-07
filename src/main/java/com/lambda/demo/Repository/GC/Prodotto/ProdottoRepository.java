package com.lambda.demo.Repository.GC.Prodotto;


import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<ProdottoEntity, ProdottoEntityId> {

    @Query(value = "SELECT * from prodotto", nativeQuery = true)
    List<ProdottoEntity> findAll();
}
