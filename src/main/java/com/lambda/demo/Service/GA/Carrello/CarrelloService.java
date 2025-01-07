package com.lambda.demo.Service.GA.Carrello;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import jakarta.servlet.http.HttpServletRequest;

public interface CarrelloService {
    void addToCart(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione,
                   String colore, HttpServletRequest req) throws Exception;
    void removeFromCart(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione,
                        String colore, HttpServletRequest req) throws Exception;
    void updateQuantity(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione,
                        String colore, int quantity, HttpServletRequest req) throws Exception;

    CarrelloEntity getCartByUser(int idAcquirente);

    void deleteCartByUser(int idAcquirente);

    void insertCartByUser(int idAcquirente, double prezzoProvvisorio);

    void insertItems(int idCarrello, int quantity, int ram, int spazioArchiviazione, int superProdottoId, String colore, String partitaIva);
}
