package com.lambda.demo.Control.GPR.AccountRivenditore;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Exception.GA.GestioneOrdini.InvalidAddressException;
import com.lambda.demo.Exception.GPR.GPRException;
import com.lambda.demo.Service.GPR.Rivenditore.RivenditoreService;
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
    public String vendorDataUpdate(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String ragioneSociale = req.getParameter("ragioneSociale");
        String indirizzo = req.getParameter("indirizzo");
        String passwordAttuale = req.getParameter("passwordAttuale");
        String nuovaPassword = req.getParameter("nuovaPassword");
        String confermaNuovaPassword = req.getParameter("confermaNuovaPassword");

        RivenditoreEntity rivenditoreEntity = SessionManager.getRivenditore(req);
        //fare controllo presenza sessione, non qui ma in un filtro o chi per esso
        try {
            rivenditoreEntity = rivenditoreService.updateVendorData(rivenditoreEntity, ragioneSociale, indirizzo, passwordAttuale, nuovaPassword, confermaNuovaPassword);
        }catch (GPRException | InvalidAddressException gprException) {
            throw new Exception(gprException.getMessage());
        }

        rivenditoreService.updateRivenditore(rivenditoreEntity);
        SessionManager.setRivenditore(req, rivenditoreEntity);

        return "redirect:/vendorArea";
    }
}
