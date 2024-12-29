package com.lambda.demo.Repository.GA.Ordine;

import com.lambda.demo.Entity.GA.Ordine.Composizione.ComposizioneEntity;
import com.lambda.demo.Entity.GA.Ordine.Composizione.ComposizioneEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComposizioneRepository extends JpaRepository<ComposizioneEntity, ComposizioneEntityId> {
}
