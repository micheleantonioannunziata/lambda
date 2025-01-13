package com.lambda.demo.configuration;

import com.lambda.demo.Service.GC.Categoria.CategoriaService;
import com.lambda.demo.Service.GC.SuperProdotto.SuperProdottoService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class HTMLMapping {

    @Autowired
    private CategoriaService categoriaService;
    @Autowired
    private SuperProdottoService superProdottoService;


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("msg", model.getAttribute("msg"));
        return "index";
    }

    @GetMapping("/vendorAuth")
    public String vendorAuth() {
        return "vendorAuth";
    }

    @GetMapping("/userAuth")
    public String userAuth() {
        return "userAuth";
    }

    @GetMapping("/userArea")
    public String userArea(Model model) {
        model.addAttribute("msg", model.getAttribute("msg"));
        return "purchaser/userArea";
    }

    @GetMapping("/vendorArea")
    public String vendorArea(Model model) {
        model.addAttribute("msg", model.getAttribute("msg"));
        return "/vendor/vendorArea";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "/purchaser/checkout";
    }

    @GetMapping("/checkoutSummary")
    public String checkoutSummary() {
        return "/purchaser/checkoutSummary";
    }

    @GetMapping("/userDataUpdate")
    public String userDataUpdate() {
        return "/purchaser/userDataUpdate";
    }

    @GetMapping("/vendorDataUpdate")
    public String vendorDataUpdate() {
        return "/vendor/vendorDataUpdate";
    }

    @GetMapping("/myCart")
    public String myCart() {
        return "/purchaser/myCart";
    }

    @PostMapping("/redirectToTradeInCategory")
    public String redirectToTradeInCategory(Model model) {
        model.addAttribute("categories", categoriaService.getAllCategories());

        return "/purchaser/tradeInCategory";
    }

    @GetMapping("/selectProduct")
    public String selectProduct(){return "/vendor/selectProduct";};

    @PostMapping("/redirectToTradeInSearch")
    public String redirectToTradeInSearch(HttpServletRequest req, Model model) {
        model.addAttribute("idCategoria", req.getParameter("idCategoria"));

        return "/purchaser/tradeInSearchProduct";
    }

    @PostMapping("/redirectToTradeInForm")
    public String redirectToTradeInForm(HttpServletRequest req, Model model) {
        model.addAttribute("superProdotto", superProdottoService.findById(Integer.parseInt(req.getParameter("id"))));
        return "/purchaser/tradeInForm";
    }

}
