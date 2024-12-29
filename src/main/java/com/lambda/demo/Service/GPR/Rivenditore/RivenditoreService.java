package com.lambda.demo.Service.GPR.Acquirente;

public interface RivenditoreService {
    void signupRivenditore(String ragioneSociale, String partitaIVa, String email, String password) throws Exception;

    void loginRivenditore(String email, String password) throws Exception;
}
