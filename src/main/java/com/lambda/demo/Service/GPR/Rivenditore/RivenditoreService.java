package com.lambda.demo.Service.GPR.Rivenditore;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Exception.GA.GestioneOrdini.InvalidAddressException;
import com.lambda.demo.Exception.GPR.GPRException;

public interface RivenditoreService {
    void signupRivenditore(String ragioneSociale, String partitaIVa, String email, String password, String confermaPassword) throws GPRException;

    void loginRivenditore(String email, String password) throws GPRException;

    int updateRivenditore(RivenditoreEntity rivenditoreEntity);

    RivenditoreEntity updateVendorData(RivenditoreEntity rivenditoreEntity, String ragioneSociale, String indirizzo, String passwordAttuale, String nuovaPassword, String confermaNuovaPassword) throws GPRException, InvalidAddressException;

    RivenditoreEntity findByPartitaIva(String partitaIva);

    RivenditoreEntity findByEmail(String email);

    void saveRivenditore(RivenditoreEntity rivenditoreEntity);

    void deleteVendorAccount(String email);
}