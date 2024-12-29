package com.lambda.demo.Control.GPR.AccessoAcquirente;

import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Service.GPR.Acquirente.AcquirenteService;
import com.lambda.demo.Utility.SessionManager;
import com.lambda.demo.Utility.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccessoAcquirenteControl {
    @Autowired
    private AcquirenteService acquirenteService;

    @Autowired
    private AcquirenteRepository acquirenteRepository;

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

        if (!Validator.isValidName(nome)){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il nome non rispetta il formato richiesto.");
            return null;
        }

        if (!Validator.isValidSurname(cognome)){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il cognome non rispetta il formato richiesto.");
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

        if (!password.equals(confermaPassword)){
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le password non coincidono.");
            return null;
        }

        acquirenteService.signupAcquirente(nome, cognome, email, password);
        SessionManager.setAcquirente(req, acquirenteRepository.findByEmail(email));
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
        SessionManager.setAcquirente(req, acquirenteRepository.findByEmail(email));
        return "userArea";
    }
}

