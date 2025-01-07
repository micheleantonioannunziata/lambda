package com.lambda.demo.Service.GC.Inserzione;

import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Exception.GC.GCException;

import java.util.List;

public interface InserzioneService {

    void checkAddInserzione(ProdottoEntity prodotto, String prezzoBase, String quantity, String scontoStandard, String scontoPremium) throws GCException;

    void addInserzione(ProdottoEntity prodotto, String partitaIva, String prezzoBase, String quantity, String scontoStandard, String scontoPremium) throws GCException;

    List<InserzioneEntity> getInsertionsByIdSuperProdotto(int idSuperProdotto);


    InserzioneEntity getInsertionsCombinationsByVendor(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione, String colore);

    InserzioneEntity findById(InserzioneEntityId id);

}