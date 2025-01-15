package com.lambda.demo.GA;

import com.lambda.demo.Entity.GA.Permuta.PermutaEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidBatteryException;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidColorException;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidFileException;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidGeneralConditionException;
import com.lambda.demo.Exception.GC.GestioneInserzione.InvalidRAMException;
import com.lambda.demo.Exception.GC.GestioneInserzione.InvalidStorageException;
import com.lambda.demo.Repository.GA.Permuta.PermutaRepository;
import com.lambda.demo.Repository.GC.SuperProdottoRepository;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Service.GA.Permuta.PermutaServiceImpl;
import com.lambda.demo.Utility.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
public class PermutaServiceTests {

    @InjectMocks
    private PermutaServiceImpl permutaServiceImpl;

    @Mock
    private PermutaRepository permutaRepository;

    @Mock
    private AcquirenteRepository acquirenteRepository;

    @Mock
    private SuperProdottoRepository superProdottoRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private AcquirenteEntity acquirenteEntity;

    @Mock
    private SuperProdottoEntity superProdottoEntity;

    private List<String> immagine;


    @Before
    public void setUp(){
        MockitoAnnotations.openMocks(this);

        when(request.getSession()).thenReturn(session);
        when(SessionManager.getAcquirente(request)).thenReturn(acquirenteEntity);

        int idSuperProdotto = 1;
        when(request.getAttribute("superProdottoId")).thenReturn(idSuperProdotto);
        when(superProdottoRepository.findById(idSuperProdotto)).thenReturn(superProdottoEntity);

        immagine = new ArrayList<>(Arrays.asList(null, null));
        immagine.set(1, String.valueOf(new Random().nextInt(100000, 31457280)));
    }

    @Test
    public void TC_GA1_1() {
        int ram = 6;
        int spazioArchiviazione = 128;
        int batteria = 75;
        String condizioneGenerale = "Mediocre";
        String colore = "Grigio Siderale";
        immagine.set(0, "iphone13.png");

        assertThrows(InvalidGeneralConditionException.class, () ->
                permutaServiceImpl.addPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, immagine)
        );
    }

    @Test
    public void TC_GA1_2() {
        int ram = 1024;
        int spazioArchiviazione = 128;
        int batteria = 80;
        String condizioneGenerale = "Discreta";
        String colore = "Grigio Siderale";
        immagine.set(0, "iphone11.png");

        assertThrows(InvalidRAMException.class, () ->
                permutaServiceImpl.addPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, immagine)
        );
    }

    @Test
    @SneakyThrows
    public void TC_GA1_3(){
        int ram = 6;
        int spazioArchiviazione = 1;
        int batteria = 60;
        String condizioneGenerale = "Buona";
        String colore = "Blu mezzanotte";
        immagine.set(0, "iphone15.png");


        assertThrows(InvalidStorageException.class, () ->
                permutaServiceImpl.addPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, immagine));

    }

    @Test
    @SneakyThrows
    public void TC_GA1_4(){
        int ram = 6;
        int spazioArchiviazione = 256;
        int batteria = 75;
        String condizioneGenerale = "Buona";
        String colore = "Grigio123 Siderale";
        immagine.set(0, "iphoneX.png");


        assertThrows(InvalidColorException.class, () ->
                permutaServiceImpl.addPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, immagine));

    }

    @Test
    @SneakyThrows
    public void TC_GA1_5(){
        int ram = 8;
        int spazioArchiviazione = 128;
        int batteria = 55;
        String condizioneGenerale = "Ottima";
        String colore = "Li";
        immagine.set(0, "iphone12.png");


        assertThrows(InvalidColorException.class, () ->
                permutaServiceImpl.addPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, immagine));

    }

    @Test
    @SneakyThrows
    public void TC_GA1_6(){
        int ram = 6;
        int spazioArchiviazione = 128;
        int batteria = 75;
        String condizioneGenerale = "Buona";
        String colore = "Grigio Siderale Blu Mezzanotte Bianco Perla Verde Palude Azzurro TIffany Giallo Lime Rosa Limpido";
        immagine.set(0, "iphone13.png");


        assertThrows(InvalidColorException.class, () ->
                permutaServiceImpl.addPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, immagine));

    }

    @Test
    @SneakyThrows
    public void TC_GA1_7(){
        int ram = 4;
        int spazioArchiviazione = 256;
        int batteria = 1000;
        String condizioneGenerale = "Ottima";
        String colore = "Bianco Perla";
        immagine.set(0, "iphone11.png");


        assertThrows(InvalidBatteryException.class, () ->
                permutaServiceImpl.addPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, immagine));

    }

    @Test
    @SneakyThrows
    public void TC_GA1_8(){
        int ram = 4;
        int spazioArchiviazione = 512;
        int batteria = 75;
        String condizioneGenerale = "Discreta";
        String colore = "Grigio Siderale";
        immagine.set(0, "iphone11.png");
        immagine.set(1, "31457281");


        assertThrows(InvalidFileException.class, () ->
                permutaServiceImpl.addPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, immagine));

    }

    @Test
    @SneakyThrows
    public void TC_GA1_9(){
        int ram = 12;
        int spazioArchiviazione = 128;
        int batteria = 75;
        String condizioneGenerale = "Ottima";
        String colore = "Bianco Perla";
        immagine.set(0, "iphone- 13.png");



        assertThrows(InvalidFileException.class, () ->
                permutaServiceImpl.addPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, immagine));

    }

    @Test
    @SneakyThrows
    public void TC_GA1_10() {
        int ram = 6;
        int spazioArchiviazione = 128;
        int batteria = 75;
        String condizioneGenerale = "Ottima";
        String colore = "Grigio Siderale";
        immagine.set(0, "iphone13.png");

        // recupera inserzione più economica del superprodotto
        InserzioneEntity inserzioneEntityMock = mock(InserzioneEntity.class);
        when(inserzioneEntityMock.getPrezzoBase()).thenReturn(1000.0);
        SuperProdottoEntity superProdottoEntityMock = mock(SuperProdottoEntity.class);
        when(superProdottoEntityMock.getCheapestInsertion()).thenReturn(inserzioneEntityMock);
        when(superProdottoRepository.findById(anyInt())).thenReturn(superProdottoEntityMock);

        assertDoesNotThrow(() ->
                permutaServiceImpl.addPermuta(ram, spazioArchiviazione, batteria, condizioneGenerale, colore, immagine));

        verify(permutaRepository, times(1)).save(any(PermutaEntity.class));
        verify(acquirenteRepository, times(1)).updateSaldoLambdaPoints(anyInt(), anyInt());
    }

}