package com.lambda.demo.Service.GC.SuperProdotto;

import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;

import java.util.List;

public interface SuperProdottoService {
    List<SuperProdottoEntity> findByName(String name);

    List<ProdottoEntity> findProductsBySuperProdottoId(int id);

    List<SuperProdottoEntity> findByNameAndCategory(String name, int idCategoria);

    List<ProdottoEntity> findProductsCombinations(int idSuperProdotto, String ram, String spazioArchiviazione, String colore);

    SuperProdottoEntity findById(int id);

    List<RivenditoreEntity> findVendorsBySuperProdottoId(int id);
}
