package com.lambda.demo.Control.GC;

import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Service.GC.SuperProdotto.SuperProdottoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ProdottoControl {
    @Autowired
    private SuperProdottoService superProdottoService;

    /**
     * gestisce la logica relativa alla ricerca dei prodotti
     *
     * @param req                oggetto HttServletRequest che rappresenta la richiesta Http
     * @param redirectAttributes oggetto RedirectAttributes che funge da interfaccia
     * @see HttpServletRequest
     * @see RedirectAttributes
     */
    @RequestMapping(value = "/searchProduct", method = RequestMethod.POST)
    public String searchProduct(HttpServletRequest req, Model model, RedirectAttributes redirectAttributes) {
        String idCategoriaReq = req.getParameter("idCategoria");
        List<SuperProdottoEntity> products = new ArrayList<>();
        String name = req.getParameter("name");
        String catalogName = req.getParameter("catalogName");

        if (catalogName != null && !catalogName.isBlank()) {
            List<SuperProdottoEntity> productsToAdd = superProdottoService.findByName(catalogName)
                    .stream()
                    .filter(superProdottoEntity -> superProdottoEntity.getCheapestInsertion() != null)
                    .toList();
            model.addAttribute("catalogName", catalogName);
            model.addAttribute("products", productsToAdd);
            return "purchaser/catalog";
        } else {
            if (name != null) {
                //se il parametro idCategoriaReq è diverso da null significa che c'è un form che ha un input con tale nome - tradeInSearchProduct.html
                if (idCategoriaReq != null) {
                    try {
                        int idCategoria = Integer.parseInt(idCategoriaReq);
                        redirectAttributes.addFlashAttribute("idCategoria", idCategoria);

                        if (!name.isBlank())
                            products = superProdottoService.findByNameAndCategory(name, idCategoria);
                        model.addAttribute("products", products);

                        return "purchaser/tradeInSearchProduct";
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("ID Categoria non valido - controlla il form e riprova.");
                    }
                } else {
                    //se idCategoria è uguale a null, nessun form sta provando a ricercare prodotti basandosi sulla categoria - selectProduct.html
                    if (!name.isBlank())
                        products = superProdottoService.findByName(name);
                    model.addAttribute("products", products);
                    return "vendor/selectProduct";
                }
            }
        }

        return "redirect:/userArea";
    }
}
