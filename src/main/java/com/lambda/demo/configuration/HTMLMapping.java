package com.lambda.demo.configuration;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HTMLMapping {
    @GetMapping("/")
    public String index() {
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

    @GetMapping("/myCart")
    public String myCart(){ return "myCart";}

    @GetMapping("/checkout")
    public String checkout(){ return "checkout";}

    @GetMapping("/userArea")
    public String userArea(){ return "userArea";}

    @GetMapping("/vendorArea")
    public String vendorArea(){ return "vendorArea";}

    @GetMapping("/userDataUpdate")
    public String userDataUpdate(){ return "userDataUpdate";}

    @GetMapping("/vendorDataUpdate")
    public String vendorDataUpdate(){ return "vendorDataUpdate";}

    @GetMapping("/technicalRequirementsForm")
    public String technicalRequirementsForm(){ return "technicalRequirementsForm";}

    @GetMapping("/priceQuantityForm")
    public String priceQuantityForm(){ return "priceQuantityForm";}

    @GetMapping("/selectProduct")
    public String selectProduct(){ return "selectProduct";}

    @GetMapping("/catalog")
    public String catalog(){ return "catalog";}

    @GetMapping("/insertionOverview")
    public String insertionOverview(){ return "insertionOverview";}

    @GetMapping("/tradeInCategory")
    public String tradeInCategory(){ return "tradeInCategory";}

    @GetMapping("/tradeInForm")
    public String tradeInForm(){ return "tradeInForm";}

    @GetMapping("/tradeInSearchProduct")
    public String tradeInSearchProduct(){ return "tradeInSearchProduct";}

    @GetMapping("/tradeInSummary")
    public String tradeInSummary(){ return "tradeInSummary";}

    @GetMapping("/checkoutSummary")
    public String checkoutSummary(){ return "checkoutSummary";}

    @GetMapping("/tecnhicalRequirementsForm")
    public String tecnhicalRequirementsForm(){ return "tecnhicalRequirementsForm";}

    @GetMapping("/addInsertionSummary")
    public String addInsertionSummary(){ return "addInsertionSummary";}
}

