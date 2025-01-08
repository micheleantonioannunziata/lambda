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
        String idSuperProdotto = req.getParameter("idSuperProdotto");
        String ram = req.getParameter("RAM");
        String spazioArchiviazione = req.getParameter("spazioArchiviazione");
        String colore = req.getParameter("colore");


        try {
            carrelloService.addToCart(partitaIvaRivenditore, idSuperProdotto, ram, spazioArchiviazione, colore, req);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
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
    public String removeFromCart(HttpServletRequest req, HttpServletResponse res) throws Exception{
        //Prendo l'id dell'Inserzione (formato da idSuperProdotto, ram, spazioArchiviazione, colore)
        String insertionWrapper = req.getParameter("idInserzione");

        List<String> values = getValues(insertionWrapper);

        try {
            carrelloService.removeFromCart(values.getFirst(),  values.get(1), values.get(2), values.get(3), values.get(4), req);
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return "redirect:/myCart";
    }


    @RequestMapping(value = "/updateQuantity", method = RequestMethod.POST)
    public String updateQuantity(HttpServletRequest req, HttpServletResponse res) throws Exception {
        //Prendo l'id dell'Inserzione (formato da idSuperProdotto, ram, spazioArchiviazione, colore)
        String insertionWrapper = req.getParameter("idInserzione");

        List<String> values = getValues(insertionWrapper);
        String quantity = req.getParameter("quantity");

        System.out.println(quantity);
        try {
            carrelloService.updateQuantity(values.getFirst(), values.get(1), values.get(2), values.get(3), values.get(4), quantity, req);
        }catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        return "redirect:/myCart";
    }

    List<String> getValues(String insertionWrapper) {
        String[] insertionComponents = insertionWrapper.split(",");

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