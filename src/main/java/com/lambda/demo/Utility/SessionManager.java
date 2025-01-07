package com.lambda.demo.Utility;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import jakarta.servlet.http.HttpServletRequest;

public class SessionManager {
    public static void setAcquirente(HttpServletRequest req, AcquirenteEntity acquirenteEntity){
        req.getSession().setAttribute("acquirente", acquirenteEntity);
    }

    public static AcquirenteEntity getAcquirente(HttpServletRequest req){
        return (AcquirenteEntity) req.getSession().getAttribute("acquirente");
    }

    public static void setRivenditore(HttpServletRequest req, RivenditoreEntity rivenditoreEntity){
        req.getSession().setAttribute("rivenditore", rivenditoreEntity);
    }

    public static RivenditoreEntity getRivenditore(HttpServletRequest req){
        return (RivenditoreEntity) req.getSession().getAttribute("rivenditore");
    }

    public static void setCarrello(HttpServletRequest req, CarrelloEntity carrelloEntity){
        req.getSession().setAttribute("carrello", carrelloEntity);
    }

    public static CarrelloEntity getCarrello(HttpServletRequest req){
        return (CarrelloEntity) req.getSession().getAttribute("carrello");
    }

}
