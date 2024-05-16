package com.example.backend.services;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class XmlStreamProvider {
    private InputStream getDataStream(String urlToStreamFrom) throws IOException {
        URL url = new URL(urlToStreamFrom);
        return url.openStream();
    }

    public InputStream getContractCustomersStream() throws IOException {
        return getDataStream("https://javaintegration.systementor.se/customers");
    }

    public InputStream getShippersStream() throws IOException {
        return getDataStream("https://javaintegration.systementor.se/shippers");
    }


}
