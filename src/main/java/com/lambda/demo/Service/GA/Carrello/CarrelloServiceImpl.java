package com.lambda.demo.Service.GA.Carrello;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntityId;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Repository.GA.Carrello.CarrelloRepository;
import com.lambda.demo.Repository.GA.Carrello.FormazioneCarrelloRepository;
import com.lambda.demo.Repository.GC.Inserzione.InserzioneRepository;
import com.lambda.demo.Repository.GC.Prodotto.ProdottoRepository;
import com.lambda.demo.Repository.GC.SuperProdottoRepository;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Repository.GPR.RivenditoreRepository;
import com.lambda.demo.Utility.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarrelloServiceImpl implements CarrelloService{
    @Autowired
    private InserzioneRepository inserzioneRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private FormazioneCarrelloRepository formazioneCarrelloRepository;


    @Override
    public void addToCart(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione, String colore, HttpServletRequest req) throws Exception {
        ProdottoEntityId prodottoEntityId = new ProdottoEntityId();
        prodottoEntityId.setColore(colore);
        prodottoEntityId.setRam(ram);
        prodottoEntityId.setSpazioArchiviazione(spazioArchiviazione);
        prodottoEntityId.setSuperProdottoId(idSuperProdotto);

        InserzioneEntityId inserzioneEntityId = new InserzioneEntityId();
        inserzioneEntityId.setPartitaIvaRivenditore(partitaIvaRivenditore);
        inserzioneEntityId.setIdProdotto(prodottoEntityId);


        InserzioneEntity inserzioneEntity = inserzioneRepository.findById(inserzioneEntityId).get();
        System.out.println(inserzioneEntity.getPrezzoBase());


        CarrelloEntity carrelloEntity = SessionManager.getCarrello(req);

        List<FormazioneCarrelloEntity> cartItems = carrelloEntity.getCarrelloItems();

        if(cartItems == null) cartItems = new ArrayList<>();
        FormazioneCarrelloEntityId formazioneCarrelloEntityId = new FormazioneCarrelloEntityId();
        formazioneCarrelloEntityId.setIdCarrello(carrelloEntity.getId());
        formazioneCarrelloEntityId.setIdInserzione(inserzioneEntityId);
        if(cartItems.isEmpty()){
            FormazioneCarrelloEntity cartItem = new FormazioneCarrelloEntity();
            cartItem.setId(formazioneCarrelloEntityId);
            cartItem.setCarrello(carrelloEntity);
            cartItem.setInserzione(inserzioneEntity);
            cartItem.setQuantita(1);
        }else{
            boolean isNotTheSame = false;
            for(FormazioneCarrelloEntity cartItem : cartItems){
                if(cartItem.getInserzione().equals(inserzioneEntity)) cartItem.setQuantita(cartItem.getQuantita() + 1);
                else isNotTheSame = true;
            }
            if(isNotTheSame){
                FormazioneCarrelloEntity cartItem = new FormazioneCarrelloEntity();
                cartItem.setId(formazioneCarrelloEntityId);
                cartItem.setCarrello(carrelloEntity);
                cartItem.setInserzione(inserzioneEntity);
                cartItem.setQuantita(1);
            }
        }
        carrelloEntity.setPrezzoProvvisorio(carrelloEntity.getPrezzoProvvisorio() + inserzioneEntity.getPrezzoBase());

        SessionManager.setCarrello(req, carrelloEntity);
    }

    @Override
    public void removeFromCart(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione, String colore, HttpServletRequest req) throws Exception {
        CarrelloEntity carrelloEntity = SessionManager.getCarrello(req);
        List<FormazioneCarrelloEntity> cartItems = carrelloEntity.getCarrelloItems();

        ProdottoEntityId prodottoEntityId = new ProdottoEntityId();
        prodottoEntityId.setSuperProdottoId(idSuperProdotto);
        prodottoEntityId.setRam(ram);
        prodottoEntityId.setSpazioArchiviazione(spazioArchiviazione);
        prodottoEntityId.setColore(colore);

        if (!prodottoRepository.findById(prodottoEntityId).isPresent())
            throw new Exception();

        InserzioneEntityId inserzioneEntityId = new InserzioneEntityId();
        inserzioneEntityId.setIdProdotto(prodottoEntityId);
        inserzioneEntityId.setPartitaIvaRivenditore(partitaIvaRivenditore);

        if (!inserzioneRepository.findById(inserzioneEntityId).isPresent())
            throw new Exception();

        InserzioneEntity inserzioneEntity = inserzioneRepository.findById(inserzioneEntityId).get();

        boolean isPresent = false;
        int index = 0;
        for (FormazioneCarrelloEntity item: cartItems){
            if (item.getInserzione().equals(inserzioneEntity)) {
                isPresent = true;
                carrelloEntity.setPrezzoProvvisorio(carrelloEntity.getPrezzoProvvisorio()-
                        item.getInserzione().getPrezzoBase()*item.getQuantita());
                break;
            }
            index++;
        }

        if (!isPresent) {
            System.out.println("Nun nge sta");
            throw new Exception("Prodotto non presente nel carrello");
        } else {
            System.out.println("ci siamo fratm, sempre presenti");
            cartItems.remove(index);
        }

        SessionManager.setCarrello(req, carrelloEntity);


    }

    @Override
    public void updateQuantity(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione, String colore, int quantity, HttpServletRequest req) throws Exception {
        CarrelloEntity carrelloEntity = SessionManager.getCarrello(req);
        List<FormazioneCarrelloEntity> cartItems = carrelloEntity.getCarrelloItems();

        ProdottoEntityId prodottoEntityId = new ProdottoEntityId();
        prodottoEntityId.setSuperProdottoId(idSuperProdotto);
        prodottoEntityId.setRam(ram);
        prodottoEntityId.setSpazioArchiviazione(spazioArchiviazione);
        prodottoEntityId.setColore(colore);

        if (!prodottoRepository.findById(prodottoEntityId).isPresent())
            throw new Exception();

        InserzioneEntityId inserzioneEntityId = new InserzioneEntityId();
        inserzioneEntityId.setIdProdotto(prodottoEntityId);
        inserzioneEntityId.setPartitaIvaRivenditore(partitaIvaRivenditore);

        if (!inserzioneRepository.findById(inserzioneEntityId).isPresent())
            throw new Exception();

        InserzioneEntity inserzioneEntity = inserzioneRepository.findById(inserzioneEntityId).get();


        int index = 0;
        boolean isPresent = false;
        for(FormazioneCarrelloEntity item: cartItems){
            if(item.getInserzione().equals(inserzioneEntity)){
                isPresent = true;
                break;
            }
            index++;
        }

        if(!isPresent) {throw new Exception("Prodotto non presente nel carrello");}

        int oldQuantity = carrelloEntity.getCarrelloItems().get(index).getQuantita();

        cartItems.get(index).setQuantita(quantity);

        double prezzoBase = carrelloEntity.getCarrelloItems().get(index).getInserzione().getPrezzoBase();
        double oldTotal = oldQuantity * prezzoBase;
        double newTotal = quantity * prezzoBase;

        carrelloEntity.setPrezzoProvvisorio(carrelloEntity.getPrezzoProvvisorio() - oldTotal + newTotal);

        SessionManager.setCarrello(req, carrelloEntity);
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