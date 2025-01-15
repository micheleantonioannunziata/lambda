package com.lambda.demo.GPR;

import com.lambda.demo.Exception.GPR.AccessoAcquirente.*;
import com.lambda.demo.Exception.GPR.InvalidEmailException;
import com.lambda.demo.Exception.GPR.InvalidPasswordException;
import com.lambda.demo.Exception.GPR.UnMatchedPasswordException;
import com.lambda.demo.Repository.GPR.AcquirenteRepository;
import com.lambda.demo.Service.GPR.Acquirente.AcquirenteServiceImpl;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
public class AcquirenteServiceTests {

    @Mock
    private AcquirenteRepository acquirenteRepository;

    @InjectMocks
    private AcquirenteServiceImpl acquirenteServiceImpl;

    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(acquirenteRepository.findByEmail(anyString())).thenReturn(null);
    }

    @SneakyThrows
    @Test
    public void TC_GPR_1() {
        assertThrows(InvalidNameException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Antonio242","Auriemma","antoniospatiero12@gmail.com","Antoniospa1!","Antoniospa1!" ));
    }



    @Test
    public void TC_GPR_2() {
        assertThrows(InvalidNameException.class,
                () -> acquirenteServiceImpl.signupAcquirente("A","Coppola","antoniocoppola12@gmail.com","Antoniocop1!", "Antoniocop1!"));
    }

    @Test
    public void TC_GPR_3() {
        assertThrows(InvalidNameException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Alessandro Giuseppe Leonardo Federico Matteo Vittorio Romano","Cavani","alessandroromano1@gmail.com","Alessandrorom3!", "Alessandrorom3!"));
    }


    @Test
    public void TC_GPR_4() {
        assertThrows(InvalidSurnameException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Alessia","Cop487","alessiacoppola@gmail.com","Alessiarom1!", "Alessiarom1!"));
    }


    @Test
    public void TC_GPR_5() {
        assertThrows(InvalidSurnameException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Antonella","E","antonellaelanga@gmail.com","Antonellael1!", "Antonellael1!"));
    }


    @Test
    public void TC_GPR_6() {
        assertThrows(InvalidSurnameException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Cristiano","Ronaldo dos Santos Aveiro Carvajal Benzema Do Renascimento Edinson Cavani Gonzalez","cristianoromano@gmail.com","Cristianorom4!", "Cristianorom4!"));
    }


    @Test
    public void TC_GPR_7() {
        assertThrows(InvalidEmailException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Giulia","Lewandowska","giulialewagmail.com","Giulialewa1!", "Giulialewa1!"));
    }

    @Test
    public void TC_GPR_8() {
        assertThrows(InvalidEmailException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Giulio","Golia","g@uk.co","giulioGol1!", "giulioGol1!"));
    }


    @Test
    public void TC_GPR_9() {
        assertThrows(InvalidEmailException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Gianluigi","Rossini","GianluigiGiuseppeLeonardoFedericoMatteoVittorioRossini1@gmail.com","GianRos3!",  "GianRos3!"));
    }


    @Test
    public void TC_GPR_10() {
        assertThrows(InvalidPasswordException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Luca","De Rosa","LucaGuida@gmail.com","Abcdef123", "Abcdef123"));
    }

    @Test
    public void TC_GPR_11() {
        assertThrows(InvalidPasswordException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Mario","Franzese","MarioFranzese@gmail.com","Abc123!", "Abc123!"));
    }

    @Test
    public void TC_GPR_12() {
        assertThrows(InvalidPasswordException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Mario","Franzesini","MarioFranzesini@gmail.com","MarioFranzesiniMarioFranzesiniMarioFranzesini12345!", "MarioFranzesiniMarioFranzesiniMarioFranzesini12345!"));
    }

    @Test
    public void TC_GPR_13() {
        assertThrows(UnMatchedPasswordException.class,
                () -> acquirenteServiceImpl.signupAcquirente("Luigi","Rossi","LuigiRossi@gmail.com","LuigiRossi123!", "LuigiRossi12!"));
    }

    @Test
    public void TC_GPR_14() {
        assertDoesNotThrow(() -> acquirenteServiceImpl.signupAcquirente("Gianmario", "Corrado", "GianCor@gmail.com", "GianCor12!", "GianCor12!"));
    }

}
