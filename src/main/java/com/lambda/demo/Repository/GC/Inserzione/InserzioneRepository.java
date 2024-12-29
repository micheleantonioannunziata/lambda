package com.lambda.demo.Repository.GC.Inserzione;

import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InserzioneRepository extends JpaRepository<InserzioneEntity, InserzioneEntityId> {
}
