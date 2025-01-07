package com.lambda.demo.Control.GPR.AccessoAcquirente;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntityId;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GPR.GPRException;
import com.lambda.demo.Repository.GA.Carrello.CarrelloRepository;
import com.lambda.demo.Repository.GA.Carrello.FormazioneCarrelloRepository;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Service.GPR.Acquirente.AcquirenteService;
import com.lambda.demo.Utility.SessionManager;
import com.lambda.demo.Utility.Validator;
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
    private AcquirenteRepository acquirenteRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private FormazioneCarrelloRepository formazioneCarrelloRepository;

    @Autowired
    private EntityManager entityManager;

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
        //la notazione RequestBody mi permette di salvare tutti i parametri inviati alla servlet nella stringa singUp

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

        //se tutto va a buon fine, bisogna associare all'acquirente un nuovo carrello
        SessionManager.setAcquirente(req, acquirenteRepository.findByEmail(email));

        CarrelloEntity carrelloEntity = new CarrelloEntity();
        carrelloEntity.setAcquirente(SessionManager.getAcquirente(req));
        SessionManager.setCarrello(req, carrelloEntity);


        return "userArea";
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
        if (email == null || password == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tutti i campi sono obbligatori.");
            return null;
        }


        if (!Validator.isValidEmail(email)){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "L'email non rispetta il formato richiesto.");
            return null;
        }

        if (!Validator.isValidPassword(password)){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "La password non rispetta il formato richiesto.");
            return null;
        }

        acquirenteService.loginAcquirente(email, password);

        //setto il bean dell'acquirente nella sessione
        SessionManager.setAcquirente(req, acquirenteRepository.findByEmail(email));

        //anche se il carrello è vuoto, già c'è sul database
        CarrelloEntity carrelloEntity;
        //mi prendo il carrello dal db
        carrelloEntity = carrelloRepository.findByAcquirente(SessionManager.getAcquirente(req));
        //setto il carrello preso dal db nella sessione
        SessionManager.setCarrello(req, carrelloEntity);

        return "userArea";
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

        //chiamata a save - merge - in modo tale da salvare eventuali informazioni cambiate durante l'accesso al sito
        //acquirenteRepository.save(acquirente);

        //prendo il carrello dalla sessione
        CarrelloEntity carrelloEntity = SessionManager.getCarrello(req);


        if(carrelloEntity == null) {
            System.out.println("Carrello non presente in sessione - .");
            carrelloEntity = new CarrelloEntity();
        }
        //rimuovo dal db l'attuale carrello salvato
        carrelloRepository.deleteCarrelloByAcquirente(SessionManager.getAcquirente(req).getId());

        //inserisco nel db il carrello presente in sessione
        carrelloRepository.insert(SessionManager.getAcquirente(req).getId(), carrelloEntity.getPrezzoProvvisorio());


        //Salvo ogni elemento della lista FormazioneCarrelloEntity associata al carrello in sessione
        List<FormazioneCarrelloEntity> cartItems = carrelloEntity.getCarrelloItems();
        for(FormazioneCarrelloEntity cartItem : cartItems){
            FormazioneCarrelloEntityId id = new FormazioneCarrelloEntityId();
            id.setIdCarrello(carrelloRepository.findByAcquirente(SessionManager.getAcquirente(req)).getId());
            id.setIdInserzione(cartItem.getInserzione().getId());
            cartItem.setId(id);
            formazioneCarrelloRepository.insert(cartItem.getId().getIdCarrello(), cartItem.getQuantita(), cartItem.getInserzione().getProdotto().getId().getRam(), cartItem.getInserzione().getProdotto().getId().getSpazioArchiviazione(), cartItem.getInserzione().getProdotto().getSuperProdotto().getId(), cartItem.getInserzione().getProdotto().getId().getColore(), cartItem.getInserzione().getRivenditore().getPartitaIva());
        }



        //chiudo la sessione
        req.getSession().invalidate();


        return "index";
    }


}

