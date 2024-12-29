package com.lambda.demo.Repository.GA.Permuta;

import com.lambda.demo.Entity.GA.Permuta.PermutaEntity;
import com.lambda.demo.Entity.GA.Permuta.PermutaEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface PermutaRepository extends JpaRepository<PermutaEntity, PermutaEntityId> {

    //Impedisce alla query di restituire un oggetto null evitando NullPointerException
    @Query
    Optional<PermutaEntity> findById(PermutaEntityId id);

}
