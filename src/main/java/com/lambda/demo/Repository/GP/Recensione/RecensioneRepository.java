package com.lambda.demo.Repository.GP.Recensione;

import com.lambda.demo.Entity.GP.Recensione.RecensioneEntity;
import com.lambda.demo.Entity.GP.Recensione.RecensioneEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RecensioneRepository extends JpaRepository<RecensioneEntity, RecensioneEntityId> {
}
