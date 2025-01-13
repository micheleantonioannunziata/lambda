package com.lambda.demo.Control.GA;

import com.lambda.demo.Exception.GA.GAException;
import com.lambda.demo.Service.GA.Ordine.OrdineService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrdineControl {
    @Autowired
    private OrdineService ordineService;

    /**
     * gestisce la logica relativa all'aggiunta delle informazioni necessarie per finalizzare un ordine
     *
     * @param req                oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes che funge da interfaccia
     * @throws GAException eccezione generica di GA
     * @see HttpServletRequest
     * @see RedirectAttributes
     */
    @RequestMapping(value = "/checkoutInfo", method = RequestMethod.POST)
    public String processCheckoutInfo(HttpServletRequest req, Model model) throws GAException {
        // recupero parametri
        String destinatario = req.getParameter("destinatario");
        String indirizzo = req.getParameter("indirizzo");
        String lambdaFlag = req.getParameter("lambdaFlag");

        // verifica lambdaFlag
        if (!(lambdaFlag.equals("true") || lambdaFlag.equals("false")))
            throw new GAException("DOM modificato: valore non valido per lambdaFlag");

        boolean lambda = Boolean.parseBoolean(lambdaFlag);

        // recupero dati carta di pagamento
        String intestatarioCarta = req.getParameter("intestatarioCarta");
        String numeroCarta = req.getParameter("numeroCarta");
        String cvv = req.getParameter("CVV");
        String scadenza = req.getParameter("scadenza");

        // gestione checkout con carta
        if (!lambda && intestatarioCarta != null && numeroCarta != null && cvv != null && scadenza != null) {
            try {
                ordineService.cardCheckoutValidation(destinatario, indirizzo, intestatarioCarta, numeroCarta, cvv, scadenza);
            } catch (GAException gaException) {
                throw new GAException(gaException.getMessage());
            }
            // aggiunta dati carta
            model.addAttribute("intestatarioCarta", intestatarioCarta);
            model.addAttribute("numeroCarta", numeroCarta);
            model.addAttribute("cvv", cvv);
            model.addAttribute("scadenza", scadenza);
        }
        // gestione checkout con Lambda Points
        else if (lambda && intestatarioCarta == null && numeroCarta == null && cvv == null && scadenza == null) {
            try {
                ordineService.lambdaCheckoutValidation(destinatario, indirizzo);
            } catch (GAException gaException) {
                throw new GAException(gaException.getMessage());
            }
        } else {
            throw new GAException("Errore nella selezione del metodo di pagamento: controllare i dati forniti.");
        }

        // aggiunta informazioni comuni
        model.addAttribute("destinatario", destinatario);
        model.addAttribute("indirizzo", indirizzo);
        model.addAttribute("lambda", lambda);

        return "purchaser/checkoutSummary";
    }


    /**
     * gestisce la logica relativa alla finalizzazione dell'ordine
     *
     * @param req                oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes per meccanismo dei riscontri
     * @throws GAException eccezione generica di GA
     * @see HttpServletRequest
     * @see RedirectAttributes
     */
    @Transactional
    @RequestMapping(value = "/checkoutFinalization", method = RequestMethod.POST)
    public String checkoutFinalization(HttpServletRequest req, RedirectAttributes redirectAttributes) throws GAException {
        // recupero parametri
        String lambdaFlag = req.getParameter("lambda");
        String destinatario = req.getParameter("destinatario");
        String indirizzo = req.getParameter("indirizzoSpedizione");
        String numeroCarta = req.getParameter("numeroCarta");
        String intestatarioCarta = req.getParameter("intestatario");
        String cvv = req.getParameter("cvv");
        String scadenza = req.getParameter("scadenza");
        String action = req.getParameter("action");

        // verifica lambdaFlag
        if (!(lambdaFlag.equals("true") || lambdaFlag.equals("false")))
            throw new GAException("DOM modificato: valore non valido per lambdaFlag!");

        // verifica action
        if (!(action.equals("conferma") || action.equals("annulla")))
            throw new GAException("DOM modificato: valore non valido per action!");


        // passaggio info al service
        req.setAttribute("lambda", lambdaFlag);

        if (action.equals("conferma"))
            try {
                ordineService.checkoutFinalization(destinatario, indirizzo, intestatarioCarta, numeroCarta, cvv, scadenza);
            } catch (GAException gaException) {
                throw new GAException(gaException.getMessage());
            }

        // meccanismo riscontri
        redirectAttributes.addFlashAttribute("msg", "Ordine finalizzato con successo!");

        return "redirect:/userArea";
    }
}