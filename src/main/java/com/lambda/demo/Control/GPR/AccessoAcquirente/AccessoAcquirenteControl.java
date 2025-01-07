package com.lambda.demo.Control.GPR.AccessoAcquirente;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntityId;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GPR.GPRException;
import com.lambda.demo.Repository.GA.Carrello.CarrelloRepository;
import com.lambda.demo.Repository.GA.Carrello.FormazioneCarrelloRepository;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Service.GA.Carrello.CarrelloService;
import com.lambda.demo.Service.GPR.Acquirente.AcquirenteService;
import com.lambda.demo.Utility.SessionManager;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class AccessoAcquirenteControl {
    @Autowired
    private AcquirenteService acquirenteService;

    @Autowired
    private CarrelloService carrelloService;

    /**
     * gestisce la richiesta di signup di un nuovo acquirente
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    @RequestMapping(value = "/purchaserSignup", method = RequestMethod.POST)
    public String signupAcquirente(HttpServletRequest req, HttpServletResponse res) throws Exception {

        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confermaPassword = req.getParameter("confermaPassword");


        if (nome == null || cognome == null || email == null || password == null || confermaPassword == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tutti i campi sono obbligatori.");
            return null;
        }


        try {
            acquirenteService.signupAcquirente(nome, cognome, email, password, confermaPassword);
        }catch (GPRException e){
            throw new GPRException(e.getMessage());
        }



        SessionManager.setAcquirente(req, acquirenteService.getAcquirente(email));

        //se tutto va a buon fine, bisogna associare all'acquirente un nuovo carrello
        CarrelloEntity carrelloEntity = new CarrelloEntity();
        carrelloEntity.setAcquirente(SessionManager.getAcquirente(req));
        SessionManager.setCarrello(req, carrelloEntity);

        return "redirect:/userArea";
    }

    /**
     * gestisce la richiesta di accesso da parte di un acquirente
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */

    @RequestMapping(value = "/purchaserLogin", method = RequestMethod.POST)
    public String loginAcquirente(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String email = req.getParameter("email");
        String password = req.getParameter("password");


        try {
            acquirenteService.loginAcquirente(email, password);
        }catch (GPRException gprException){
            throw new GPRException(gprException.getMessage());
        }

        AcquirenteEntity acquirente = acquirenteService.getAcquirente(email);

        //setto il bean dell'acquirente nella sessione
        SessionManager.setAcquirente(req, acquirente);

        //anche se il carrello è vuoto, già c'è sul database
        CarrelloEntity carrelloEntity = carrelloService.getCartByUser(acquirente.getId());
        if (carrelloEntity == null) carrelloEntity = new CarrelloEntity();

        //setto il carrello preso dal db nella sessione
        SessionManager.setCarrello(req, carrelloEntity);

        return "redirect:/userArea";
    }

    /**
     * gestisce la richiesta di logout da parte di un acquirente
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    @Transactional
    @RequestMapping(value = "/purchaserLogout", method = RequestMethod.GET)
    public String logout(HttpServletRequest req, HttpServletResponse res) {
        AcquirenteEntity acquirente = SessionManager.getAcquirente(req);


        //prendo il carrello dalla sessione
        CarrelloEntity carrelloEntity = SessionManager.getCarrello(req);


        if(carrelloEntity == null) {
            carrelloEntity = new CarrelloEntity();
        }
        //rimuovo dal db l'attuale carrello salvato
        carrelloService.deleteCartByUser(acquirente.getId());

        //inserisco nel db il carrello presente in sessione
        carrelloService.insertCartByUser(acquirente.getId(), carrelloEntity.getPrezzoProvvisorio());


        //Salvo ogni elemento della lista FormazioneCarrelloEntity associata al carrello in sessione
        List<FormazioneCarrelloEntity> cartItems = carrelloEntity.getCarrelloItems();
        for(FormazioneCarrelloEntity cartItem : cartItems){
            FormazioneCarrelloEntityId id = new FormazioneCarrelloEntityId(carrelloService.getCartByUser(SessionManager.getAcquirente(req).getId()).getId(), cartItem.getInserzione().getId());
            cartItem.setId(id);
            carrelloService.insertItems(cartItem.getId().getIdCarrello(), cartItem.getQuantita(), cartItem.getInserzione().getProdotto().getId().getRam(), cartItem.getInserzione().getProdotto().getId().getSpazioArchiviazione(), cartItem.getInserzione().getProdotto().getSuperProdotto().getId(), cartItem.getInserzione().getProdotto().getId().getColore(), cartItem.getInserzione().getRivenditore().getPartitaIva());
        }

        //chiudo la sessione
        req.getSession().invalidate();

        return "redirect:/";
    }


}

