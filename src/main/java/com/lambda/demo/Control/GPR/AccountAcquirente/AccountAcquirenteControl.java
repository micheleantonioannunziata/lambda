package com.lambda.demo.Control.GPR.AccountAcquirente;

import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GA.GestioneOrdini.InvalidAddressException;
import com.lambda.demo.Exception.GPR.GPRException;
import com.lambda.demo.Service.GPR.Acquirente.AcquirenteService;
import com.lambda.demo.Utility.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountAcquirenteControl {
    @Autowired
    private AcquirenteService acquirenteService;

    /**
     * gestisce la richiesta di modifica dei dati di un acquirente
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    @RequestMapping(value="/purchaserDataUpdate", method = RequestMethod.POST)
    public String purchaserDataUpdate(HttpServletRequest req, HttpServletResponse res, RedirectAttributes redirectAttributes) throws Exception {
        String nome = req.getParameter("nome");
        String cognome = req.getParameter("cognome");
        String indirizzo = req.getParameter("indirizzo");
        String passwordAttuale = req.getParameter("passwordAttuale");
        String nuovaPassword = req.getParameter("nuovaPassword");
        String confermaNuovaPassword = req.getParameter("confermaNuovaPassword");

        AcquirenteEntity acquirenteEntity = SessionManager.getAcquirente(req);
        //fare controllo presenza sessione, non qui ma in un filtro o chi per esso

        try {
            acquirenteEntity = acquirenteService.updateAcquirenteData(acquirenteEntity, nome, cognome, indirizzo, passwordAttuale, nuovaPassword, confermaNuovaPassword);
        } catch (GPRException | InvalidAddressException gprException) {
            throw new Exception(gprException.getMessage());
        }

        acquirenteService.updateAcquirente(acquirenteEntity);
        SessionManager.setAcquirente(req, acquirenteEntity);

        redirectAttributes.addFlashAttribute("msg", "Modifica dati personali avvenuta con successo!");

        return "redirect:/userArea";
    }

    @RequestMapping(value = "/purchaserDeleteAccount", method = RequestMethod.POST)
    public String purchaserDeleteAccount(HttpServletRequest req, HttpServletResponse res) throws Exception {
        acquirenteService.deletePurchaserAccount(SessionManager.getAcquirente(req).getEmail());

        return "redirect:/";
    }
}