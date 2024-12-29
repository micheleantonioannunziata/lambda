package com.lambda.demo.Repository.GA.Abbonamento;

import com.lambda.demo.Entity.GA.Abbonamento.Sottoscrizione.SottoscrizioneEntity;
import com.lambda.demo.Entity.GA.Abbonamento.Sottoscrizione.SottoscrizioneEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SottoscrizioneRepository extends JpaRepository<SottoscrizioneEntity, SottoscrizioneEntityId> {
}
