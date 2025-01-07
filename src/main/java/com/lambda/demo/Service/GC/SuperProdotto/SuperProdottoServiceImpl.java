package com.lambda.demo.Service.GC.SuperProdotto;

import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Repository.GC.CategoriaRepository;
import com.lambda.demo.Repository.GC.SuperProdottoRepository;
import com.lambda.demo.Repository.GPR.RivenditoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SuperProdottoServiceImpl implements SuperProdottoService{
    @Autowired
    private SuperProdottoRepository superProdottoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private RivenditoreRepository rivenditoreRepository;

    @Override
    public List<SuperProdottoEntity> findByName(String name) {
        return superProdottoRepository.findByName(name);
    }

    @Override
    public List<ProdottoEntity> findProductsBySuperProdottoId(int id) {
        SuperProdottoEntity superProdotto = null;
        List<ProdottoEntity> products = new ArrayList<>();
        if(superProdottoRepository.findById(id) != null) {
            superProdotto = superProdottoRepository.findById(id);
            products.addAll(superProdotto.getProdotti());
        }

        return products;
    }

    @Override
    public List<SuperProdottoEntity> findByNameAndCategory(String name, int idCategoria) {
        //CategoriaEntity categoriaEntity = categoriaRepository.findById(idCategoria);
        return superProdottoRepository.findByNameAndIdCategoria(name, idCategoria);
    }

    @Override
    public List<ProdottoEntity> findProductsCombinations(int idSuperProdotto, String ram, String spazioArchiviazione, String colore) {
        // Recupera i prodotti associati al superProdotto
        List<ProdottoEntity> products = findProductsBySuperProdottoId(idSuperProdotto);

        // Converte i parametri in formato numerico solo se validi
        int ramInt = 0;
        int spazioArchiviazioneInt = 0;

        try {
            ramInt = !ram.isBlank() ? Integer.parseInt(ram) : 0;
        } catch (NumberFormatException e) {
            System.err.println("Invalid RAM value: " + ram);
        }

        try {
            spazioArchiviazioneInt = !spazioArchiviazione.isBlank() ? Integer.parseInt(spazioArchiviazione) : 0;
        } catch (NumberFormatException e) {
            System.err.println("Invalid storage value: " + spazioArchiviazione);
        }

        // Lista per i prodotti filtrati
        List<ProdottoEntity> filteredProducts = new ArrayList<>();

        // Applica i filtri
        for (ProdottoEntity prod : products) {
            boolean matches = true;

            // Filtra per RAM
            if (ramInt > 0 && prod.getId().getRam() != ramInt) {
                matches = false;
            }

            // Filtra per Spazio di Archiviazione
            if (spazioArchiviazioneInt > 0 && prod.getId().getSpazioArchiviazione() != spazioArchiviazioneInt) {
                matches = false;
            }

            // Filtra per Colore
            if (!colore.isBlank() && !prod.getId().getColore().equalsIgnoreCase(colore)) {
                matches = false;
            }

            // Aggiungi il prodotto se soddisfa tutti i criteri
            if (matches) {
                filteredProducts.add(prod);
            }
        }

        // Restituisce i prodotti filtrati
        return filteredProducts;
    }

    @Override
    public SuperProdottoEntity findById(int id) {
        return superProdottoRepository.findById(id);
    }

    @Override
    public List<RivenditoreEntity> findVendorsBySuperProdottoId(int id) {
        List<RivenditoreEntity> vendors = rivenditoreRepository.findDistinctBySuperProdottoId(id);

        if (vendors == null) vendors = new ArrayList<>();

        return vendors;
    }
}
