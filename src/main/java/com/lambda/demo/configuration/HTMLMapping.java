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

}

