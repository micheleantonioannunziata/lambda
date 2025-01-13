package com.lambda.demo.Control.GPR;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;
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
public class AccessoRivenditoreControl {
    @Autowired
    private RivenditoreService rivenditoreService;

    /**
     * gestisce la richiesta di signup di un nuovo rivenditore
     *
     * @param req                oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes per meccanismo riscontri
     * @throws GPRException eccezione generica di GPR
     * @see HttpServletRequest
     * @see RedirectAttributes
     * @see GPRException
     */
    @RequestMapping(value = "/vendorSignup", method = RequestMethod.POST)
    public String signupRivenditore(HttpServletRequest req, RedirectAttributes redirectAttributes) throws GPRException {

        String ragioneSociale = req.getParameter("ragioneSociale");
        String partitaIVA = req.getParameter("partitaIVA");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confermaPassword = req.getParameter("confermaPassword");

        try {
            rivenditoreService.signupRivenditore(ragioneSociale, partitaIVA, email, password, confermaPassword);
        } catch (GPRException gprException) {
            throw new GPRException(gprException.getMessage());
        }

        SessionManager.setRivenditore(req, rivenditoreService.findByPartitaIva(partitaIVA));
        redirectAttributes.addFlashAttribute("msg", "Registrazione effettuata con successo!");
        return "redirect:/vendorArea";
    }

    /**
     * gestisce la richiesta di accesso di un rivenditore
     *
     * @param req                oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes per meccanismo riscontri
     * @throws GPRException eccezione generica di GPR
     * @see HttpServletRequest
     * @see RedirectAttributes
     * @see GPRException
     */
    @RequestMapping(value = "/vendorLogin", method = RequestMethod.POST)
    public String loginRivenditore(HttpServletRequest req, RedirectAttributes redirectAttributes) throws GPRException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        try {
            rivenditoreService.loginRivenditore(email, password);
        } catch (GPRException gprException) {
            throw new GPRException(gprException.getMessage());
        }

        redirectAttributes.addFlashAttribute("msg", "Login effettuato con successo!");

        SessionManager.setRivenditore(req, rivenditoreService.findByEmail(email));
        return "redirect:/vendorArea";
    }

    /**
     * gestisce la logica di logout di un rivenditore
     *
     * @param req                oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes per meccanismo riscontri
     * @see HttpServletRequest
     * @see RedirectAttributes
     */
    @RequestMapping(value = "/vendorLogout", method = RequestMethod.POST)
    public String logoutRivenditore(HttpServletRequest req, RedirectAttributes redirectAttributes) {
        RivenditoreEntity rivenditore = SessionManager.getRivenditore(req);

        rivenditoreService.updateRivenditore(rivenditore);

        req.getSession().invalidate();

        redirectAttributes.addFlashAttribute("msg", "Logout effettuato con successo!");

        return "redirect:/";
    }

}