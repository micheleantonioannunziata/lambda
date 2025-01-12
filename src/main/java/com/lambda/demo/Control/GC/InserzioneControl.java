package com.lambda.demo.Control.GC;


import com.lambda.demo.Entity.GC.Inserzione.InserzioneEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntity;
import com.lambda.demo.Entity.GC.Prodotto.ProdottoEntityId;
import com.lambda.demo.Entity.GC.SuperProdottoEntity;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Exception.GA.GestionePermuta.InvalidColorException;
import com.lambda.demo.Exception.GC.GCException;
import com.lambda.demo.Service.GC.Inserzione.InserzioneService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
public class InserzioneControl {

    @Autowired
    private SuperProdottoService superProdottoService;


    @Autowired
    private InserzioneService inserzioneService;


    @Autowired
    private RivenditoreService rivenditoreService;


    @RequestMapping(value = "/redirectToTechnicalRequirementsForm", method = RequestMethod.GET)
    public String redirectToTechnicalRequirementsForm(HttpServletRequest req, HttpServletResponse res, Model model) {
        String id = req.getParameter("id");
        List<ProdottoEntity> products = new ArrayList<>();


        products = superProdottoService.findProductsBySuperProdottoId(Integer.parseInt(id));


        Set<Integer> ramValues = new HashSet<>();
        Set<Integer> storageValues = new HashSet<>();
        Set<String> colorValues = new HashSet<>();

        for (ProdottoEntity p : products) {
            ramValues.add(p.getId().getRam());
            storageValues.add(p.getId().getSpazioArchiviazione());
            colorValues.add(p.getId().getColore());
        }


        model.addAttribute("ramValues", ramValues);
        model.addAttribute("storageValues", storageValues);
        model.addAttribute("colorValues", colorValues);

        model.addAttribute("idSuperProdotto", Integer.parseInt(id));


        return "technicalRequirementsForm";
    }

    @RequestMapping(value = "/techRequirements", method = RequestMethod.GET)
    public String techRequirements(HttpServletRequest req, HttpServletResponse res, Model model) throws GCException {
        String ram = req.getParameter("ram");
        String spazioArchiviazione = req.getParameter("storage");
        String colore = req.getParameter("color");
        String idSuperProdotto = req.getParameter("idSuperProdotto");

        try {
            inserzioneService.checkTechRequirements(idSuperProdotto, ram, spazioArchiviazione, colore);
        }catch (GCException | InvalidColorException gcException){
            throw new GCException(gcException.getMessage());
        }

        model.addAttribute("ram", ram);
        model.addAttribute("storage", spazioArchiviazione);
        model.addAttribute("color", colore);
        model.addAttribute("idSuperProdotto", idSuperProdotto);

        return "priceQuantityForm";
    }

    @RequestMapping(value = "/priceQuantity", method = RequestMethod.GET)
    public String priceQuantity(HttpServletRequest req, HttpServletResponse res, Model model) throws GCException {
        String ram = req.getParameter("ram");
        String storage = req.getParameter("spazioArchiviazione");
        String colore = req.getParameter("colore");
        String idSuperProdotto = req.getParameter("idSuperProdotto");
        String quantity = req.getParameter("quantity");
        String prezzoBase = req.getParameter("prezzoBase");
        String scontoStandard = req.getParameter("scontoStandard");
        String scontoPremium = req.getParameter("scontoPremium");



        try {
            inserzioneService.checkTechRequirements(idSuperProdotto, ram, storage, colore);
            inserzioneService.checkPriceQuantity(quantity, prezzoBase, scontoStandard, scontoPremium);
        } catch (GCException | InvalidColorException e) {
            throw new GCException(e.getMessage());
        }


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
    public String addInsertion(HttpServletRequest req, HttpServletResponse res, RedirectAttributes redirectAttributes) throws Exception {
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
                inserzioneService.addInserzione(prodottoEntity, prezzoBase, quantity, scontoStandard, scontoPremium);
            }catch (GCException e){
                throw new Exception(e.getMessage());
            }

        }

        redirectAttributes.addFlashAttribute("msg", "Inserzione aggiunta con successo!");
        return "redirect:/vendorArea";
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


        JSONArray jsonArray = new JSONArray();

        int index = 0;

        if (partitaIvaRivenditore != null){
            InserzioneEntity inserzioneEntity = null;
            inserzioneEntity = inserzioneService.getInsertionsCombinationsByVendor(partitaIvaRivenditore, idSuperProdotto,
                    Integer.parseInt(values.get("ram")), Integer.parseInt(values.get("storage")), values.get("color"));


            if(inserzioneEntity != null && inserzioneEntity.isDisponibilita()) {
                JSONObject obj = new JSONObject();
                obj.put("prezzo", inserzioneEntity.returnDiscountedPrice(SessionManager.getAcquirente(req).isPremium()));
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