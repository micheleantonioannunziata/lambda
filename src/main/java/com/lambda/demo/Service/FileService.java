package com.lambda.demo.Service;

import com.lambda.demo.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class FileService {

    @Autowired
    private final DataLoader dataLoader;

    public FileService(DataLoader dataLoader) {
        this.dataLoader = dataLoader;
    }

    public Set<String> getFileNamesFromPurchaserFolder() {
        return dataLoader.getFileNamesPurchaserFolder();
    }

    public Set<String> getFileNamesFromVendorFolder() {
        return dataLoader.getFileNamesFromVendorFolder();
    }

    public Set<String> getOtherFileNames() {
        return dataLoader.getOtherFileNames();
    }
}
