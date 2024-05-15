package com.example.backend.services.impl;

import static org.mockito.Mockito.*;

import com.example.backend.FetchContractCustomers;
import com.example.backend.model.ContractCustomer;
import com.example.backend.repos.ContractCustomerRepo;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FetchContractCustomersIntegrationTest {

    @Mock
    private ContractCustomerRepo contractCustomerRepo;

    @InjectMocks
    private FetchContractCustomers fetchContractCustomers;


    @Test
    public void testRun() throws Exception {
        ContractCustomer existingCustomer = new ContractCustomer();
        existingCustomer.setExternalSystemId("123");
        when(contractCustomerRepo.findByExternalSystemId("123")).thenReturn(existingCustomer);

        fetchContractCustomers.run();

        verify(contractCustomerRepo, times(1)).save(any(ContractCustomer.class));
    }
}
