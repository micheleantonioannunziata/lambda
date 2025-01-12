package com.lambda.demo.Control;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorControl implements ErrorController {

    /**
     * gestisce la richiesta di visualizzazione pagina di errore
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param model oggetto Model che funge da interfaccia
     * @see HttpServletRequest
     * @see Model
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest req, Model model) {
        Integer statusCode = (Integer) req.getAttribute("jakarta.servlet.error.status_code");
        Throwable throwable = (Throwable) req.getAttribute("jakarta.servlet.error.exception");

        String msg = "Errore generico di sistema!";

        if (throwable != null)
            msg = getRootCause(throwable).getMessage();

        if (statusCode == 404)
            msg = "Risorsa non trovata!";

        model.addAttribute("status", statusCode);
        model.addAttribute("msg", msg);

        return "error";
    }

    /**
     * gestisce la logica per cattuarare la radice di un'eccezione
     * @param throwable oggetto Throwable che rappresenta l'eccezione lanciata
     * @see Throwable
     */
    private Throwable getRootCause(Throwable throwable) {
        Throwable cause = throwable.getCause();
        if (cause != null && cause != throwable) {
            return getRootCause(cause);
        }
        return throwable;
    }

}