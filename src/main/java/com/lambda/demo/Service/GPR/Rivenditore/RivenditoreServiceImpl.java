package com.lambda.demo.Service.GPR.Rivenditore;

import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Repository.GPR.RivenditoreRepository;
import com.lambda.demo.Utility.Encrypt;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RivenditoreServiceImpl implements RivenditoreService{
    @Autowired
    private RivenditoreRepository rivenditoreRepository;

    @Override
    public void signupRivenditore(String ragioneSociale, String partitaIVA, String email, String password) throws Exception {
        RivenditoreEntity rivenditore = rivenditoreRepository.findByPartitaIva(partitaIVA);
        if (rivenditore != null)
            throw new Exception("Partita IVA già registrata!");

        rivenditore = rivenditoreRepository.findByRagioneSociale(ragioneSociale);
        if (rivenditore != null)
            throw new Exception("Ragione sociale già registrata!");

        rivenditore = rivenditoreRepository.findByEmail(email);
        //se è già presente una mail nel DB, l'utente già esiste, quindi non può registrarsi
        if(rivenditore != null)
            throw new Exception("Utente già registrato!");

        String encryptedPassword = Encrypt.encrypt(password);
        rivenditore = new RivenditoreEntity();
        rivenditore.setRagioneSociale(ragioneSociale);
        rivenditore.setPartitaIva(partitaIVA);
        rivenditore.setEmail(email);
        rivenditore.setPassword(encryptedPassword);
        rivenditoreRepository.save(rivenditore);
    }


    @Override
    public void loginRivenditore(String email, String password) throws Exception {
        RivenditoreEntity rivenditore = null;

        rivenditore = rivenditoreRepository.findByEmail(email);

        if(rivenditore == null) throw new Exception("Utente non registrato!");

        rivenditore = null;

        rivenditore = rivenditoreRepository.findByEmailAndPassword(email, Encrypt.encrypt(password));

        if(rivenditore == null) throw new Exception("Password errata!");
    }

    @Override
    @Transactional
    public int updateRivenditore(RivenditoreEntity rivenditoreEntity) {
        return rivenditoreRepository.updateRivenditoreEntity(rivenditoreEntity);
    }

    @Override
    public RivenditoreEntity findByPartita(String partitaIva) {
        return rivenditoreRepository.findByPartitaIva(partitaIva);
    }
}
