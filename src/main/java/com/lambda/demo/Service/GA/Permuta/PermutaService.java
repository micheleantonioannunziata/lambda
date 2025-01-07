package com.lambda.demo.Service.GA.Permuta;

import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Exception.GA.GAException;
import com.lambda.demo.Exception.GC.GestioneInserzione.InvalidRAMException;
import com.lambda.demo.Exception.GC.GestioneInserzione.InvalidStorageException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PermutaService {

    void checkAddPermuta(String ram, String spazioArchiviazione, String batteria, String condizioneGenerale, String colore, List<String> immagine) throws GAException, InvalidRAMException, InvalidStorageException;

    void addPermuta(int superProdottoId, int ram, int spazioArchiviazione, int batteria, String condizioneGenerale, String colore, int lambdaPoints, HttpServletRequest req);

    int evaluateLambdaPoints(SuperProdottoEntity superProdotto, String condizioneGenerale, String batteria);
}
