package com.lambda.demo.Repository.GA.Abbonamento;

import com.lambda.demo.Entity.GA.Abbonamento.PianoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PianoRepository extends JpaRepository<PianoEntity, Integer> {
}
