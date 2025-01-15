package com.lambda.demo.GC;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Exception.GC.GestioneInserzione.*;
import com.lambda.demo.Repository.GC.Inserzione.InserzioneRepository;
import com.lambda.demo.Repository.GC.Prodotto.ProdottoRepository;
import com.lambda.demo.Repository.GPR.RivenditoreRepository;
import com.lambda.demo.Service.GC.Inserzione.InserzioneServiceImpl;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class InserzioneServiceTests {

    @InjectMocks
    private InserzioneServiceImpl inserzioneService;

    @Mock
    private RivenditoreRepository rivenditoreRepository;

    @Mock
    private ProdottoRepository prodottoRepository;

    @Mock
    private InserzioneRepository inserzioneRepository;

    @Mock
    private RivenditoreEntity rivenditoreEntity;

    @Mock
    private ProdottoEntity prodotto;

    @Mock
    private HttpServletRequest request;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);

        when(SessionManager.getRivenditore(request)).thenReturn(rivenditoreEntity);
        when(rivenditoreRepository.findByPartitaIva(anyString())).thenReturn(rivenditoreEntity);
        when(rivenditoreEntity.getPartitaIva()).thenReturn("123456789");
        when(rivenditoreEntity.getInserzioni()).thenReturn(new ArrayList<>());

        ProdottoEntityId prodottoEntityId = mock(ProdottoEntityId.class);
        when(prodotto.getId()).thenReturn(prodottoEntityId);
        when(prodottoEntityId.getSuperProdottoId()).thenReturn(1);
        when(prodottoEntityId.getRam()).thenReturn(2);
        when(prodottoEntityId.getColore()).thenReturn("giallo");
        when(prodottoEntityId.getSpazioArchiviazione()).thenReturn(128);
        when(prodottoRepository.findById(any(ProdottoEntityId.class))).thenReturn(Optional.of(prodotto));
    }

    @SneakyThrows
    @Test
    public void TC_GC_1() {
        when(prodottoRepository.findById(any(ProdottoEntityId.class))).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () ->
                inserzioneService.addInserzione(prodotto, "60.50", "50", "30", "40")
        );
    }

    @SneakyThrows
    @Test
    public void TC_GC_2() {
        assertThrows(InvalidQuantityException.class, () ->
                inserzioneService.addInserzione(prodotto, "300.50", "0", "60", "65")
        );

    }

    @SneakyThrows
    @Test
    public void TC_GC_3() {
        assertThrows(InvalidPriceException.class, () ->
                inserzioneService.addInserzione(prodotto, "10060.50", "550", "40", "45")
        );
    }

    @SneakyThrows
    @Test
    public void TC_GC_4() {
        assertThrows(InvalidPriceException.class, () ->
                inserzioneService.addInserzione(prodotto, "0.00", "300", "50", "55")
        );
    }

    @SneakyThrows
    @Test
    public void TC_GC_5(){
        assertThrows(InvalidStandardDiscountException.class, () ->
                inserzioneService.addInserzione(prodotto, "360.50", "256", "-50", "45")
        );
    }

    @SneakyThrows
    @Test
    public void TC_GC_6(){
        assertThrows(InvalidPremiumDiscountException.class, () ->
                inserzioneService.addInserzione(prodotto,"460.50", "405", "40", "100")
        );
    }

    @SneakyThrows
    @Test
    public void TC_GC_7(){
        assertDoesNotThrow(() ->
                inserzioneService.addInserzione(prodotto,"660.50", "500", "40", "45")
        );

        verify(rivenditoreRepository, times(1)).findByPartitaIva(anyString());
        verify(inserzioneRepository, times(1)).save(any(InserzioneEntity.class));
    }
}