package com.lambda.demo.Service.GPR.Acquirente;

import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Utility.Encrypt;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcquirenteServiceImpl implements AcquirenteService{
    @Autowired
    private AcquirenteRepository acquirenteRepository;

    @Override
    public void signupAcquirente(String nome, String cognome, String email, String password) throws Exception {
        AcquirenteEntity acquirente = acquirenteRepository.findByEmail(email);

        //se è già presente una mail nel DB, l'utente già esiste, quindi non può registrarsi
        if(acquirente != null)
            throw new Exception("Utente già registrato!");

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
