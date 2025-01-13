package com.lambda.demo.Control.GA;

import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Exception.GA.GAException;
import com.lambda.demo.Service.GA.Permuta.PermutaService;
import com.lambda.demo.Service.GC.Categoria.CategoriaService;
import com.lambda.demo.Service.GC.SuperProdotto.SuperProdottoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PermutaControl {
    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private SuperProdottoService superProdottoService;

    @Autowired
    private PermutaService permutaService;

    /**
     * gestisce la logica relativa alla valutazione di un prodotto permutato
     *
     * @param req   oggetto HttServletRequest che rappresenta la richiesta Http
     * @param model oggetto model che rappresenta un'interfaccia di comunicazione tra View e Business Logic
     * @throws Exception eccezione generica
     * @see HttpServletRequest
     * @see Model
     */
    @RequestMapping(value = "/productEvaluation", method = RequestMethod.POST)
    public String productEvaluation(HttpServletRequest req, Model model) throws Exception {
        // recupero parametri
        String superProdottoIdReq = req.getParameter("superProdottoId");
        int superProdottoId = Integer.parseInt(superProdottoIdReq);
        SuperProdottoEntity superProdottoEntity = superProdottoService.findById(superProdottoId);
        String ram = req.getParameter("ram");
        String spazioArchiviazione = req.getParameter("spazioArchiviazione");
        String colore = req.getParameter("colore");
        String condizioneGenerale = req.getParameter("condizioneGenerale");
        String batteria = req.getParameter("batteria");
        Part imgDispositivo = req.getPart("imgDispositivo");


        List<String> img = new ArrayList<>();
        img.add(imgDispositivo.getSubmittedFileName());
        img.add(String.valueOf(imgDispositivo.getSize()));

        try {
            permutaService.checkAddPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, img);
        } catch (Exception exception) {
            throw new Exception(exception.getMessage());
        }

        model.addAttribute("superProdotto", superProdottoEntity);
        model.addAttribute("ram", ram);
        model.addAttribute("spazioArchiviazione", spazioArchiviazione);
        model.addAttribute("colore", colore);
        model.addAttribute("condizioneGenerale", condizioneGenerale);
        model.addAttribute("batteria", batteria);
        model.addAttribute("nomeImg", img.getFirst());
        model.addAttribute("pesoImg", img.getLast());


        int lambdaPoints = permutaService.evaluateLambdaPoints(superProdottoEntity, condizioneGenerale, batteria);
        model.addAttribute("lambdaPoints", lambdaPoints);

        return "purchaser/tradeInSummary";
    }


    /**
     * gestisce la logica all'aggiunta di una permuta
     *
     * @param req                oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto model per meccanismo dei riscontri
     * @throws Exception eccezione generica
     * @see HttpServletRequest
     * @see RedirectAttributes
     */
    @Transactional
    @RequestMapping(value = "/addTradeIn", method = RequestMethod.POST)
    public String addTradeIn(HttpServletRequest req, RedirectAttributes redirectAttributes) throws Exception {
        String action = req.getParameter("action");

        if (action.equals("conferma")) {
            String superProdottoId = req.getParameter("superProdottoId");
            String ram = req.getParameter("ram");
            String spazioArchiviazione = req.getParameter("spazioArchiviazione");
            String colore = req.getParameter("colore");
            String condizioneGenerale = req.getParameter("condizioneGenerale");
            String batteria = req.getParameter("batteria");

            List<String> img = new ArrayList<>();
            img.add(req.getParameter("nomeImg"));
            img.add(req.getParameter("pesoImg"));


            req.setAttribute("superProdottoId", Integer.parseInt(superProdottoId));
            try {
                permutaService.addPermuta(Integer.parseInt(ram),
                        Integer.parseInt(spazioArchiviazione), Integer.parseInt(batteria),
                        condizioneGenerale, colore, img);
            } catch (GAException gaException) {
                throw new GAException(gaException.getMessage());
            }

        }

        redirectAttributes.addFlashAttribute("msg", "Permuta effettuata con successo!");

        return "redirect:/userArea";
    }
}