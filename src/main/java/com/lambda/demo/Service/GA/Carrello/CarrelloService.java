package com.lambda.demo.Service.GA.Carrello;

import com.lambda.demo.Entity.GA.Carrello.CarrelloEntity;
import jakarta.servlet.http.HttpServletRequest;

public interface CarrelloService {

    void addToCart(String partitaIvaRivenditore, String idSuperProdotto, String ram, String spazioArchiviazione, String colore, HttpServletRequest request) throws Exception;

    void removeFromCart(String partitaIvaRivenditore, String idSuperProdotto, String ram, String spazioArchiviazione,
                        String colore, HttpServletRequest req) throws Exception;

    void updateQuantity(String partitaIvaRivenditore, String idSuperProdotto, String ram, String spazioArchiviazione,
                        String colore, String quantity, HttpServletRequest req) throws Exception;

    CarrelloEntity getCartByUser(int idAcquirente);

    void deleteCartByUser(int idAcquirente);

    void insertCartByUser(int idAcquirente, double prezzoProvvisorio);

    void insertItems(int idCarrello, int quantity, int ram, int spazioArchiviazione, int superProdottoId, String colore, String partitaIva);
}
