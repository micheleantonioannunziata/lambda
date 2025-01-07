package com.lambda.demo.Service.GA.Carrello;

import jakarta.servlet.http.HttpServletRequest;

public interface CarrelloService {
    void addToCart(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione,
                   String colore, HttpServletRequest req) throws Exception;
    void removeFromCart(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione,
                        String colore, HttpServletRequest req) throws Exception;
    void updateQuantity(String partitaIvaRivenditore, int idSuperProdotto, int ram, int spazioArchiviazione,
                        String colore, int quantity, HttpServletRequest req) throws Exception;
}
