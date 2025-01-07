package com.lambda.demo.Service.GC.Prodotto;

import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import com.lambda.demo.Repository.GC.Prodotto.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProdottoServiceImpl implements ProdottoService {

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Override
    public ProdottoEntity getProdottoById(ProdottoEntityId id) {
        return prodottoRepository.findById(id).get();
    }
}