package com.lambda.demo.Repository.GC;

import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SuperProdottoRepository extends JpaRepository<SuperProdottoEntity, Integer> {

    @Query
    SuperProdottoEntity findById(int id);


    @Query("SELECT sp FROM SuperProdottoEntity sp WHERE CONCAT(sp.marca, ' ', sp.modello) LIKE %:name%")
    List<SuperProdottoEntity> findByName (@Param("name") String name);

    @Query(value = "SELECT * FROM super_prodotto WHERE CONCAT(marca, ' ', modello) LIKE CONCAT('%', :name, '%') AND categoria_id = :idCategoria",
            nativeQuery = true)
    List<SuperProdottoEntity> findByNameAndIdCategoria(@Param("name") String name, @Param("idCategoria") int idCategoria);

    @Query
    boolean existsByModello(String modello);

    @Query
    SuperProdottoEntity findByModello(String modello);

}
