package com.lambda.demo.Service.GC.Inserzione;


import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Exception.GC.GCException;
import com.lambda.demo.Exception.GC.GestioneInserzione.*;
import com.lambda.demo.Repository.GC.Inserzione.InserzioneRepository;
import com.lambda.demo.Repository.GC.Prodotto.ProdottoRepository;
import com.lambda.demo.Repository.GPR.RivenditoreRepository;
import com.lambda.demo.Utility.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InserzioneServiceImpl implements InserzioneService {

    @Autowired
    private InserzioneRepository inserzioneRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private RivenditoreRepository rivenditoreRepository;

    @Override
    public void checkAddInserzione(ProdottoEntity prodotto, String prezzoBase, String quantity, String scontoStandard, String scontoPremium) throws GCException {

        String ramReq = String.valueOf(prodotto.getId().getRam());
        String spazioArchiviazioneReq = String.valueOf(prodotto.getId().getSpazioArchiviazione());



        if (Double.parseDouble(prezzoBase) <= 0)
            throw new InvalidPriceException("Valore prezzo non ammesso!");

        if (!Validator.isValidPrezzo(prezzoBase))
            throw new InvalidPriceException("Prezzo non rispetta il formato!");

        if (!Validator.isValidQuantita(quantity))
            throw new InvalidQuantityException("Quantità non rispetta il formato!");

        if (!Validator.isValidScontoBase(scontoStandard))
            throw new InvalidStandardDiscountException("Sconto base non rispetta il formato!");

        if (Integer.parseInt(scontoStandard) < 0 || Integer.parseInt(scontoStandard) > 80)
            throw new InvalidStandardDiscountException("Valore sconto base non ammesso!");

        if (!Validator.isValidScontoPremium(scontoPremium))
            throw new InvalidPremiumDiscountException("Sconto premium non rispetta il formato!");

        if (Integer.parseInt(scontoPremium) < 0 || Integer.parseInt(scontoPremium) > 80 || Integer.parseInt(scontoPremium) < Integer.parseInt(scontoStandard))
            throw new InvalidPremiumDiscountException("Valore sconto premium non ammesso!");


        if (!Validator.isValidRamValue(ramReq))
            throw new InvalidRAMException("Valore ram non ammesso!");

        if (!Validator.isValidStorageValue(spazioArchiviazioneReq))
            throw new InvalidStorageException("Valore spazio archiviazione non ammesso!");


        if(prodottoRepository.findById(prodotto.getId()).isEmpty())
            throw new ProductNotFoundException("Prodotto non trovato!");

    }


    @Override
    public void addInserzione(ProdottoEntity prodotto, String partitaIva, String prezzoBase, String quantity, String scontoStandard, String scontoPremium){
        RivenditoreEntity rivenditore = rivenditoreRepository.findByPartitaIva(partitaIva);

        InserzioneEntityId inserzioneEntityId = new InserzioneEntityId(rivenditore.getPartitaIva(), prodotto.getId());



        InserzioneEntity inserzioneEntity = InserzioneEntity.builder()
                .id(inserzioneEntityId)
                .prodotto(prodotto)
                .rivenditore(rivenditore)
                .quantita(Integer.parseInt(quantity))
                .prezzoBase(Double.parseDouble(prezzoBase))
                .scontoStandard(Integer.parseInt(scontoStandard))
                .scontoPremium(Integer.parseInt(scontoPremium))
                .build();


        inserzioneRepository.save(inserzioneEntity);
    }


    @Override
    public List<InserzioneEntity> getInsertionsByIdSuperProdotto(int idSuperProdotto) {
        List<InserzioneEntity> inserzioni = inserzioneRepository.findById_IdProdotto_SuperProdottoId(idSuperProdotto);
        if (inserzioni == null) inserzioni = new ArrayList<InserzioneEntity>();

        return inserzioni;
    }

    @Override
    public InserzioneEntity getInsertionsCombinationsByVendor(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione, String colore) {
        InserzioneEntityId id = new InserzioneEntityId();
        id.setPartitaIvaRivenditore(partitaIvaRivenditore);

        ProdottoEntityId prodottoEntityId = new ProdottoEntityId();
        prodottoEntityId.setRam(ram);
        prodottoEntityId.setSpazioArchiviazione(spazioArchiviazione);
        prodottoEntityId.setColore(colore);
        prodottoEntityId.setSuperProdottoId(idSuperProdotto);

        id.setIdProdotto(prodottoEntityId);

        InserzioneEntity inserzioneEntity = null;

        if (inserzioneRepository.findById(id).isPresent()) inserzioneEntity = inserzioneRepository.findById(id).get();

        return inserzioneEntity;

    }

    @Override
    public InserzioneEntity findById(InserzioneEntityId id) {
        return inserzioneRepository.findById(id).get();
    }
}