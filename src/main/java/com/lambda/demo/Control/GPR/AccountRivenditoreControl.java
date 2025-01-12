package com.lambda.demo.Control.GPR;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Exception.GA.GestioneOrdini.InvalidAddressException;
import com.lambda.demo.Exception.GPR.GPRException;
import com.lambda.demo.Service.GPR.Rivenditore.RivenditoreService;
import com.lambda.demo.Utility.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AccountRivenditoreControl {
    @Autowired
    private RivenditoreService rivenditoreService;

    /**
     * gestisce la richiesta di modifica dati di un rivenditore
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes per meccanismo riscontri
     * @throws Exception eccezione generica
     * @see HttpServletRequest
     * @see RedirectAttributes
     * @see Exception
     */
    @RequestMapping(value="/vendorDataUpdate", method = RequestMethod.POST)
    public String vendorDataUpdate(HttpServletRequest req, RedirectAttributes redirectAttributes) throws Exception {
        String ragioneSociale = req.getParameter("ragioneSociale");
        String indirizzo = req.getParameter("indirizzo");
        String passwordAttuale = req.getParameter("passwordAttuale");
        String nuovaPassword = req.getParameter("nuovaPassword");
        String confermaNuovaPassword = req.getParameter("confermaNuovaPassword");

        RivenditoreEntity rivenditoreEntity = SessionManager.getRivenditore(req);

        try {
            rivenditoreEntity = rivenditoreService.updateVendorData(rivenditoreEntity, ragioneSociale, indirizzo, passwordAttuale, nuovaPassword, confermaNuovaPassword);
        } catch (GPRException | InvalidAddressException gprException) {
            throw new Exception(gprException.getMessage());
        }

        rivenditoreService.updateRivenditore(rivenditoreEntity);
        SessionManager.setRivenditore(req, rivenditoreEntity);

        redirectAttributes.addFlashAttribute("msg", "Modifica dati aziendali effettuata con successo!");

        return "redirect:/vendorArea";
    }

    /**
     * gestisce la richiesta di cancellazione dell'account di un rivenditore
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes per meccanismo riscontri
     * @see HttpServletRequest
     * @see RedirectAttributes
     */
    @RequestMapping(value = "/deleteVendorAccount", method = RequestMethod.POST)
    public String deleteVendorAccount(HttpServletRequest req, RedirectAttributes redirectAttributes) {
        rivenditoreService.deleteVendorAccount(SessionManager.getRivenditore(req).getEmail());

        redirectAttributes.addFlashAttribute("msg", "Account aziendale eliminato con successo!");

        return "redirect:/";
    }
}