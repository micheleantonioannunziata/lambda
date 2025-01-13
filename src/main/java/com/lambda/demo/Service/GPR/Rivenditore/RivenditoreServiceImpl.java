package com.lambda.demo.Service.GPR.Rivenditore;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Exception.GA.GestioneOrdini.InvalidAddressException;
import com.lambda.demo.Exception.GPR.AccessoRivenditore.AlreadyRegisteredCompanyNameException;
import com.lambda.demo.Exception.GPR.AccessoRivenditore.AlreadyRegisteredVATNumberException;
import com.lambda.demo.Exception.GPR.AccessoRivenditore.InvalidCompanyNameException;
import com.lambda.demo.Exception.GPR.AccessoRivenditore.InvalidVATNumberException;
import com.lambda.demo.Exception.GPR.*;
import com.lambda.demo.Repository.GPR.RivenditoreRepository;
import com.lambda.demo.Utility.Encrypt;
import com.lambda.demo.Utility.Validator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RivenditoreServiceImpl implements RivenditoreService {
    @Autowired
    private RivenditoreRepository rivenditoreRepository;

    @Override
    public void signupRivenditore(String ragioneSociale, String partitaIVA, String email, String password, String confermaPassword) throws GPRException {
        if (!Validator.isValidCompanyName(ragioneSociale))
            throw new InvalidCompanyNameException("Ragione sociale non rispetta il formato richiesto!");

        if (!Validator.isValidEmail(email))
            throw new InvalidEmailException("Email non rispetta il formato richiesto!");

        if (!Validator.isValidPartitaIVA(partitaIVA))
            throw new InvalidVATNumberException("Partita Iva non rispetta il formato richiesto!");

        if (!Validator.isValidPassword(password))
            throw new InvalidPasswordException("Password non rispetta il formato richiesto!");

        if (!confermaPassword.equals(password))
            throw new UnMatchedPasswordException("Le password non coincidono!");

        RivenditoreEntity rivenditore = rivenditoreRepository.findByPartitaIva(partitaIVA);
        if (rivenditore != null)
            throw new AlreadyRegisteredVATNumberException("Partita IVA già registrata!");

        rivenditore = rivenditoreRepository.findByRagioneSociale(ragioneSociale);
        if (rivenditore != null)
            throw new AlreadyRegisteredCompanyNameException("Ragione sociale già registrata!");

        rivenditore = rivenditoreRepository.findByEmail(email);
        //se è già presente una mail nel DB, non può registrarsi con quella mail
        if (rivenditore != null)
            throw new AlreadyRegisteredEmailException("Utente già registrato con questa email!");

        String encryptedPassword = Encrypt.encrypt(password);
        rivenditore = new RivenditoreEntity();
        rivenditore.setRagioneSociale(ragioneSociale);
        rivenditore.setPartitaIva(partitaIVA);
        rivenditore.setEmail(email);
        rivenditore.setPassword(encryptedPassword);
        rivenditoreRepository.save(rivenditore);
    }


    @Override
    public void loginRivenditore(String email, String password) throws GPRException {
        if (!Validator.isValidEmail(email))
            throw new InvalidEmailException("Email non rispetta il formato richiesto!");

        if (!Validator.isValidPassword(password))
            throw new InvalidPasswordException("Password non rispetta il formato richiesto!");


        RivenditoreEntity rivenditore = rivenditoreRepository.findByEmail(email);

        if (rivenditore == null) throw new NotRegisteredUserException("Utente non registrato!");


        rivenditore = rivenditoreRepository.findByEmailAndPassword(email, Encrypt.encrypt(password));

        if (rivenditore == null) throw new WrongPasswordException("Password errata!");
    }


    @Override
    public RivenditoreEntity updateVendorData(RivenditoreEntity rivenditoreEntity, String ragioneSociale, String indirizzo, String passwordAttuale, String nuovaPassword, String confermaNuovaPassword) throws GPRException, InvalidAddressException {
        if (passwordAttuale.isBlank())
            throw new PasswordEmptyException("Inserire la password attuale per modificare i dati!");

        if (!Encrypt.encrypt(passwordAttuale).equals(rivenditoreEntity.getPassword()))
            throw new WrongPasswordException("Password attuale non corretta!");

        if (!ragioneSociale.isBlank()) {
            if (!Validator.isValidCompanyName(ragioneSociale))
                throw new InvalidCompanyNameException("Ragione sociale non rispetta il formato richiesto!");
            rivenditoreEntity.setRagioneSociale(ragioneSociale);
        }

        if (!indirizzo.isBlank()) {
            if (!Validator.isValidAddress(indirizzo))
                throw new InvalidAddressException("Indirizzo non rispetta il formato richiesto");
            rivenditoreEntity.setIndirizzo(indirizzo);
        }

        if ((!nuovaPassword.isBlank() && confermaNuovaPassword.isBlank())
                || (nuovaPassword.isBlank() && !confermaNuovaPassword.isBlank()))
            throw new NotCompiledAllPasswordFIelds("I campi relativi alle password non sono stati compilati nella loro interezza!");

        if (!nuovaPassword.isBlank() && !confermaNuovaPassword.isBlank()) {
            if (!Validator.isValidPassword(nuovaPassword))
                throw new InvalidPasswordException("Nuova password non rispetta il formato!");
            if (passwordAttuale.equals(nuovaPassword))
                throw new MatchingOldAndNewPasswordException("La vecchia password e la nuova password non possono essere uguali!");
            if (!nuovaPassword.equals(confermaNuovaPassword))
                throw new UnMatchedPasswordException("La nuova password e la conferma non coincidono!");
            rivenditoreEntity.setPassword(Encrypt.encrypt(nuovaPassword));
        }


        return rivenditoreEntity;
    }

    @Override
    @Transactional
    public int updateRivenditore(RivenditoreEntity rivenditoreEntity) {
        return rivenditoreRepository.updateRivenditoreEntity(rivenditoreEntity);
    }

    @Override
    public RivenditoreEntity findByPartitaIva(String partitaIva) {
        return rivenditoreRepository.findByPartitaIva(partitaIva);
    }

    @Override
    public RivenditoreEntity findByEmail(String email) {
        return rivenditoreRepository.findByEmail(email);
    }

    @Override
    public void saveRivenditore(RivenditoreEntity rivenditoreEntity) {
        rivenditoreRepository.save(rivenditoreEntity);
    }

    @Override
    @Transactional
    public void deleteVendorAccount(String email) {
        rivenditoreRepository.deleteRivenditoreEntityByEmail(email);
    }
}