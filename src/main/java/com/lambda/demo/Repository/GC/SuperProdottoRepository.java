package com.lambda.demo.Repository.GC;

import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SuperProdottoRepository extends JpaRepository<SuperProdottoEntity, Integer> {
}
