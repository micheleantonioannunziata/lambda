package com.lambda.demo.Service.GA.Permuta;

import com.lambda.demo.Entity.GA.Permuta.PermutaEntity;
import com.lambda.demo.Entity.GA.Permuta.PermutaEntityId;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GA.GAException;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidBatteryException;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidColorException;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidFileException;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidGeneralConditionException;
import com.lambda.demo.Exception.GC.GestioneInserzione.InvalidRAMException;
import com.lambda.demo.Exception.GC.GestioneInserzione.InvalidStorageException;
import com.lambda.demo.Repository.GA.Permuta.PermutaRepository;
import com.lambda.demo.Repository.GC.SuperProdottoRepository;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Utility.SessionManager;
import com.lambda.demo.Utility.Validator;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PermutaServiceImpl implements PermutaService {
    @Autowired
    private PermutaRepository permutaRepository;

    @Autowired
    private AcquirenteRepository acquirenteRepository;

    @Autowired
    private SuperProdottoRepository superProdottoRepository;

    @Override
    public void checkAddPermuta(String ramReq, String spazioArchiviazioneReq, String batteriaReq, String condizioneGenerale, String colore, List<String> immagine) throws GAException, InvalidRAMException, InvalidStorageException {

    if (!Validator.isValidGeneralCondition(condizioneGenerale))
        throw new InvalidGeneralConditionException("Valore di Condizione generale non ammesso!");

    if(!Validator.isValidRamValue(ramReq))
        throw new InvalidRAMException("Valore di RAM non ammesso!");

    if(!Validator.isValidStorageValue(spazioArchiviazioneReq))
        throw new InvalidStorageException("Valore di spazio archiviazione non valido!");

    if(!Validator.isValidBattery(batteriaReq))
        throw new InvalidBatteryException("Valore di batteria non valido!");

    if(!Validator.isValidColor(colore))
        throw new InvalidColorException("Il colore non rispetta il formato richiesto!");

    if(!Validator.isValidFileName(immagine.getFirst())) {
        throw new InvalidFileException("Nome del file non rispetta il formato richiesto!");
    }
    if (!Validator.isValidFileSize(Long.parseLong(immagine.getLast())))
        throw new InvalidFileException("Peso del file eccessivo");

    }

    @Override
    public void addPermuta(int superProdottoId, int ram, int spazioArchiviazione, int batteria, String condizioneGenerale, String colore, int lambdaPoints, HttpServletRequest req) {
        PermutaEntityId permutaEntityId = new PermutaEntityId(SessionManager.getAcquirente(req).getId(), superProdottoId, LocalDateTime.now());

        AcquirenteEntity acquirenteEntity =  acquirenteRepository.findById(SessionManager.getAcquirente(req).getId()).get();
        SuperProdottoEntity superProdottoEntity = superProdottoRepository.findById(superProdottoId);

        PermutaEntity permutaEntity = new PermutaEntity(permutaEntityId, superProdottoEntity, acquirenteEntity, batteria, condizioneGenerale, ram, spazioArchiviazione, colore, lambdaPoints);

        permutaRepository.save(permutaEntity);


        SessionManager.getAcquirente(req).setSaldo(SessionManager.getAcquirente(req).getSaldo() + lambdaPoints);

        acquirenteRepository.updateSaldoLambdaPoints(SessionManager.getAcquirente(req).getSaldo(), SessionManager.getAcquirente(req).getId());

        List<PermutaEntity> permute = SessionManager.getAcquirente(req).getPermute();
        permute.add(permutaEntity);
        SessionManager.getAcquirente(req).setPermute(permute);
    }


    @Override
    public int evaluateLambdaPoints(SuperProdottoEntity superProdotto, String condizioneGenerale, String batteria) {
        InserzioneEntity inserzioneEntity = superProdotto.getCheapestInsertion();

        int conversionRate = 10;

        int lambdaPoints = (int) (inserzioneEntity.getPrezzoBase() / (conversionRate * 2));

        if (condizioneGenerale.equals("buona")) lambdaPoints -= (int) (0.02 * lambdaPoints);
        else if(condizioneGenerale.equals("discreta")) lambdaPoints -= (int) (0.05 * lambdaPoints);

        if (Integer.parseInt(batteria) < 60)
            lambdaPoints -= (int) (0.05 * lambdaPoints);

        return lambdaPoints;
    }
}
