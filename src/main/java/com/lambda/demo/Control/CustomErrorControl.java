package com.lambda.demo.Control;

import com.lambda.demo.Exception.GA.GestioneOrdini.InsufficientLambdaPointsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorControl implements ErrorController {

    /**
     * gestisce la richiesta di visualizzazione pagina di errore
     *
     * @param req   oggetto HttServletRequest che rappresenta la richiesta Http
     * @param model oggetto Model che funge da interfaccia
     * @see HttpServletRequest
     * @see Model
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest req, Model model) {
        Integer statusCode = (Integer) req.getAttribute("jakarta.servlet.error.status_code");
        Throwable throwable = (Throwable) req.getAttribute("jakarta.servlet.error.exception");

        String msg = "Errore generico di sistema!";
        String errorType = "generic";

        if (throwable != null) {
            Throwable rootCause = getRootCause(throwable);
            msg = rootCause.getMessage();
            if (rootCause.getMessage().contains("il saldo attuale di lambda points non è sufficiente per la finalizzazione dell'ordine")) {
                errorType = "insufficientPoints";
            }
        }


        if (statusCode == 404) {
            msg = "Risorsa non trovata!";
        }

        model.addAttribute("status", statusCode);
        model.addAttribute("msg", msg);
        model.addAttribute("errorType", errorType);

        return "error";
    }

    /**
     * gestisce la logica per cattuarare la radice di un'eccezione
     *
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