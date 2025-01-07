package com.lambda.demo.Service.GPR.Acquirente;

import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GPR.AccessoAcquirente.*;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Utility.Encrypt;
import com.lambda.demo.Utility.Validator;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcquirenteServiceImpl implements AcquirenteService{
    @Autowired
    private AcquirenteRepository acquirenteRepository;

    @Override
    public void signupAcquirente(String nome, String cognome, String email, String password, String confermaPassword) throws Exception {
        AcquirenteEntity acquirente = acquirenteRepository.findByEmail(email);

        //se è già presente una mail nel DB, l'utente già esiste, quindi non può registrarsi
        if(acquirente != null)
            throw new Exception("Utente già registrato!");

        if(!Validator.isValidName(nome))
            throw new InvalidNameException("Nome non rispetta il formato!");

        if(!Validator.isValidSurname(cognome))
            throw new InvalidSurnameException("Cognome non rispetta il formato!");

        if(!Validator.isValidEmail(email))
            throw new InvalidEmailException("Email non rispetta il formato!");

        if(!Validator.isValidPassword(password))
            throw new InvalidPasswordException("Password non rispetta il formato!");

        if(!confermaPassword.equals(password))
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
    public void loginAcquirente(String email, String password) throws Exception {
        AcquirenteEntity acquirente = null;

        acquirente = acquirenteRepository.findByEmail(email);

        if(acquirente == null) throw new Exception("Utente non registrato!");

        acquirente = null;

        acquirente = acquirenteRepository.findByEmailAndPassword(email, Encrypt.encrypt(password));

        if(acquirente == null) throw new Exception("Password errata!");
    }

    @Override
    @Transactional
    public int updateAcquirente(AcquirenteEntity acquirenteEntity) {
        return acquirenteRepository.updateAcquirenteEntity(acquirenteEntity);
    }
}
