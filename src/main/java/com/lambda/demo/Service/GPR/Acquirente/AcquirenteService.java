package com.lambda.demo.Service.GPR.Acquirente;

import com.lambda.demo.Entity.GPR.AcquirenteEntity;

public interface AcquirenteService {
    void signupAcquirente(String nome, String cognome, String email, String password, String confermaPassword) throws Exception;

    void loginAcquirente(String email, String password) throws Exception;

    int updateAcquirente(AcquirenteEntity acquirenteEntity);
}

