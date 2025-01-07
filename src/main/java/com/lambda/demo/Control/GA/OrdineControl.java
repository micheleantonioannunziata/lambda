package com.lambda.demo.Control.GA;

import com.lambda.demo.Exception.GA.GAException;
import com.lambda.demo.Service.GA.Ordine.OrdineService;
import com.lambda.demo.Utility.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class OrdineControl {
    @Autowired
    private OrdineService ordineService;
    /**
     * gestisce la logica relativa all'aggiunta delle informazioni necessarie per completare un ordine
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    @RequestMapping(value="/checkoutInfo", method= RequestMethod.POST)
    public String processCheckoutInfo(HttpServletRequest req, HttpServletResponse res, Model model) throws Exception {
        String destinatario = req.getParameter("destinatario");
        String indirizzo = req.getParameter("indirizzo");
        int conversionRate = 10;

        String lambdaFlag = req.getParameter("lambdaFlag");
        if (!(lambdaFlag.equals("true") || lambdaFlag.equals("false"))){
            throw new Exception("DOM modificato");
        }
        boolean lambda;
        lambda = Boolean.parseBoolean(lambdaFlag);

        String intestatarioCarta = req.getParameter("intestatarioCarta");
        String numeroCarta = req.getParameter("numeroCarta");
        String cvv = req.getParameter("CVV");
        String scadenza = req.getParameter("scadenza");



        if (intestatarioCarta != null && numeroCarta != null && cvv != null && scadenza != null && !lambda) {
            try {
                ordineService.checkout(destinatario, indirizzo, intestatarioCarta, numeroCarta, cvv, scadenza);
            }catch (GAException gaException){
                throw new GAException(gaException.getMessage());
            }

            model.addAttribute("intestatarioCarta", intestatarioCarta);
            model.addAttribute("numeroCarta", numeroCarta);
            model.addAttribute("cvv", cvv);
            model.addAttribute("scadenza", scadenza);
        }else if (intestatarioCarta == null && numeroCarta == null && cvv == null && scadenza == null && lambda){
            // controllo presente anche nello use-case in cui viene confrontato il saldo attuale con il totale del carrello
            if (SessionManager.getAcquirente(req).getSaldo()*conversionRate < (SessionManager.getCarrello(req).getPrezzoProvvisorio() + 3.99))
                throw new Exception("Saldo lambda points insufficiente per concretizzare l'ordine. Si prega di inserire un metodo di pagamento alternativo");

        }



        model.addAttribute("destinatario", destinatario);
        model.addAttribute("indirizzo", indirizzo);
        model.addAttribute("lambda", lambda);


        return "checkoutSummary";
    }

    /**
     * gestisce la logica relativa alla finalizzazione dell'ordine
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    @Transactional //questo perché o si rimuove dal carrello e si aggiunge all'ordine insieme o non si fa niente
    @RequestMapping(value = "/checkoutFinalization", method = RequestMethod.POST)
    public String checkoutFinalization(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String lambda = req.getParameter("lambda");
        String destinatario = req.getParameter("destinatario");
        String indirizzo = req.getParameter("indirizzoSpedizione");
        String numeroCarta = req.getParameter("numeroCarta");

        if (!lambda.equals("true") && !lambda.equals("false"))
            throw new Exception("DOM modificato");


        String action = req.getParameter("action");
        if (!action.equals("conferma") && !action.equals("annulla"))
            throw new Exception("DOM modificato");


        if (action.equals("conferma")) ordineService.checkoutFinalization(Boolean.parseBoolean(lambda), destinatario, indirizzo, numeroCarta, req);


        return "redirect:/userArea";
    }
}
