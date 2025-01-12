package com.lambda.demo;

import com.lambda.demo.Entity.GC.CategoriaEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Repository.GC.CategoriaRepository;
import com.lambda.demo.Repository.GC.Inserzione.InserzioneRepository;
import com.lambda.demo.Repository.GC.Prodotto.ProdottoRepository;
import com.lambda.demo.Repository.GC.SuperProdottoRepository;
import com.lambda.demo.Repository.GPR.RivenditoreRepository;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Scanner;

@Component
public class DataLoader {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private SuperProdottoRepository superProdottoRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private RivenditoreRepository rivenditoreRepository;

    @Autowired
    private InserzioneRepository inserzioneRepository;


    /**
     * gestisce la logica di parsing di "dataLoader.json" per popolare le tabelle
     * @throws Exception eccezione generica
     * @see Exception
     */
    @PostConstruct
    @Transactional
    public void init() throws Exception {
        try (InputStream inputStream = getClass().getResourceAsStream("/json/dataLoader.json")) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File dataLoader.json non trovato nella cartella resources!");
            }

            String jsonText = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
            JSONObject data = new JSONObject(jsonText);

            if (data.getString("insert").equalsIgnoreCase("y")){
                loadCategories(data.getJSONArray("categories"));
                loadSuperProducts(data.getJSONArray("superProducts"));
                loadProducts(data.getJSONArray("products"));
                loadVendors(data.getJSONArray("vendors"));
                loadInsertions(data.getJSONArray("insertions"));

                System.out.println("Dati caricati con successo.");
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private void loadCategories(JSONArray categories) {
        for (int i = 0; i < categories.length(); i++) {
            JSONObject category = categories.getJSONObject(i);
            CategoriaEntity categoria = new CategoriaEntity();
            if (!categoriaRepository.existsByNome(category.getString("nome"))){
                categoria.setNome(category.getString("nome"));
                categoria.setImmagine(category.getString("immagine"));
                categoriaRepository.save(categoria);
            }
        }
    }

    private void loadSuperProducts(JSONArray superProducts) {
        for (int i = 0; i < superProducts.length(); i++) {
            JSONObject superProduct = superProducts.getJSONObject(i);
            SuperProdottoEntity superProdotto = new SuperProdottoEntity();
            if (!superProdottoRepository.existsByModello(superProduct.getString("modello"))){
                superProdotto.setMarca(superProduct.getString("marca"));
                superProdotto.setModello(superProduct.getString("modello"));
                superProdotto.setImmagine(superProduct.getString("immagine"));
                superProdotto.setCategoria(categoriaRepository.findByNome(superProduct.getString("categoria")));
                superProdottoRepository.save(superProdotto);
            }
        }
    }

    private void loadProducts(JSONArray products){
        for (int i = 0; i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);
            ProdottoEntity prodotto = new ProdottoEntity();
            ProdottoEntityId prodottoEntityId = new ProdottoEntityId();
            prodottoEntityId.setSuperProdottoId(superProdottoRepository.findByModello(product.getString("superProdottoModello")).getId());
            prodottoEntityId.setRam(product.getInt("ram"));
            prodottoEntityId.setSpazioArchiviazione(product.getInt("spazioArchiviazione"));
            prodottoEntityId.setColore(product.getString("colore"));

            prodotto.setId(prodottoEntityId);
            prodotto.setSuperProdotto(superProdottoRepository.findById(prodottoEntityId.getSuperProdottoId()));

            if (prodottoRepository.findById(prodottoEntityId).isEmpty()){
                prodottoRepository.save(prodotto);
            }
        }
    }


    private void loadVendors(JSONArray vendors) {
        for (int i = 0; i < vendors.length(); i++) {
            JSONObject vendor = vendors.getJSONObject(i);

            RivenditoreEntity rivenditore = new RivenditoreEntity();

            rivenditore.setPartitaIva(vendor.getString("partitaIva"));
            rivenditore.setEmail(vendor.getString("mail"));
            rivenditore.setRagioneSociale(vendor.getString("ragioneSociale"));
            rivenditore.setPassword(vendor.getString("password"));

            rivenditoreRepository.saveAndFlush(rivenditore);

        }
    }


    private void loadInsertions(JSONArray insertions){
        for (int i = 0; i < insertions.length(); i++) {
            JSONObject insertion = insertions.getJSONObject(i);
            JSONObject product = insertion.getJSONObject("prodotto");


            InserzioneEntity inserzione = new InserzioneEntity();

            InserzioneEntityId inserzioneEntityId = new InserzioneEntityId();
            inserzioneEntityId.setPartitaIvaRivenditore(insertion.getString("partitaIva"));
            inserzioneEntityId.setIdProdotto(new ProdottoEntityId((superProdottoRepository.findByModello(product.getString("superProdottoModello")).getId()),
                    product.getInt("ram"), product.getInt("spazioArchiviazione"), product.getString("colore")));


            inserzione.setId(inserzioneEntityId);
            inserzione.setRivenditore(rivenditoreRepository.findByPartitaIva(inserzioneEntityId.getPartitaIvaRivenditore()));
            inserzione.setProdotto(prodottoRepository.findById(inserzioneEntityId.getIdProdotto()).get());


            inserzione.setPrezzoBase(insertion.getInt("prezzo"));
            inserzione.setScontoStandard(insertion.getInt("scontoStandard"));
            inserzione.setScontoPremium(insertion.getInt("scontoPremium"));
            inserzione.setQuantita(insertion.getInt("quantità"));
            inserzione.setDataPubblicazione(LocalDateTime.now());
            inserzione.setDisponibilita(true);

            if (inserzioneRepository.findById(inserzioneEntityId).isEmpty()){
                inserzioneRepository.saveAndFlush(inserzione);
            }
        }

    }
}