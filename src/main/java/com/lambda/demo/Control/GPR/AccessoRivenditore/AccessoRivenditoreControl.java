package com.lambda.demo.Control.GPR.AccessoRivenditore;

import com.lambda.demo.Repository.GPR.RivenditoreRepository;
import com.lambda.demo.Service.GPR.Rivenditore.RivenditoreService;
import com.lambda.demo.Utility.SessionManager;
import com.lambda.demo.Utility.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccessoRivenditoreControl {
    @Autowired
    private RivenditoreService rivenditoreService;

    @Autowired
    private RivenditoreRepository rivenditoreRepository;

    /**
     * gestisce la richiesta di signup di un nuovo rivenditore
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */

    @RequestMapping(value = "/vendorSignup", method = RequestMethod.POST)
    public String signup(HttpServletRequest req, HttpServletResponse res) throws Exception {
        //la notazione RequestBody mi permette di salvare tutti i parametri inviati alla servlet nella stringa singUp

        String ragioneSociale = req.getParameter("ragioneSociale");
        String partitaIVA = req.getParameter("partitaIVA");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confermaPassword = req.getParameter("confermaPassword");

        if (ragioneSociale == null || partitaIVA == null || email == null || password == null || confermaPassword == null) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tutti i campi sono obbligatori.");
            return null;
        }

        if (!Validator.isValidPartitaIVA(partitaIVA)){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "La partita IVA non rispetta il formato richiesto.");
            return null;
        }

        if (!Validator.isValidEmail(email)){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "L'email non rispetta il formato richiesto.");
            return null;
        }

        /*System.out.println(password);*/
        if (!Validator.isValidPassword(password)){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "La password non rispetta il formato richiesto.");
            return null;
        }

        if (!password.equals(confermaPassword)){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le password non coincidono.");
            return null;
        }

        rivenditoreService.signupRivenditore(ragioneSociale, partitaIVA, email, password);
        SessionManager.setRivenditore(req, rivenditoreRepository.findByEmail(email));
        return "vendorArea";
    }

    /**
     * gestisce la richiesta di accesso da parte di un rivenditore
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */

    @RequestMapping(value = "/vendorLogin", method = RequestMethod.POST)
    public String loginRivenditore(HttpServletRequest req, HttpServletResponse res) throws Exception {
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

        rivenditoreService.loginRivenditore(email, password);
        SessionManager.setRivenditore(req, rivenditoreRepository.findByEmail(email));
        return"vendorArea";
    }
}