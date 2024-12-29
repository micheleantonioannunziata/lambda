package com.lambda.demo.Service.GPR.Rivenditore;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;

public interface RivenditoreService {
    void signupRivenditore(String ragioneSociale, String partitaIVa, String email, String password) throws Exception;

    void loginRivenditore(String email, String password) throws Exception;

    int updateRivenditore(RivenditoreEntity rivenditoreEntity);
}
