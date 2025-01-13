package com.lambda.demo.Service.GA.Ordine;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GA.Ordine.Composizione.ComposizioneEntity;
import com.lambda.demo.Entity.GA.Ordine.Composizione.ComposizioneEntityId;
import com.lambda.demo.Entity.GA.Ordine.OrdineEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GA.GAException;
import com.lambda.demo.Exception.GA.GestioneOrdini.*;
import com.lambda.demo.Repository.GA.Carrello.CarrelloRepository;
import com.lambda.demo.Repository.GA.Ordine.ComposizioneRepository;
import com.lambda.demo.Repository.GA.Ordine.OrdineRepository;
import com.lambda.demo.Repository.GC.Inserzione.InserzioneRepository;
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
public class OrdineServiceImpl implements OrdineService {
    @Autowired
    private ComposizioneRepository composizioneRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private AcquirenteRepository acquirenteRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private InserzioneRepository inserzioneRepository;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void cardCheckoutValidation(String destinatario, String indirizzo, String intestatario, String numeroCarta, String cvv, String scadenza) throws GAException {
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

    @Override
    public void lambdaCheckoutValidation(String destinatario, String indirizzo) throws GAException {
        if (!Validator.isValidReceiver(destinatario))
            throw new InvalidReceiverException("Destinatario non rispetta il formato richiesto!");

        if (!Validator.isValidAddress(indirizzo))
            throw new InvalidAddressException("Indirizzo non rispetta il formato richiesto!");

        AcquirenteEntity acquirente = SessionManager.getAcquirente(request);
        int saldo = acquirente.getSaldo();
        if ((saldo * 10) < (SessionManager.getCarrello(request).getPrezzoProvvisorio() + 3.99))
            throw new InsufficientLambdaPointsException("Errore: il saldo attuale di lambda points non è sufficiente per la finalizzazione dell'ordine. Si prega di inserire un metodo di pagamento alternativo.");

    }

    @Transactional
    @Override
    public void checkoutFinalization(String destinatario, String indirizzo, String intestatario, String numeroCarta, String cvv, String scadenza) throws GAException {
        boolean lambda = Boolean.parseBoolean(request.getParameter("lambda"));

        if (lambda && (!intestatario.isBlank() || !numeroCarta.isBlank() || !cvv.isBlank() || !scadenza.isBlank()))
            throw new GAException("Errore: Hai selezionato i Lambda Points come metodo di pagamento, ma alcuni campi relativi a un altro metodo di pagamento (carta di credito) risultano compilati. Verifica i dati e riprova. Potrebbe essere stato alterato il DOM della pagina.");

        if (lambda) lambdaCheckoutValidation(destinatario, indirizzo);
        if (!lambda) cardCheckoutValidation(destinatario, indirizzo, intestatario, numeroCarta, cvv, scadenza);

        AcquirenteEntity acquirente = SessionManager.getAcquirente(request);

        CarrelloEntity carrello = SessionManager.getCarrello(request);
        List<FormazioneCarrelloEntity> cartItems = carrello.getCarrelloItems();

        OrdineEntity ordineEntity = new OrdineEntity();
        ordineEntity.setAcquirente(acquirente);
        ordineEntity.setPrezzo(carrello.getPrezzoProvvisorio() + 3.99);
        ordineEntity.setStato("In esecuzione");
        ordineEntity.setDestinatario(destinatario);
        ordineEntity.setIndirizzoSpedizione(indirizzo);

        if (lambda) {
            int lambdaPoints = (int) Math.round(ordineEntity.getPrezzo() / 10.0);
            int idAcquirente = SessionManager.getAcquirente(request).getId();
            int updatedSaldo = SessionManager.getAcquirente(request).getSaldo() - lambdaPoints;

            SessionManager.getAcquirente(request).setSaldo(updatedSaldo);
            ordineEntity.setMetodoDiPagamento("lambda points");
            ordineEntity.setLambdaPointsSpesi(lambdaPoints);
            acquirenteRepository.updateSaldoLambdaPoints(updatedSaldo, idAcquirente);
        } else {
            ordineEntity.setMetodoDiPagamento("carta di debito");
            ordineEntity.setUltimeQuattroCifre(numeroCarta.substring(numeroCarta.length() - 4));
        }

        ordineRepository.save(ordineEntity);

        List<ComposizioneEntity> orderItems = new ArrayList<>();
        ComposizioneEntityId composizioneEntityId = new ComposizioneEntityId();
        for (FormazioneCarrelloEntity cartItem : cartItems) {
            composizioneEntityId.setIdOrdine(ordineEntity.getId());
            composizioneEntityId.setIdInserzione(cartItem.getInserzione().getId());

            ComposizioneEntity orderItem = new ComposizioneEntity();
            orderItem.setId(composizioneEntityId);
            orderItem.setOrdine(ordineEntity);
            orderItem.setInserzione(cartItem.getInserzione());
            orderItem.setQuantita(cartItem.getQuantita());
            //prezzoAcquisto di ordine contiene il prezzo di acquisto di un singolo articolo, indipendetemente dalla quantità specificata
            orderItem.setPrezzoAcquisto(cartItem.getInserzione().returnDiscountedPrice(acquirente.isPremium()));

            ordineEntity.addComposizione(orderItem);
            orderItems.add(orderItem);

            cartItem.getInserzione().setQuantita(cartItem.getInserzione().getQuantita() - orderItem.getQuantita());
            if (cartItem.getInserzione().getQuantita() == 0) {
                cartItem.getInserzione().setDisponibilita(false);
            }
            inserzioneRepository.updateQuantity(cartItem.getInserzione().getQuantita(), cartItem.getInserzione().isDisponibilita(), cartItem.getInserzione().getId().getIdProdotto().getSuperProdottoId(),
                    cartItem.getInserzione().getId().getIdProdotto().getColore(), cartItem.getInserzione().getId().getIdProdotto().getSpazioArchiviazione(), cartItem.getInserzione().getId().getIdProdotto().getRam());
        }


        composizioneRepository.saveAll(orderItems);

        cartItems.clear();
        carrello.setPrezzoProvvisorio(0);
        carrelloRepository.deleteCarrelloByAcquirente(acquirenteRepository.findByEmail(acquirente.getEmail()).getId());
        carrelloRepository.insert(acquirenteRepository.findByEmail(acquirente.getEmail()).getId(), carrello.getPrezzoProvvisorio());
        SessionManager.setCarrello(request, carrello);

        SessionManager.setAcquirente(request, acquirente);
    }


}