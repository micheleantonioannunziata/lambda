package com.lambda.demo.Control.GPR.AccountRivenditore;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Service.GPR.Acquirente.RivenditoreService;
import com.lambda.demo.Utility.Encrypt;
import com.lambda.demo.Utility.SessionManager;
import com.lambda.demo.Utility.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

@Controller
public class AccountRivenditoreControl {
    @Autowired
    private RivenditoreService rivenditoreService;

    /**
     * gestisce la richiesta di modifica dei dati di un rivenditore
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    @RequestMapping(value="/vendorDataUpdate", method = RequestMethod.POST)
    public String vendorDataUpdate(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String ragioneSociale = req.getParameter("ragioneSociale");
        String indirizzo = req.getParameter("indirizzo");
        String passwordAttuale = req.getParameter("passwordAttuale");
        String nuovaPassword = req.getParameter("nuovaPassword");
        String confermaNuovaPassword = req.getParameter("confermaNuovaPassword");

        RivenditoreEntity rivenditoreEntity = SessionManager.getRivenditore(req);
        //fare controllo presenza sessione, non qui ma in un filtro o chi per esso

        //se non inserisco nulla in un campo di un form, viene passata una stringa vuota e NON null
        if (!indirizzo.isBlank() && !Validator.isValidAddress(indirizzo)) {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "L\' indirizzo non rispetta il formato richiesto.");
            return null;
        }

        if ((passwordAttuale.isBlank() && nuovaPassword.isBlank() && confermaNuovaPassword.isBlank()))
            ;
        else if (!passwordAttuale.isBlank() && !nuovaPassword.isBlank() && !confermaNuovaPassword.isBlank()){
            //se la password attuale non coincide con quella in utilizzo
            if (!Encrypt.encrypt(passwordAttuale).equals(rivenditoreEntity.getPassword())){
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "La password inserita non coincide con quella attuale.");
                return null;
            }

            //se la nuova password non rispetta il formato previsto
            if (!Validator.isValidPassword(nuovaPassword)) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "La nuova password non rispetta il formato richiesto.");
                return null;
            }

            //se password attuale e nuova password coincidono
            if (passwordAttuale.equals(nuovaPassword)) {
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "La nuova password e quella attuale coincidono.");
                return null;
            }

            //se nuova password e conferma password non coincidono
            if (!nuovaPassword.equals(confermaNuovaPassword)){
                res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Il campo nuova password non coincide con la conferma.");
                return null;
            }
        } else {
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "I campi relativi alle password non sono stati compilati nella loro interezza.");
            return null;
        }

        if (!ragioneSociale.isBlank())
            rivenditoreEntity.setRagioneSociale(ragioneSociale);

        if (!indirizzo.isBlank())
            rivenditoreEntity.setIndirizzo(indirizzo);

        if (!nuovaPassword.isBlank())
            rivenditoreEntity.setPassword(Encrypt.encrypt(nuovaPassword));

        return "vendorArea";
    }
}
