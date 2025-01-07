package com.lambda.demo.Service.GPR.Acquirente;

import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GA.GestioneOrdini.InvalidAddressException;
import com.lambda.demo.Exception.GPR.GPRException;

public interface AcquirenteService {
    void signupAcquirente(String nome, String cognome, String email, String password, String confermaPassword) throws GPRException;

    void loginAcquirente(String email, String password) throws GPRException;

    int updateAcquirente(AcquirenteEntity acquirenteEntity);

    AcquirenteEntity updateAcquirenteData(AcquirenteEntity acquirenteEntity, String nome, String cognome, String indirizzo, String passwordAttuale, String nuovaPassword, String confermaNuovaPassword) throws GPRException, InvalidAddressException;

    AcquirenteEntity getAcquirente(String email);
}

