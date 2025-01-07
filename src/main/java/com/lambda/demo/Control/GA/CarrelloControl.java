package com.lambda.demo.Control.GA;

import com.lambda.demo.Service.GA.Carrello.CarrelloService;
import com.lambda.demo.Utility.Validator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class CarrelloControl {
    @Autowired
    private CarrelloService carrelloService;

    /**
     * gestisce la logica relativa all'agggiunta di un inserzione al carrello
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    @RequestMapping(value = "/addToCart", method = RequestMethod.POST)
    public String addToCart(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String partitaIvaRivenditore = req.getParameter("partitaIvaRivenditore");
        String idSuperProdottoRequest = req.getParameter("idSuperProdotto");
        String ramRequest = req.getParameter("RAM");
        String spazioArchiviazioneRequest = req.getParameter("spazioArchiviazione");
        String colore = req.getParameter("colore");

        int ram;
        int spazioArchiviazione;
        int idSuperProdotto;

        //controllo che i campi non siano vuoti (potrebbe accadere se qualcuno manipola il DOM)
        if (partitaIvaRivenditore.isBlank() || idSuperProdottoRequest.isBlank() ||
                ramRequest.isBlank() || spazioArchiviazioneRequest.isBlank() ||
                colore.isBlank())
            res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Inserzione non valida");

        //verifico che i campi ram, spazioArchiviazione e idSuperProdotto siano degli interi come da DB
        try {
            ram = Integer.parseInt(ramRequest);
            spazioArchiviazione = Integer.parseInt(spazioArchiviazioneRequest);
            idSuperProdotto = Integer.parseInt(idSuperProdottoRequest);
            carrelloService.addToCart(partitaIvaRivenditore, idSuperProdotto, ram, spazioArchiviazione, colore, req);
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("Parametri non validi - DOM modificato");
        }

        return "redirect:/myCart";
    }

    /**
     * gestisce la logica relativa alla rimozione di un inserzione al carrello
     * @param req oggetto HttServletRequest che rappresenta la richiesta Http
     * @param res oggetto HttpServletResponse che rappresenta la risposta Http
     *
     * @see HttpServletRequest
     * @see HttpServletResponse
     */
    @RequestMapping(value="/removeFromCart", method = RequestMethod.POST)
    public String removeFromCart(HttpServletRequest req, HttpServletResponse res) throws Exception {
        //Prendo l'id dell'Inserzione (formato da idSuperProdotto, ram, spazioArchiviazione, colore)
        String insertionWrapper = req.getParameter("idInserzione");

        List<String> values = getValues(insertionWrapper);

        String partitaIvaRivenditore = values.get(0);
        // Controllo che la Partita IVA sia valida
        if (!Validator.isValidPartitaIVA(partitaIvaRivenditore))
            throw new IllegalArgumentException("PartitaIVA non valida - DOM modificato");

        String idSuperProdottoReq = values.get(1);
        String ramReq = values.get(2);
        String spazioArchiviazioneReq = values.get(3);
        String colore = values.get(4);

        try{
            int idSuperProdotto = Integer.parseInt(idSuperProdottoReq);
            int ram = Integer.parseInt(ramReq);
            int spazioArchiviazione = Integer.parseInt(spazioArchiviazioneReq);

            //elimino l'inserzione dal carrello
            carrelloService.removeFromCart(partitaIvaRivenditore, idSuperProdotto, ram, spazioArchiviazione, colore, req);
        }catch (NumberFormatException e){
            throw new IllegalArgumentException("Parametri non validi - DOM modificato");
        }

        //redirect necessario per evitare che ricaricando la pagina di myCart dopo aver rimosso un elemento da carrello possa lanciare un eccezione
        return "redirect:/myCart";
    }

    @RequestMapping(value = "/updateQuantity", method = RequestMethod.POST)
    public String updateQuantity(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // Recupero l'id dell'inserzione
        String insertionWrapper = req.getParameter("idInserzione");

        // Recupero la quantità che si desidera modificare
        String quantityReq = req.getParameter("quantity");

        try {
            int quantity = Integer.parseInt(quantityReq);

            // Se la quantità è pari o inferiore a 0, rimuovo l'elemento dal carrello
            if (quantity <= 0) {
                removeFromCart(req, res);
            } else {
                // Estraggo i valori necessari per l'aggiornamento
                List<String> values = getValues(insertionWrapper);

                // Controllo che la Partita IVA sia valida
                String partitaIvaRivenditore = values.get(0);
                if (!Validator.isValidPartitaIVA(partitaIvaRivenditore)) {
                    throw new IllegalArgumentException("Partita IVA non valida - DOM modificato");
                }

                // Recupero altri parametri necessari
                String idSuperProdottoReq = values.get(1);
                String ramReq = values.get(2);
                String spazioArchiviazioneReq = values.get(3);
                String colore = values.get(4);

                try {
                    int idSuperProdotto = Integer.parseInt(idSuperProdottoReq);
                    int ram = Integer.parseInt(ramReq);
                    int spazioArchiviazione = Integer.parseInt(spazioArchiviazioneReq);

                    // Aggiorno la quantità del prodotto nel carrello
                    carrelloService.updateQuantity(partitaIvaRivenditore, idSuperProdotto, ram, spazioArchiviazione, colore, quantity, req);
                } catch (NumberFormatException e) {
                    // Errore nei parametri numerici
                    throw new IllegalArgumentException("Parametri non validi - DOM modificato", e);
                }

                // Redirect necessario per evitare il reinvio accidentale della richiesta
                return "redirect:/myCart";
            }
        } catch (NumberFormatException e) {
            // Gestione dell'errore per parametri non numerici
            throw new IllegalArgumentException("Parametri non validi - DOM modificato", e);
        }

        return "redirect:/myCart";
    }



    List<String> getValues(String insertionWrapper) {
        String[] insertionComponents = insertionWrapper.split(",");
        for (String s : insertionComponents)
            System.out.println(s);

        List<String> values = new ArrayList<>();

        for (String s : insertionComponents) {
            String[] auxiliarArray = s.split("=");
            int n = 1;
            if (auxiliarArray.length > 2)
                n = 2;


            if (auxiliarArray.length > n) {
                if (auxiliarArray[n].contains(")"))
                    auxiliarArray[n] = auxiliarArray[n].substring(0, auxiliarArray[n].length() - 2);

                values.add(auxiliarArray[n].trim());
            }
        }

        return values;
    }

}