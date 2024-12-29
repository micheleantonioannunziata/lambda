package com.lambda.demo.Repository.GA.Ordine;

import com.lambda.demo.Entity.GA.Ordine.OrdineEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdineRepository extends JpaRepository<OrdineEntity, Integer> {
}
