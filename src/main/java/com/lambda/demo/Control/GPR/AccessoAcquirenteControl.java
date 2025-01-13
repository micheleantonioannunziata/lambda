package com.lambda.demo.Control.GPR;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntity;
import com.lambda.demo.Entity.GA.Carrello.FormazioneCarrello.FormazioneCarrelloEntityId;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GPR.GPRException;
import com.lambda.demo.Service.GA.Carrello.CarrelloService;
import com.lambda.demo.Service.GPR.Acquirente.AcquirenteService;
import com.lambda.demo.Utility.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class AccessoAcquirenteControl {
    @Autowired
    private AcquirenteService acquirenteService;

    @Autowired
    private CarrelloService carrelloService;

    /**
     * gestisce la richiesta di signup di un nuovo acquirente
     *
     * @param req                oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes per meccanismo riscontri
     * @throws GPRException eccezione generica di GPR
     * @see HttpServletRequest
     * @see HttpServletResponse
     * @see GPRException
     */
    @RequestMapping(value = "/purchaserSignup", method = RequestMethod.POST)
    public String signupAcquirente(HttpServletRequest req, RedirectAttributes redirectAttributes) throws GPRException {
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confermaPassword = req.getParameter("confermaPassword");

        try {
            acquirenteService.signupAcquirente(nome, cognome, email, password, confermaPassword);
        } catch (GPRException e) {
            throw new GPRException(e.getMessage());
        }

        SessionManager.setAcquirente(req, acquirenteService.getAcquirente(email));

        CarrelloEntity carrelloEntity = new CarrelloEntity();
        carrelloEntity.setAcquirente(SessionManager.getAcquirente(req));
        SessionManager.setCarrello(req, carrelloEntity);

        redirectAttributes.addFlashAttribute("msg", "Registrazione avvenuta con successo!");

        return "redirect:/userArea";
    }

    /**
     * gestisce la richiesta di accesso di un acquirente
     *
     * @param req                oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes per meccanismo riscontri
     * @throws GPRException eccezione generica di GPR
     * @see HttpServletRequest
     * @see RedirectAttributes
     * @see GPRException
     */

    @RequestMapping(value = "/purchaserLogin", method = RequestMethod.POST)
    public String loginAcquirente(HttpServletRequest req, RedirectAttributes redirectAttributes) throws GPRException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");


        try {
            acquirenteService.loginAcquirente(email, password);
        } catch (GPRException gprException) {
            throw new GPRException(gprException.getMessage());
        }

        AcquirenteEntity acquirente = acquirenteService.getAcquirente(email);

        // bean acquirente in sessione
        SessionManager.setAcquirente(req, acquirente);

        CarrelloEntity carrelloEntity = carrelloService.getCartByUser(acquirente.getId());
        if (carrelloEntity == null) carrelloEntity = new CarrelloEntity();

        // sincronizzazione carrello db - sessione
        SessionManager.setCarrello(req, carrelloEntity);

        redirectAttributes.addFlashAttribute("msg", "Login effettuato con successo!");

        return "redirect:/userArea";
    }

    /**
     * gestisce la richiesta di logout di un acquirente
     *
     * @param req                oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes per meccanismo riscontri
     * @see HttpServletRequest
     * @see RedirectAttributes
     */
    @Transactional
    @RequestMapping(value = "/purchaserLogout", method = RequestMethod.POST)
    public String logout(HttpServletRequest req, RedirectAttributes redirectAttributes) {
        AcquirenteEntity acquirente = SessionManager.getAcquirente(req);

        CarrelloEntity carrelloEntity = SessionManager.getCarrello(req);

        if (carrelloEntity == null)
            carrelloEntity = new CarrelloEntity();

        carrelloService.deleteCartByUser(acquirente.getId());
        carrelloService.insertCartByUser(acquirente.getId(), carrelloEntity.getPrezzoProvvisorio());


        List<FormazioneCarrelloEntity> cartItems = carrelloEntity.getCarrelloItems();

        for (FormazioneCarrelloEntity cartItem : cartItems) {
            FormazioneCarrelloEntityId id = new FormazioneCarrelloEntityId(carrelloService.getCartByUser(SessionManager.getAcquirente(req).getId()).getId(), cartItem.getInserzione().getId());
            cartItem.setId(id);
            carrelloService.insertItems(cartItem.getId().getIdCarrello(), cartItem.getQuantita(), cartItem.getInserzione().getProdotto().getId().getRam(), cartItem.getInserzione().getProdotto().getId().getSpazioArchiviazione(), cartItem.getInserzione().getProdotto().getSuperProdotto().getId(), cartItem.getInserzione().getProdotto().getId().getColore(), cartItem.getInserzione().getRivenditore().getPartitaIva());
        }

        req.getSession().invalidate();

        redirectAttributes.addFlashAttribute("msg", "Logout effettuato con successo!");

        return "redirect:/";
    }
}
