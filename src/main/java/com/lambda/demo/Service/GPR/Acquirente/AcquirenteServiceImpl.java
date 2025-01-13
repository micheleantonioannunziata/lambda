package com.lambda.demo.Service.GPR.Acquirente;

import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GA.GestioneOrdini.InvalidAddressException;
import com.lambda.demo.Exception.GPR.AccessoAcquirente.InvalidNameException;
import com.lambda.demo.Exception.GPR.AccessoAcquirente.InvalidSurnameException;
import com.lambda.demo.Exception.GPR.*;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Utility.Encrypt;
import com.lambda.demo.Utility.Validator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcquirenteServiceImpl implements AcquirenteService {
    @Autowired
    private AcquirenteRepository acquirenteRepository;

    @Override
    public void signupAcquirente(String nome, String cognome, String email, String password, String confermaPassword) throws GPRException {
        AcquirenteEntity acquirente = acquirenteRepository.findByEmail(email);

        //se è già presente una mail nel DB, l'utente già esiste, quindi non può registrarsi
        if (acquirente != null)
            throw new AlreadyRegisteredEmailException("Utente già registrato!");

        if (!Validator.isValidName(nome))
            throw new InvalidNameException("Nome non rispetta il formato!");

        if (!Validator.isValidSurname(cognome))
            throw new InvalidSurnameException("Cognome non rispetta il formato!");

        if (!Validator.isValidEmail(email))
            throw new InvalidEmailException("Email non rispetta il formato!");

        if (!Validator.isValidPassword(password))
            throw new InvalidPasswordException("Password non rispetta il formato!");

        if (!confermaPassword.equals(password))
            throw new UnMatchedPasswordException("Conferma password non coincide con la password!");


        String encryptedPassword = Encrypt.encrypt(password);
        acquirente = new AcquirenteEntity();
        acquirente.setCognome(cognome);
        acquirente.setNome(nome);
        acquirente.setEmail(email);
        acquirente.setPassword(encryptedPassword);
        acquirenteRepository.save(acquirente);
    }

    @Override
    public void loginAcquirente(String email, String password) throws GPRException {
        if (!Validator.isValidEmail(email)) {
            throw new InvalidEmailException("Email non rispetta il formato!");
        }

        if (!Validator.isValidPassword(password)) {
            throw new InvalidPasswordException("Password non rispetta il formato!");
        }


        AcquirenteEntity acquirente = acquirenteRepository.findByEmail(email);
        if (acquirente == null) {
            throw new NotRegisteredUserException("Utente non registrato!");
        }

        String encryptedPassword = Encrypt.encrypt(password);
        acquirente = acquirenteRepository.findByEmailAndPassword(email, encryptedPassword);
        if (acquirente == null) {
            throw new WrongPasswordException("Password errata!");
        }
    }


    @Override
    public AcquirenteEntity updateAcquirenteData(AcquirenteEntity acquirente, String nome, String cognome, String indirizzo, String passwordAttuale, String nuovaPassword, String confermaNuovaPassword) throws GPRException, InvalidAddressException {
        if (passwordAttuale.isBlank())
            throw new PasswordEmptyException("Inserire la password attuale per modificare i dati!");

        if (!Encrypt.encrypt(passwordAttuale).equals(acquirente.getPassword()))
            throw new WrongPasswordException("Password attuale non corretta!");

        if (!nome.isBlank()) {
            if (!Validator.isValidName(nome))
                throw new InvalidNameException("Nome non rispetta il formato!");
            acquirente.setNome(nome);
        }

        if (!cognome.isBlank()) {
            if (!Validator.isValidSurname(cognome))
                throw new InvalidSurnameException("Cognome non rispetta il formato!");
            acquirente.setCognome(cognome);
        }

        if (!indirizzo.isBlank()) {
            if (!Validator.isValidAddress(indirizzo))
                throw new InvalidAddressException("Indizzo non rispetta il formato richiesto!");
            acquirente.setIndirizzo(indirizzo);
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
            acquirente.setPassword(Encrypt.encrypt(nuovaPassword));
        }

        return acquirente;
    }

    @Override
    @Transactional
    public int updateAcquirente(AcquirenteEntity acquirenteEntity) {
        return acquirenteRepository.updateAcquirenteEntity(acquirenteEntity);
    }

    @Override
    public AcquirenteEntity getAcquirente(String email) {
        return acquirenteRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void deletePurchaserAccount(String email) {
        acquirenteRepository.deleteAcquirenteEntityByEmail(email);
    }
}