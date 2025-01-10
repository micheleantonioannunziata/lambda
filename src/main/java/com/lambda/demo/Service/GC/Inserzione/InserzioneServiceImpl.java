package com.lambda.demo.Service.GC.Inserzione;


import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidColorException;
import com.lambda.demo.Exception.GC.GCException;
import com.lambda.demo.Exception.GC.GestioneInserzione.*;
import com.lambda.demo.Repository.GC.Inserzione.InserzioneRepository;
import com.lambda.demo.Repository.GC.Prodotto.ProdottoRepository;
import com.lambda.demo.Repository.GPR.RivenditoreRepository;
import com.lambda.demo.Utility.SessionManager;
import com.lambda.demo.Utility.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    @Autowired
    private HttpServletRequest request;

    @Override
    public void checkTechRequirements(String idSuperProdotto, String ram, String spazioArchiviazione, String colore) throws GCException, InvalidColorException {
        int x;
        try {
            x = Integer.parseInt(idSuperProdotto);
        } catch (NumberFormatException e) {
            throw new GCException("ID del super prodotto non valido. Assicurati che sia un numero intero valido.");
        }

        if (!Validator.isValidRamValue(ram)) {
            throw new InvalidRAMException("Valore RAM non ammesso!");
        }

        if (!Validator.isValidStorageValue(spazioArchiviazione)) {
            throw new InvalidStorageException("Valore spazio archiviazione non ammesso!");
        }

        if (!Validator.isValidColor(colore)) {
            throw new InvalidColorException("Colore non rispetta il formato richiesto.");
        }
    }



    @Override
    public void checkPriceQuantity(String quantity, String prezzoBase, String scontoStandard, String scontoPremium) throws GCException {
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
    }


    @Override
    public void checkAddInserzione(ProdottoEntity prodotto, String prezzoBase, String quantity, String scontoStandard, String scontoPremium) throws GCException, InvalidColorException {
        checkTechRequirements(String.valueOf(prodotto.getId().getSuperProdottoId()), String.valueOf(prodotto.getId().getRam()), String.valueOf(prodotto.getId().getSpazioArchiviazione()), prodotto.getId().getColore());
        checkPriceQuantity(quantity, prezzoBase, scontoStandard, scontoPremium);
        if(prodottoRepository.findById(prodotto.getId()).isEmpty())
            throw new ProductNotFoundException("Prodotto non trovato!");
    }

    @Override
    public void addInserzione(ProdottoEntity prodotto, String prezzoBase, String quantity, String scontoStandard, String scontoPremium) throws GCException, InvalidColorException {
        checkAddInserzione(prodotto, prezzoBase, quantity, scontoStandard, scontoPremium);

        RivenditoreEntity rivenditore = rivenditoreRepository.findByPartitaIva(SessionManager.getRivenditore(request).getPartitaIva());

        InserzioneEntityId idInserzione = new InserzioneEntityId(rivenditore.getPartitaIva(), prodotto.getId());
        InserzioneEntity inserzione = new InserzioneEntity();
        inserzione.setId(idInserzione); inserzione.setRivenditore(rivenditore); inserzione.setProdotto(prodotto);
        inserzione.setQuantita(Integer.parseInt(quantity)); inserzione.setPrezzoBase(Double.parseDouble(prezzoBase));
        inserzione.setScontoStandard(Integer.parseInt(scontoStandard)); inserzione.setScontoPremium(Integer.parseInt(scontoPremium));
        inserzione.setDisponibilita(true); inserzione.setDataPubblicazione(LocalDateTime.now());

        inserzioneRepository.save(inserzione);


        InserzioneEntityId inserzioneEntityId = new InserzioneEntityId(rivenditore.getPartitaIva(), prodotto.getId());
        InserzioneEntity justAddedInsertion = findById(inserzioneEntityId);

        SessionManager.getRivenditore(request).getInserzioni().add(justAddedInsertion);
    }


    @Override
    public List<InserzioneEntity> getInsertionsByIdSuperProdotto(int idSuperProdotto) {
        List<InserzioneEntity> inserzioni = inserzioneRepository.findById_IdProdotto_SuperProdottoId(idSuperProdotto);
        if (inserzioni == null) inserzioni = new ArrayList<>();

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
        if (inserzioneRepository.findById(id).isPresent()) return inserzioneRepository.findById(id).get();
        return null;
    }
}