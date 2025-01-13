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
import jakarta.servlet.http.HttpSession;
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

    @Autowired
    private HttpServletRequest request;

    @Override
    public void checkAddPermuta(String ramReq, String spazioArchiviazioneReq, String batteriaReq, String condizioneGenerale, String colore, List<String> immagine) throws GAException, InvalidRAMException, InvalidStorageException {
        if (!Validator.isValidGeneralCondition(condizioneGenerale))
            throw new InvalidGeneralConditionException("Valore di Condizione generale non ammesso!");

        if (!Validator.isValidRamValue(ramReq))
            throw new InvalidRAMException("Valore di RAM non ammesso!");

        if (!Validator.isValidStorageValue(spazioArchiviazioneReq))
            throw new InvalidStorageException("Valore di spazio archiviazione non valido!");

        if (!Validator.isValidBattery(batteriaReq))
            throw new InvalidBatteryException("Valore di batteria non valido!");

        if (!Validator.isValidColor(colore))
            throw new InvalidColorException("Il colore non rispetta il formato richiesto!");

        if (!Validator.isValidFileName(immagine.getFirst())) {
            throw new InvalidFileException("Nome del file non rispetta il formato richiesto!");
        }
        if (!Validator.isValidFileSize(Long.parseLong(immagine.getLast())))
            throw new InvalidFileException("Peso del file eccessivo");

    }

    @Override
    public void addPermuta(int ram, int spazioArchiviazione, int batteria, String condizioneGenerale, String colore, List<String> immagine) throws GAException, InvalidStorageException, InvalidRAMException {

        //HttpSession session = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getSession();

        HttpSession session = request.getSession();
        checkAddPermuta(String.valueOf(ram), String.valueOf(spazioArchiviazione), String.valueOf(batteria), condizioneGenerale, colore, immagine);
        AcquirenteEntity acquirenteEntity = (AcquirenteEntity) session.getAttribute("acquirente");

        int superProdottoId = (int) request.getAttribute("superProdottoId");
        PermutaEntityId permutaEntityId = new PermutaEntityId(acquirenteEntity.getId(), superProdottoId, LocalDateTime.now());

        SuperProdottoEntity superProdottoEntity = superProdottoRepository.findById(superProdottoId);
        int lambdaPoints = evaluateLambdaPoints(superProdottoEntity, condizioneGenerale, String.valueOf(batteria));

        PermutaEntity permutaEntity = new PermutaEntity(permutaEntityId, superProdottoEntity, SessionManager.getAcquirente(request), batteria, condizioneGenerale, ram, spazioArchiviazione, colore, lambdaPoints);
        permutaRepository.save(permutaEntity);

        acquirenteEntity.setSaldo(acquirenteEntity.getSaldo() + lambdaPoints);

        acquirenteRepository.updateSaldoLambdaPoints(acquirenteEntity.getSaldo(), acquirenteEntity.getId());

        List<PermutaEntity> permute = acquirenteEntity.getPermute();
        permute.add(permutaEntity);
        acquirenteEntity.setPermute(permute);
        SessionManager.setAcquirente(request, acquirenteEntity);
    }


    @Override
    public int evaluateLambdaPoints(SuperProdottoEntity superProdotto, String condizioneGenerale, String batteria) {
        int conversionRate = 10;
        InserzioneEntity inserzioneEntity = superProdotto.getCheapestInsertion();
        double prezzo = (inserzioneEntity != null) ? inserzioneEntity.getPrezzoBase() : 500;

        int lambdaPoints = (int) (prezzo / (conversionRate * 2));

        if (condizioneGenerale.equals("buona")) lambdaPoints -= (int) (0.02 * lambdaPoints);
        else if (condizioneGenerale.equals("discreta")) lambdaPoints -= (int) (0.05 * lambdaPoints);

        if (Integer.parseInt(batteria) < 60)
            lambdaPoints -= (int) (0.05 * lambdaPoints);

        return lambdaPoints;

    }
}
