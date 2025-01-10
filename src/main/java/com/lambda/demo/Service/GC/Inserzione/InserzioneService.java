package com.lambda.demo.Service.GC.Inserzione;

import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidColorException;
import com.lambda.demo.Exception.GC.GCException;

import java.util.List;

public interface InserzioneService {

    void checkTechRequirements(String idSuperProdotto, String ram, String spazioArchiviazione, String colore) throws GCException, InvalidColorException;

    void checkPriceQuantity(String quantity, String prezzoBase, String scontoStandard, String scontoPremium) throws GCException;

    void checkAddInserzione(ProdottoEntity prodotto, String prezzoBase, String quantity, String scontoStandard, String scontoPremium) throws GCException, InvalidColorException;

    void addInserzione(ProdottoEntity prodotto, String prezzoBase, String quantity, String scontoStandard, String scontoPremium) throws GCException, InvalidColorException;

    List<InserzioneEntity> getInsertionsByIdSuperProdotto(int idSuperProdotto);

    InserzioneEntity getInsertionsCombinationsByVendor(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione, String colore);

    InserzioneEntity findById(InserzioneEntityId id);

}