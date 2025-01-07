package com.lambda.demo.Service.GA.Ordine;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GA.Ordine.Composizione.ComposizioneEntity;
import com.lambda.demo.Entity.GA.Ordine.Composizione.ComposizioneEntityId;
import com.lambda.demo.Entity.GA.Ordine.OrdineEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GA.GAException;
import com.lambda.demo.Exception.GA.GestioneOrdini.*;
import com.lambda.demo.Repository.GA.Ordine.ComposizioneRepository;
import com.lambda.demo.Repository.GA.Ordine.OrdineRepository;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Utility.SessionManager;
import com.lambda.demo.Utility.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrdineServiceImpl implements OrdineService{
    @Autowired
    private ComposizioneRepository composizioneRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private AcquirenteRepository acquirenteRepository;


    @Override
    public void checkout(String destinatario, String indirizzo, String intestatario, String numeroCarta, String cvv, String scadenza) throws GAException {
        if (!Validator.isValidReceiver(destinatario))
            throw new InvalidReceiverException("Destinatario non rispetta il formato richiesto!");

        if (!Validator.isValidAddress(indirizzo))
            throw new InvalidAddressException("Indirizzo non rispetta il formato richiesto!");

        if (!Validator.isValidCardHolder(intestatario))
            throw new InvalidCardHolderException("Intestatario carta non rispetta il formato richiesto!");

        if (!Validator.isValidCardNumber(numeroCarta))
            throw new InvalidCardNumberException("Numero carta non rispetta il formato richiesto!");

        if (!Validator.isValidCVV(cvv))
            throw new InvalidCVVException("CVV non rispetta il formato richiesto!");

        if (!Validator.isValidExpirationDate(scadenza))
            throw new InvalidExpirationDateException("Scadenza della carta non rispetta il formato richiesto!");


    }

    @Transactional
    @Override
    public void checkoutFinalization(boolean lambda, String destinatario, String indirizzo, String numeroCarta, HttpServletRequest req) {
        CarrelloEntity carrelloEntity = SessionManager.getCarrello(req);
        List<FormazioneCarrelloEntity> cartItems = carrelloEntity.getCarrelloItems();


        System.out.println(destinatario);
        System.out.println(indirizzo);
        System.out.println(numeroCarta);

        OrdineEntity ordineEntity = new OrdineEntity();
        ordineEntity.setAcquirente(SessionManager.getAcquirente(req));
        ordineEntity.setPrezzo(carrelloEntity.getPrezzoProvvisorio() + 3.99); //da rivedere
        ordineEntity.setStato("in esecuzione");
        ordineEntity.setDestinatario(destinatario);
        ordineEntity.setIndirizzoSpedizione(indirizzo);



        if (lambda) {
            int lambdaPoints = (int) Math.round(ordineEntity.getPrezzo() / 10.0);
            int idAcquirente = SessionManager.getAcquirente(req).getId();
            int updatedSaldo = SessionManager.getAcquirente(req).getSaldo() - lambdaPoints;

            SessionManager.getAcquirente(req).setSaldo(updatedSaldo);
            ordineEntity.setMetodoDiPagamento("lambda points");
            ordineEntity.setLambdaPointsSpesi(lambdaPoints);

            acquirenteRepository.updateSaldoLambdaPoints(updatedSaldo, idAcquirente);
        }else{
            ordineEntity.setMetodoDiPagamento("carta di debito");
            ordineEntity.setUltimeQuattroCifre(numeroCarta.substring(numeroCarta.length() - 4));
        }


        ordineRepository.save(ordineEntity);

        List<ComposizioneEntity> orderItems = new ArrayList<>();
        ComposizioneEntityId composizioneEntityId = new ComposizioneEntityId();

        for (FormazioneCarrelloEntity cartItem: cartItems){
            composizioneEntityId.setIdOrdine(ordineEntity.getId());
            composizioneEntityId.setIdInserzione(cartItem.getInserzione().getId());

            ComposizioneEntity orderItem = new ComposizioneEntity();
            orderItem.setId(composizioneEntityId);
            orderItem.setOrdine(ordineEntity);
            orderItem.setInserzione(cartItem.getInserzione());
            orderItem.setQuantita(cartItem.getQuantita());
            //prezzoAcquisto di ordine contiene il prezzo di acquisto di un singolo articolo, indipendetemente dalla quantità specificata
            orderItem.setPrezzoAcquisto(cartItem.getInserzione().getPrezzoBase());

            ordineEntity.addComposizione(orderItem);
            orderItems.add(orderItem);
        }


        composizioneRepository.saveAll(orderItems);
        cartItems.clear();
        carrelloEntity.setPrezzoProvvisorio(0);

        AcquirenteEntity acquirente = SessionManager.getAcquirente(req);
        System.out.println(acquirente.getId());


    }
}
