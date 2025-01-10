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
import jakarta.transaction.Transactional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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


    @Transactional
    @PostConstruct
    public void init() {
        // Leggi il file JSON
        InputStream inputStream = getClass().getResourceAsStream("/json/dataLoader.json");
        if (inputStream == null) {
            throw new IllegalArgumentException("File data.json non trovato nella cartella resources!");
        }

        String jsonText = new Scanner(inputStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
        JSONObject data = new JSONObject(jsonText);

        // Inserisci le categorie
        JSONArray categories = data.getJSONArray("categories");
        for (int i = 0; i < categories.length(); i++) {
            JSONObject category = categories.getJSONObject(i);
            CategoriaEntity categoria = new CategoriaEntity();
            if (!categoriaRepository.existsByNome(category.getString("nome"))){
                categoria.setNome(category.getString("nome"));
                categoria.setImmagine(category.getString("immagine"));
                categoriaRepository.save(categoria);
            }
        }

        JSONArray superProdotti = data.getJSONArray("superProducts");
        for (int i = 0; i < superProdotti.length(); i++) {
            JSONObject superProduct = superProdotti.getJSONObject(i);
            SuperProdottoEntity superProdotto = new SuperProdottoEntity();
            if (!superProdottoRepository.existsByModello(superProduct.getString("modello"))){
                superProdotto.setMarca(superProduct.getString("marca"));
                superProdotto.setModello(superProduct.getString("modello"));
                superProdotto.setImmagine(superProduct.getString("immagine"));
                superProdotto.setCategoria(categoriaRepository.findByNome(superProduct.getString("categoria")));
                superProdottoRepository.save(superProdotto);
            }
        }

        JSONArray products = data.getJSONArray("products");
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


        JSONArray vendors = data.getJSONArray("vendors");
        for (int i = 0; i < vendors.length(); i++) {
            JSONObject vendor = vendors.getJSONObject(i);

            RivenditoreEntity rivenditore = new RivenditoreEntity();

            // Verifica l'esistenza delle chiavi necessarie
            if (vendor.has("partitaIva") && vendor.has("mail") && vendor.has("ragioneSociale") && vendor.has("password")) {
                if (rivenditoreRepository.findByPartitaIva(vendor.getString("partitaIva")) == null
                        || !rivenditoreRepository.existsByEmail(vendor.getString("mail"))
                        || !rivenditoreRepository.existsByRagioneSociale(vendor.getString("ragioneSociale"))) {

                    rivenditore.setPartitaIva(vendor.getString("partitaIva"));
                    rivenditore.setEmail(vendor.getString("mail"));
                    rivenditore.setRagioneSociale(vendor.getString("ragioneSociale"));
                    rivenditore.setPassword(vendor.getString("password"));

                    rivenditoreRepository.saveAndFlush(rivenditore);
                }
            } else {
                System.err.println("Oggetto vendor mancante di campi obbligatori: " + vendor);
            }
        }




        JSONArray insertions = data.getJSONArray("insertions");
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

            if (inserzioneRepository.findById(inserzioneEntityId).isEmpty()){
                inserzioneRepository.saveAndFlush(inserzione);
            }
        }

    }


}
