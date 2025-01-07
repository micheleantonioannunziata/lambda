package com.lambda.demo.Service.GC.Prodotto;

import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;

public interface ProdottoService {

    ProdottoEntity getProdottoById(ProdottoEntityId id);
}