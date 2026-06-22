package com.fiscalcore.api.service;

import com.fiscalcore.api.exception.ResourceNotFoundException;
import com.fiscalcore.api.model.Tax;
import com.fiscalcore.api.repository.TaxRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaxServiceTest {

    private TaxRepository taxRepository;
    private TaxService taxService;

    @BeforeEach
    void setUp() {
        taxRepository = mock(TaxRepository.class);
        taxService = new TaxService(taxRepository);
    }

    @Test
    void testFindAll() {
        Tax tax = createTax();

        when(taxRepository.findAll()).thenReturn(List.of(tax));

        List<Tax> taxes = taxService.findAll();

        assertNotNull(taxes);
        assertEquals(1, taxes.size());
        assertEquals("ICMS", taxes.get(0).getName());
        assertEquals(new BigDecimal("18.00"), taxes.get(0).getRate());

        verify(taxRepository).findAll();
    }

    @Test
    void testFindByIdSuccess() {
        Tax tax = createTax();

        when(taxRepository.findById(1L)).thenReturn(Optional.of(tax));

        Tax found = taxService.findById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        assertEquals("ICMS", found.getName());
        assertEquals(new BigDecimal("18.00"), found.getRate());

        verify(taxRepository).findById(1L);
    }

    @Test
    void testFindByIdNotFound() {
        when(taxRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> taxService.findById(1L));

        verify(taxRepository).findById(1L);
    }

    @Test
    void testCreate() {
        Tax tax = new Tax();
        tax.setName("ICMS");
        tax.setDescription("Tax on goods circulation");
        tax.setRate(new BigDecimal("18.00"));

        when(taxRepository.save(any(Tax.class))).thenAnswer(invocation -> {
            Tax saved = invocation.getArgument(0);
            saved.setId(1L);
            return saved;
        });

        Tax created = taxService.create(tax);

        assertNotNull(created.getId());
        assertEquals(1L, created.getId());
        assertEquals("ICMS", created.getName());
        assertEquals(new BigDecimal("18.00"), created.getRate());

        verify(taxRepository).save(any(Tax.class));
    }

    @Test
    void testDeleteByIdSuccess() {
        when(taxRepository.existsById(1L)).thenReturn(true);
        doNothing().when(taxRepository).deleteById(1L);

        assertDoesNotThrow(() -> taxService.deleteById(1L));

        verify(taxRepository).existsById(1L);
        verify(taxRepository).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(taxRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> taxService.deleteById(1L));

        verify(taxRepository).existsById(1L);
        verify(taxRepository, never()).deleteById(1L);
    }

    @Test
    void testCalculateTax() {
        Tax tax = new Tax();
        tax.setId(1L);
        tax.setName("ICMS");
        tax.setRate(new BigDecimal("18.00"));

        when(taxRepository.findById(1L)).thenReturn(Optional.of(tax));

        BigDecimal result = taxService.calculateTax(1L, new BigDecimal("100.00"));

        assertEquals(new BigDecimal("18.00"), result);

        verify(taxRepository).findById(1L);
    }

    private Tax createTax() {
        Tax tax = new Tax();
        tax.setId(1L);
        tax.setName("ICMS");
        tax.setDescription("Tax on goods circulation");
        tax.setRate(new BigDecimal("18.00"));
        return tax;
    }
}