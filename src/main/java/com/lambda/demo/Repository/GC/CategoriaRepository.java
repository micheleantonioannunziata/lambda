package com.lambda.demo.Repository.GC;

import com.lambda.demo.Entity.GC.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {

    @Query
    CategoriaEntity findById(int id);

    @Query
    boolean existsByNome(String nome);

    @Query
    CategoriaEntity findByNome(String nome);
}
