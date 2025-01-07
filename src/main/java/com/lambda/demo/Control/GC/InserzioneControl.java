package com.lambda.demo.Control.GC;


import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntityId;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Exception.GC.GCException;
import com.lambda.demo.Service.GC.Inserzione.InserzioneService;
import com.lambda.demo.Service.GC.Prodotto.ProdottoService;
import com.lambda.demo.Service.GC.SuperProdotto.SuperProdottoService;
import com.lambda.demo.Service.GPR.Rivenditore.RivenditoreService;
import com.lambda.demo.Utility.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class InserzioneControl {

    @Autowired
    private SuperProdottoService superProdottoService;


    @Autowired
    private InserzioneService inserzioneService;

    @Autowired
    private ProdottoService prodottoService;


    @Autowired
    private RivenditoreService rivenditoreService;


    @RequestMapping(value = "/searchProductInfo", method = RequestMethod.GET)
    public String searchProductInfo(HttpServletRequest req, HttpServletResponse res, Model model) {
        String id = req.getParameter("id");
        List<ProdottoEntity> products = new ArrayList<>();


        products = superProdottoService.findProductsBySuperProdottoId(Integer.parseInt(id));


        Set<Integer> ramValues = products.stream()
                .map(p -> p.getId().getRam())
                .collect(Collectors.toSet());

        Set<Integer> storageValues = products.stream()
                .map(p -> p.getId().getSpazioArchiviazione())
                .collect(Collectors.toSet());

        Set<String> colorValues = products.stream()
                .map(p -> p.getId().getColore())
                .collect(Collectors.toSet());

        model.addAttribute("ramValues", ramValues);
        model.addAttribute("storageValues", storageValues);
        model.addAttribute("colorValues", colorValues);

        model.addAttribute("idSuperProdotto", Integer.parseInt(id));


        return "technicalRequirementsForm";
    }


    @RequestMapping(value = "/searchCombinations", method = RequestMethod.GET)
    public void searchCombinations(HttpServletRequest req, HttpServletResponse res, Model model) throws IOException {
        String idReq = req.getParameter("idSuperProdotto");
        int idSuperProdotto = Integer.parseInt(idReq);

        String partitaIvaRivenditore = req.getParameter("partitaIvaRivenditore");

        String firstType = req.getParameter("firstType");
        String firstOption = req.getParameter("firstOption");

        String secondType = req.getParameter("secondType");
        String secondOption = req.getParameter("secondOption");

        String thirdType = req.getParameter("thirdType");
        String thirdOption = req.getParameter("thirdOption");


        String wrapper = firstType + ":" + firstOption + ";" +
                secondType + ":" + secondOption + ";" +
                thirdType + ":" + thirdOption + ";";


        List<ProdottoEntity> products = new ArrayList<>();

        Map<String, String> values = new HashMap<>();

        // Dividi il wrapper in base a ';'
        String[] pairs = wrapper.split(";");

        for (String pair : pairs) {
            if (!pair.isEmpty()) {
                String[] keyValue = pair.split(":", 2); // Dividi in base a ':'
                String key = keyValue[0].trim(); // La chiave (es: ram)
                String value = keyValue.length > 1 ? keyValue[1].trim() : ""; // Il valore (può essere vuoto)
                values.put(key, value.isEmpty() ? "" : value); // Assegna null se il valore è vuoto
            }
        }

        // Output di debug per verificare i valori
        System.out.println("RAM: " + values.get("ram"));
        System.out.println("Storage: " + values.get("storage"));
        System.out.println("Color: " + values.get("color"));


        JSONArray jsonArray = new JSONArray();

        int index = 0;

        if (partitaIvaRivenditore != null){
            System.out.println("PartitaIvaRivenditore: " + partitaIvaRivenditore);

            InserzioneEntity inserzioneEntity = null;
            inserzioneEntity = inserzioneService.getInsertionsCombinationsByVendor(partitaIvaRivenditore, idSuperProdotto,
                    Integer.parseInt(values.get("ram")), Integer.parseInt(values.get("storage")), values.get("color"));


            if(inserzioneEntity != null) {
                System.out.println("InserzioneEntity: " + inserzioneEntity.getPrezzoBase());

                JSONObject obj = new JSONObject();
                obj.put("prezzo", inserzioneEntity.getPrezzoBase());
                jsonArray.put(obj);
            }
        }else{

            // Chiamata al servizio per trovare le combinazioni
            products = superProdottoService.findProductsCombinations(
                    idSuperProdotto,
                    values.getOrDefault("ram", ""),
                    values.getOrDefault("storage", ""),
                    values.getOrDefault("color", "")
            );


            for(ProdottoEntity prod : products) {

                JSONObject obj = new JSONObject();

                obj.put("ram"+index, prod.getId().getRam());

                System.out.println("ram" + index + ":" + prod.getId().getRam());

                obj.put("storage"+index, prod.getId().getSpazioArchiviazione());
                obj.put("color"+index, prod.getId().getColore());

                obj.put("idSuperProdotto", idSuperProdotto);

                jsonArray.put(obj);
                index++;
            }
        }




        res.setContentType("application/json");
        PrintWriter out = res.getWriter();
        out.println(jsonArray);
    }




    @RequestMapping(value = "/techRequirements", method = RequestMethod.GET)
    public String techRequirements(HttpServletRequest req, HttpServletResponse res, Model model) {
        String ramReq = req.getParameter("ram");
        String storageReq = req.getParameter("storage");
        String color = req.getParameter("color");
        String idSuperProdottoReq = req.getParameter("idSuperProdotto");
        int idSuperProdotto = Integer.parseInt(idSuperProdottoReq);


        int ram = Integer.parseInt(ramReq);
        int storage = Integer.parseInt(storageReq);


        System.out.println("RAM: " + ram);
        System.out.println("Storage: " + storage);
        System.out.println("Color: " + color);

        model.addAttribute("ram", ram);
        model.addAttribute("storage", storage);
        model.addAttribute("color", color);
        model.addAttribute("idSuperProdotto", idSuperProdotto);

        return "priceQuantityForm";
    }

    @RequestMapping(value = "/priceQuantity", method = RequestMethod.GET)
    public String priceQuantity(HttpServletRequest req, HttpServletResponse res, Model model) {
        String ramReq = req.getParameter("ram");
        String storageReq = req.getParameter("spazioArchiviazione");
        String colore = req.getParameter("colore");
        String idSuperProdottoReq = req.getParameter("idSuperProdotto");
        int idSuperProdotto = Integer.parseInt(idSuperProdottoReq);


        System.out.println("RAM: " + ramReq);
        System.out.println(storageReq);
        System.out.println(colore);
        System.out.println(idSuperProdotto);

        //SANNA FA E CONTROLLI
        int ram = Integer.parseInt(ramReq);
        int storage = Integer.parseInt(storageReq);

        String quantityReq = req.getParameter("quantity");
        int quantity = Integer.parseInt(quantityReq);

        String prezzoReq = req.getParameter("prezzoBase");
        double prezzoBase = Double.parseDouble(prezzoReq);


        String scontoStandardReq = req.getParameter("scontoStandard");
        int scontoStandard = Integer.parseInt(scontoStandardReq);

        String scontoPremiumReq = req.getParameter("scontoPremium");
        int scontoPremium = Integer.parseInt(scontoPremiumReq);


        model.addAttribute("ram", ram);
        model.addAttribute("spazioArchiviazione", storage);
        model.addAttribute("colore", colore);
        model.addAttribute("idSuperProdotto", idSuperProdotto);
        model.addAttribute("quantity", quantity);
        model.addAttribute("prezzoBase", prezzoBase);
        model.addAttribute("scontoStandard", scontoStandard);
        model.addAttribute("scontoPremium", scontoPremium);


        return "addInsertionSummary";
    }

    @RequestMapping(value = "/addInsertion", method = RequestMethod.POST)
    public String addInsertion(HttpServletRequest req, HttpServletResponse res) throws Exception {
        String action = req.getParameter("action");
        if (action.equals("conferma")) {

            String ram = req.getParameter("ram");
            String storage = req.getParameter("spazioArchiviazione");
            String colore = req.getParameter("colore");
            int idSuperProdotto = Integer.parseInt(req.getParameter("idSuperProdotto"));
            String quantity = req.getParameter("quantity");
            String prezzoBase = req.getParameter("prezzoBase");
            String scontoStandard = req.getParameter("scontoStandard");
            String scontoPremium = req.getParameter("scontoPremium");
            String partitaIva = SessionManager.getRivenditore(req).getPartitaIva();

            ProdottoEntityId prodottoEntityId = new ProdottoEntityId(idSuperProdotto, Integer.parseInt(ram), Integer.parseInt(storage), colore);

            ProdottoEntity prodottoEntity = new ProdottoEntity();
            prodottoEntity.setId(prodottoEntityId);


            try {
                inserzioneService.checkAddInserzione(prodottoEntity,
                        prezzoBase, quantity, scontoStandard, scontoPremium);


                inserzioneService.addInserzione(prodottoEntity, partitaIva, prezzoBase, quantity, scontoStandard, scontoPremium);
            }catch (GCException e){
                throw new Exception(e.getMessage());
            }


            InserzioneEntityId inserzioneEntityId = new InserzioneEntityId(partitaIva, prodottoEntityId);
            InserzioneEntity justAddedInsertion = new InserzioneEntity();
            justAddedInsertion = inserzioneService.findById(inserzioneEntityId);

            RivenditoreEntity rivenditore = SessionManager.getRivenditore(req);
            rivenditore.getInserzioni().add(justAddedInsertion);

            SessionManager.setRivenditore(req, rivenditore);


        }

        return "redirect:/vendorArea";
    }


    @RequestMapping (value = "/redirectToInsertionOverview", method = RequestMethod.POST)
    public String redirectToInsertionOverview(HttpServletRequest req, HttpServletResponse res, Model model) {
        String id = req.getParameter("id");
        String partitaIva = req.getParameter("partitaIva");

        SuperProdottoEntity superProdotto = superProdottoService.findById(Integer.parseInt(id));
        InserzioneEntity inserzioneEntity = new InserzioneEntity();


        List<String> colors = new ArrayList<>();
        List<Integer> ram = new ArrayList<>();
        List<Integer> storages = new ArrayList<>();





        if (partitaIva != null){
            RivenditoreEntity rivenditore = rivenditoreService.findByPartitaIva(partitaIva);
            if (rivenditore != null){

                inserzioneEntity = rivenditore.getCheapestInsertion(Integer.parseInt(id));


                model.addAttribute("insertion", inserzioneEntity);
            }

        }else {

            inserzioneEntity = superProdotto.getCheapestInsertion();

            model.addAttribute("insertion", inserzioneEntity);

        }


        List<InserzioneEntity> insertionsByVendor = inserzioneEntity.getRivenditore().getInsertionsBySuperProductId(inserzioneEntity.getProdotto().getSuperProdotto().getId());
        for (InserzioneEntity insertion : insertionsByVendor) {
            if(!colors.contains(insertion.getId().getIdProdotto().getColore()))
                colors.add(insertion.getId().getIdProdotto().getColore());

            if (!ram.contains(insertion.getId().getIdProdotto().getRam()))
                ram.add(insertion.getId().getIdProdotto().getRam());

            if(!storages.contains(insertion.getId().getIdProdotto().getSpazioArchiviazione()))
                storages.add(insertion.getId().getIdProdotto().getSpazioArchiviazione());
        }

        colors.sort(Comparator.comparing(String::toString));
        ram.sort(Comparator.comparingInt(Math::abs));
        storages.sort(Comparator.comparingInt(Math::abs));

        model.addAttribute("colors", colors);
        model.addAttribute("rams", ram);
        model.addAttribute("storages", storages);


        model.addAttribute("activeColor", inserzioneEntity.getId().getIdProdotto().getColore());
        model.addAttribute("activeRam", inserzioneEntity.getId().getIdProdotto().getRam());
        model.addAttribute("activeStorage", inserzioneEntity.getId().getIdProdotto().getSpazioArchiviazione());



        List<InserzioneEntity> inserzioni = new ArrayList<>();
        inserzioni = inserzioneService.getInsertionsByIdSuperProdotto(superProdotto.getId());
        inserzioni.remove(inserzioneEntity);



        model.addAttribute("inserzioni", inserzioni);

        List<RivenditoreEntity> rivenditori = superProdottoService.findVendorsBySuperProdottoId(superProdotto.getId());
        rivenditori.remove(inserzioneEntity.getRivenditore());

        model.addAttribute("rivenditori", rivenditori);

        model.addAttribute("idSuperProdotto", id);

        return "insertionOverview";
    }

}