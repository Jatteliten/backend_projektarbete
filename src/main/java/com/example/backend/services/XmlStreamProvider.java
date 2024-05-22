package com.example.backend.services;

import com.example.backend.configuration.IntegrationProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@Service
public class XmlStreamProvider {
    @Autowired
    IntegrationProperties integrationProperties;


    private InputStream getDataStream(String urlToStreamFrom) throws IOException {
        URL url = new URL(urlToStreamFrom);
        return url.openStream();
    }

    public InputStream getContractCustomersStream() throws IOException {
        return getDataStream(integrationProperties.getContractCustomers().getUrl());
    }

    public InputStream getShippersStream() throws IOException {
        return getDataStream(integrationProperties.getShippers().getUrl());
    }


}
