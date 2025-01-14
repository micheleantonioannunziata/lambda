package com.lambda.demo;

import com.lambda.demo.Entity.GPR.AcquirenteEntity;
import com.lambda.demo.Entity.GPR.RivenditoreEntity;
import com.lambda.demo.Service.FileService;
import com.lambda.demo.Utility.SessionManager;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Set;

@Slf4j
public class AccessControlFilter implements Filter {

    private final FileService fileService;

    public AccessControlFilter(FileService fileService) {
        this.fileService = fileService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String requestURI = request.getRequestURI();

        AcquirenteEntity acquirente = SessionManager.getAcquirente(request);
        RivenditoreEntity rivenditore = SessionManager.getRivenditore(request);

        // Escludi le richieste per risorse statiche (CSS, immagini, ecc.)
        if (requestURI.startsWith("/css/") || requestURI.startsWith("/images/") || requestURI.startsWith("/js/") || requestURI.startsWith("/favicon.ico")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("Requested URI: " + requestURI);


        // Prendi solo il nome del file (senza percorso)
        String requestedFileName = requestURI.substring(requestURI.lastIndexOf("/") + 1) + ".html";

        if (requestURI.equalsIgnoreCase("/"))
            requestedFileName = "index.html";

        System.out.println("Requested fileName: " + requestedFileName);
        Set<String> purchaserFiles = fileService.getFileNamesFromPurchaserFolder();
        Set<String> vendorFiles = fileService.getFileNamesFromVendorFolder();

        if (acquirente == null && rivenditore == null) {
            if (purchaserFiles.contains(requestedFileName) || vendorFiles.contains(requestedFileName)) {
                throw new Exception("Accesso negato");
            }
        }


        if (acquirente != null || rivenditore != null) {
            if (fileService.getOtherFileNames().contains(requestedFileName)) {
                throw new Exception("Accesso negato!");
            }
        }


        // Controlla l'accesso per l'acquirente
        if (acquirente != null) {
            if (vendorFiles.contains(requestedFileName)
                    || requestedFileName.equalsIgnoreCase("userAuth.html")
                    || requestedFileName.equalsIgnoreCase("vendorAuth.html")) {
                throw new Exception("Accesso negato!");
            }

            if (SessionManager.getCarrello(request).getPrezzoProvvisorio() == 0.0 &&
                    (requestedFileName.equalsIgnoreCase("checkout.html") || requestedFileName.equalsIgnoreCase("checkoutSummary.html"))) {
                throw new Exception("Accesso negato!");
            }
        }

        // Controlla l'accesso per il rivenditore
        if (rivenditore != null) {
            if (purchaserFiles.contains(requestedFileName)
                    || requestedFileName.equalsIgnoreCase("userAuth.html")
                    || requestedFileName.equalsIgnoreCase("vendorAuth.html")) {
                throw new Exception("Accesso negato!");
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}