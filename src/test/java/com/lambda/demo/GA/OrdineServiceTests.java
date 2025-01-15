package com.lambda.demo.GA;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GA.Ordine.OrdineEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Exception.GA.GestioneOrdini.*;
import com.lambda.demo.Repository.GA.Carrello.CarrelloRepository;
import com.lambda.demo.Repository.GA.Ordine.ComposizioneRepository;
import com.lambda.demo.Repository.GA.Ordine.OrdineRepository;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Service.GA.Ordine.OrdineServiceImpl;
import com.lambda.demo.Utility.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@SpringBootTest
public class OrdineServiceTests {

    @InjectMocks
    private OrdineServiceImpl ordineService;

    @Mock
    private OrdineRepository ordineRepository;

    @Mock
    private AcquirenteRepository acquirenteRepository;

    @Mock
    private CarrelloRepository carrelloRepository;

    @Mock
    private ComposizioneRepository composizioneRepository;

    @Mock
    private HttpServletRequest request;

    @Mock
    private AcquirenteEntity acquirente;

    @Mock
    private CarrelloEntity carrelloEntity;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        HttpSession session = Mockito.mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        when(request.getAttribute("lambda")).thenReturn(false);

        AcquirenteEntity acquirente = new AcquirenteEntity();
        acquirente.setEmail("test@example.com");
        when(acquirenteRepository.findByEmail(anyString())).thenReturn(acquirente);

        when(SessionManager.getAcquirente(request)).thenReturn(acquirente);
        carrelloEntity.setId(1);
        when(SessionManager.getCarrello(request)).thenReturn(carrelloEntity);
        when(carrelloEntity.getCarrelloItems()).thenReturn(new ArrayList<>());

        when(composizioneRepository.saveAll(anyIterable())).thenReturn(null);
    }


    @Test
    @SneakyThrows
    public void TC_GA2_1() {
        assertThrows(InvalidReceiverException.class, () ->
                ordineService.checkoutFinalization("Luca21 Romano", "Via Roma 123, 12345 Ottaviano",
                        "Luca Romano", "1234567812345678", "741",
                        "08/2026"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_2() {
        assertThrows(InvalidReceiverException.class, () ->
                ordineService.checkoutFinalization("Lu A", "Via Napoleone 3, 80040 Napoli",
                        "Luigi Auricchio", "1234567812345678", "703",
                        "09/2029"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_3() {
        assertThrows(InvalidReceiverException.class, () ->
                ordineService.checkoutFinalization("Leonardo Mario Stefano Alessandro Claudio Gianmarco Romolo Roberto Antonio Rossini", "Via Tulipani 13, 80044 Roma",
                        "Leonardo Rossini", "4325646478532316", "907",
                        "10/2028"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_4() {
        assertThrows(InvalidAddressException.class, () ->
                ordineService.checkoutFinalization("Luca Franzese", "Via Carbone 123, 12674 ",
                        "Luca Franzese", "4521963521040224", "130",
                        "02/2026"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_5() {
        assertThrows(InvalidAddressException.class, () ->
                ordineService.checkoutFinalization("Luigi Franzese", "Via Carbone 73, 8004 Roma",
                        "Luigi Franzese", "1234567812789763", "741",
                        "10/2029"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_6() {
        assertThrows(InvalidAddressException.class, () ->
                ordineService.checkoutFinalization("Francesco Totti", "P 1, 12345 Rho",
                        "Francesco Totti", "1234567812345678", "720",
                        "11/2029"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_7() {
        assertThrows(InvalidAddressException.class, () ->
                ordineService.checkoutFinalization("Michele Russo", "Via Roma tulipani milano settembre Carbone tajani Romolo Remo Cristiano Ronaldo dos Aveiros do Rinascimento Jose Dalima Dos Santos Si Erre Siette Major De Meffi Sneijder Roberto Carlos Inter Amala Gertrude Forza Giuve Sium Astolfo sulla Luna 123, 12345 Roma ",
                        "Michele Russo", "6743246314783268", "680",
                        "10/2027"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_8() {
        assertThrows(InvalidCardHolderException.class, () ->
                ordineService.checkoutFinalization("Luigi Rossi", "Via Settembre 13, 80049 Napoli",
                        "Luigi21 Rossi", "1234567812345678", "534",
                        "12/2029"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_9() {
        assertThrows(InvalidCardHolderException.class, () ->
                ordineService.checkoutFinalization("Luigi Rossi", "Via Roma 123, 12345 Verona",
                        "Lu R", "4920243823930432", "180",
                        "08/2027"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_10() {
        assertThrows(InvalidCardHolderException.class, () ->
                ordineService.checkoutFinalization("Raffaele Cirillo", "Via Milano 3, 80043 Firenze",
                        "Raffaele Luca Giulio Alessandro Gianluca Leonardo Michele Antonio Vincenzo Cirillo", "1312539302940294", "492",
                        "07/2026"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_11() {
        assertThrows(InvalidCardNumberException.class, () ->
                ordineService.checkoutFinalization("Luigi Franzese", "Via Carbone 73, 80040 Messina",
                        "Luigi Franzese", "1234567812abcdef", "741",
                        "10/2029"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_12() {
        assertThrows(InvalidCardNumberException.class, () ->
                ordineService.checkoutFinalization("Mario Draghi", "Via Carbone 73, 80040 Trieste",
                        "Luigi Franzese", "1234567812", "435",
                        "11/2029"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_13() {
        assertThrows(InvalidCVVException.class, () ->
                ordineService.checkoutFinalization("John Snow", "Via Rossi 100, 80040 Torino",
                        "John Snow", "1234567812345678", "6ab",
                        "01/2026"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_14() {
        assertThrows(InvalidCVVException.class, () ->
                ordineService.checkoutFinalization("Luigi Franzese", "Via Carbone 73, 80040 Benevento",
                        "Luigi Franzese", "1234567812324526", "7416",
                        "10/2028"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_15() {
        assertThrows(InvalidExpirationDateException.class, () ->
                ordineService.checkoutFinalization("Giusy Meloni", "Via Cento 10, 80034 Napoli",
                        "Giusy Meloni", "1234567812345678", "730",
                        "8/2"));
    }

    @Test
    @SneakyThrows
    public void TC_GA2_16() {
        assertDoesNotThrow(() ->
                ordineService.checkoutFinalization("Salvatore Farina", "Via Matematica 123, 84016 Pagani",
                        "Salvatore Farina", "1234567812345678", "741",
                        "08/2026"));

        verify(ordineRepository, times(1)).save(any(OrdineEntity.class));
        verify(composizioneRepository, times(1)).saveAll(anyIterable());
        verify(carrelloRepository, times(1)).deleteCarrelloByAcquirente(anyInt());
        verify(carrelloRepository, times(1)).insert(anyInt(), anyDouble());

    }


}
