package com.lambda.demo.Service.GA.Carrello;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntityId;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidColorException;
import com.lambda.demo.Exception.GC.GestioneInserzione.InvalidQuantityException;
import com.lambda.demo.Exception.GC.GestioneInserzione.InvalidRAMException;
import com.lambda.demo.Exception.GC.GestioneInserzione.InvalidStorageException;
import com.lambda.demo.Exception.GC.GestioneInserzione.ProductNotFoundException;
import com.lambda.demo.Exception.GC.InvalidSuperProductIdException;
import com.lambda.demo.Exception.GC.SuperProductNotFoundException;
import com.lambda.demo.Exception.GPR.AccessoRivenditore.InvalidVATNumberException;
import com.lambda.demo.Exception.GPR.VendorNotFoundException;
import com.lambda.demo.Repository.GA.Carrello.CarrelloRepository;
import com.lambda.demo.Repository.GA.Carrello.FormazioneCarrelloRepository;
import com.lambda.demo.Repository.GC.Inserzione.InserzioneRepository;
import com.lambda.demo.Repository.GC.Prodotto.ProdottoRepository;
import com.lambda.demo.Repository.GC.SuperProdottoRepository;
import com.lambda.demo.Repository.GPR.RivenditoreRepository;
import com.lambda.demo.Utility.SessionManager;
import com.lambda.demo.Utility.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarrelloServiceImpl implements CarrelloService {
    @Autowired
    private InserzioneRepository inserzioneRepository;

    @Autowired
    private SuperProdottoRepository superProdottoRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;


    @Autowired
    private RivenditoreRepository rivenditoreRepository;

    @Autowired
    private FormazioneCarrelloRepository formazioneCarrelloRepository;

    public boolean isValidInsertion(String partitaIvaRivenditore, String idSuperProdotto, String ram, String spazioArchiviazione, String colore) throws InvalidRAMException, InvalidStorageException, InvalidVATNumberException, InvalidColorException, ProductNotFoundException, VendorNotFoundException, InvalidSuperProductIdException, SuperProductNotFoundException {
        if (!Validator.isValidRamValue(ram))
            throw new InvalidRAMException("Valore ram non ammesso -  DOM probabilmente modificato");

        if (!Validator.isValidStorageValue(spazioArchiviazione))
            throw new InvalidStorageException("Valore spazio archiviazione non ammesso - DOM probabilmente modificato");

        if (!Validator.isValidPartitaIVA(partitaIvaRivenditore))
            throw new InvalidVATNumberException("Valore partita iva non ammesso - DOM probabilmente modificato");

        if (!Validator.isValidColor(colore))
            throw new InvalidColorException("Colore non rispetta il formato richiesto - DOM probabilmente modificato");

        if (superProdottoRepository.findById(Integer.parseInt(idSuperProdotto)) == null)
            throw new SuperProductNotFoundException("SuperProdotto non trovaot - DOM probabilmente modificato");

        if (prodottoRepository.findById(new ProdottoEntityId(Integer.parseInt(idSuperProdotto), Integer.parseInt(ram), Integer.parseInt(spazioArchiviazione), colore)).isEmpty())
            throw new ProductNotFoundException("Prodotto non trovato - DOM probabilmente modificato");

        if (rivenditoreRepository.findByPartitaIva(partitaIvaRivenditore) == null)
            throw new VendorNotFoundException("Rivenditore non trovato - DOM probabilmente modificato");

        return inserzioneRepository.findById(new InserzioneEntityId(partitaIvaRivenditore, new ProdottoEntityId(Integer.parseInt(idSuperProdotto), Integer.parseInt(ram), Integer.parseInt(spazioArchiviazione), colore))).isPresent();
    }


    @Override
    public void addToCart(String partitaIvaRivenditore, String idSuperProdotto, String ram, String spazioArchiviazione, String colore, HttpServletRequest request) throws Exception {
        if (!isValidInsertion(partitaIvaRivenditore, idSuperProdotto, ram, spazioArchiviazione, colore)) {
            throw new Exception("Inserzione non valida - DOM probabilmente modificato");
        }

        InserzioneEntityId inserzioneEntityId = new InserzioneEntityId(partitaIvaRivenditore,
                new ProdottoEntityId(Integer.parseInt(idSuperProdotto), Integer.parseInt(ram), Integer.parseInt(spazioArchiviazione), colore));
        InserzioneEntity inserzione = inserzioneRepository.findById(inserzioneEntityId).get();

        CarrelloEntity carrello = SessionManager.getCarrello(request);
        List<FormazioneCarrelloEntity> cartItems = carrello.getCarrelloItems();
        if (cartItems == null) {
            cartItems = new ArrayList<>();
            carrello.setCarrelloItems(cartItems);
        }

        if (inserzione.isDisponibilita()) {
            int itemIndex = getItemIndex(cartItems, inserzione);
            if (itemIndex == -1) {
                if (inserzione.getQuantita() > 0)
                    cartItems.add(new FormazioneCarrelloEntity(new FormazioneCarrelloEntityId(carrello.getId(), inserzioneEntityId), carrello, inserzione, 1));
            } else if (cartItems.get(itemIndex).getQuantita() + 1 <= inserzione.getQuantita()) {
                cartItems.get(itemIndex).setQuantita(cartItems.get(itemIndex).getQuantita() + 1);
            }


            carrello.setPrezzoProvvisorio(carrello.getPrezzoProvvisorio() + inserzione.returnDiscountedPrice(SessionManager.getAcquirente(request).isPremium()));
        }
        SessionManager.setCarrello(request, carrello);
    }


    @Override
    public void removeFromCart(String partitaIvaRivenditore, String idSuperProdotto, String ram, String spazioArchiviazione, String colore, HttpServletRequest req) throws Exception {
        if (!isValidInsertion(partitaIvaRivenditore, idSuperProdotto, ram, spazioArchiviazione, colore))
            throw new Exception("Inserzione non valida - DOM probabilmente modificato");

        InserzioneEntity inserzione = inserzioneRepository.findById(new InserzioneEntityId(partitaIvaRivenditore,
                new ProdottoEntityId(Integer.parseInt(idSuperProdotto), Integer.parseInt(ram), Integer.parseInt(spazioArchiviazione), colore))).get();

        CarrelloEntity carrello = SessionManager.getCarrello(req);
        List<FormazioneCarrelloEntity> cartItems = carrello.getCarrelloItems();


        int itemToRemoveIndex = getItemIndex(cartItems, inserzione);
        if (itemToRemoveIndex == -1)
            throw new Exception("Inserzione non presente nel carrello - DOM probabilmente modificato");

        carrello.setPrezzoProvvisorio(carrello.getPrezzoProvvisorio() -
                cartItems.get(itemToRemoveIndex).getInserzione().returnDiscountedPrice(SessionManager.getAcquirente(req).isPremium()) * cartItems.get(itemToRemoveIndex).getQuantita());

        cartItems.remove(itemToRemoveIndex);

        SessionManager.setCarrello(req, carrello);
    }


    @Override
    public void updateQuantity(String partitaIvaRivenditore, String idSuperProdotto, String ram, String spazioArchiviazione, String colore, String quantity, HttpServletRequest req) throws Exception {
        if (!isValidInsertion(partitaIvaRivenditore, idSuperProdotto, ram, spazioArchiviazione, colore))
            throw new Exception("Inserzione non valida -  DOM probabilmente modificato");

        InserzioneEntity inserzione = inserzioneRepository.findById(new InserzioneEntityId(partitaIvaRivenditore, new ProdottoEntityId(Integer.parseInt(idSuperProdotto), Integer.parseInt(ram), Integer.parseInt(spazioArchiviazione), colore))).get();

        int q;
        try {
            q = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            throw new InvalidQuantityException("Quantità non valida - DOM probabilmente modificato");
        }
        if (q > inserzione.getQuantita())
            throw new InvalidQuantityException("Quantità non valida - DOM probabilmente modificato");

        if (q == 0) removeFromCart(partitaIvaRivenditore, idSuperProdotto, ram, spazioArchiviazione, colore, req);
        else {
            CarrelloEntity carrello = SessionManager.getCarrello(req);
            List<FormazioneCarrelloEntity> cartItems = carrello.getCarrelloItems();


            int itemToUpdateIndex = getItemIndex(cartItems, inserzione);

            if (itemToUpdateIndex == -1)
                throw new Exception("Inserzione non presente nel carrello - DOM probabilmente modificato");

            int oldQuantity = carrello.getCarrelloItems().get(itemToUpdateIndex).getQuantita();

            carrello.getCarrelloItems().get(itemToUpdateIndex).setQuantita(q);

            double prezzoBase = carrello.getCarrelloItems().get(itemToUpdateIndex).getInserzione().returnDiscountedPrice(SessionManager.getAcquirente(req).isPremium());
            double oldTotal = oldQuantity * prezzoBase;
            double newTotal = q * prezzoBase;

            carrello.setPrezzoProvvisorio(carrello.getPrezzoProvvisorio() - oldTotal + newTotal);

            SessionManager.setCarrello(req, carrello);
        }
    }

    int getItemIndex(List<FormazioneCarrelloEntity> cartItems, InserzioneEntity inserzione) {
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getInserzione().equals(inserzione)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public CarrelloEntity getCartByUser(int idAcquirente) {
        return carrelloRepository.findByAcquirente(idAcquirente);
    }

    @Override
    public void deleteCartByUser(int idAcquirente) {
        carrelloRepository.deleteCarrelloByAcquirente(idAcquirente);
    }

    @Override
    public void insertCartByUser(int idAcquirente, double prezzoProvvisorio) {
        carrelloRepository.insert(idAcquirente, prezzoProvvisorio);
    }

    @Override
    public void insertItems(int idCarrello, int quantity, int ram, int spazioArchiviazione, int superProdottoId, String colore, String partitaIva) {
        formazioneCarrelloRepository.insert(idCarrello, quantity, ram, spazioArchiviazione, superProdottoId, colore, partitaIva);
    }
}