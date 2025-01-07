package com.lambda.demo.Control.GPR.AccessoRivenditore;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Exception.GPR.GPRException;
import com.lambda.demo.Service.GPR.Rivenditore.RivenditoreService;
import com.lambda.demo.Utility.SessionManager;
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

    /**
     * gestisce la richiesta di signup di un nuovo rivenditore
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */

    @RequestMapping(value = "/vendorSignup", method = RequestMethod.POST)
    public String signupRivenditore(HttpServletRequest req, HttpServletResponse res) throws Exception {

        String ragioneSociale = req.getParameter("ragioneSociale");
        String partitaIVA = req.getParameter("partitaIVA");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confermaPassword = req.getParameter("confermaPassword");

        try {
            rivenditoreService.signupRivenditore(ragioneSociale, partitaIVA, email, password, confermaPassword);
        }catch (GPRException gprException){
            throw new GPRException(gprException.getMessage());
        }
        SessionManager.setRivenditore(req, rivenditoreService.findByPartitaIva(partitaIVA));
        return "redirect:/vendorArea";
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

        try {
            rivenditoreService.loginRivenditore(email, password);
        }catch (GPRException gprException){
            throw new GPRException(gprException.getMessage());
        }

        SessionManager.setRivenditore(req, rivenditoreService.findByPartitaIva(email));
        return "redirect:/vendorArea";
    }

    @RequestMapping(value = "/vendorLogout", method = RequestMethod.POST)
    public String logoutRivenditore(HttpServletRequest req, HttpServletResponse res){
        RivenditoreEntity rivenditore = SessionManager.getRivenditore(req);

        rivenditoreService.updateRivenditore(rivenditore);

        req.getSession().invalidate();

        return "redirect:/";
    }

}