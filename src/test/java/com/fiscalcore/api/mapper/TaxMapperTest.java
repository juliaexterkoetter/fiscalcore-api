package com.fiscalcore.api.mapper;

import com.fiscalcore.api.dto.TaxDTO;
import com.fiscalcore.api.model.Tax;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TaxMapperTest {

    private final TaxMapper mapper = new TaxMapper();

    @Test
    void testToDTO() {
        Tax tax = new Tax();
        tax.setId(1L);
        tax.setName("ICMS");
        tax.setDescription("Tax on goods circulation");
        tax.setRate(new BigDecimal("18.00"));

        TaxDTO dto = mapper.toDTO(tax);

        assertEquals(1L, dto.getId());
        assertEquals("ICMS", dto.getName());
        assertEquals("Tax on goods circulation", dto.getDescription());
        assertEquals(new BigDecimal("18.00"), dto.getRate());
    }

    @Test
    void testToEntity() {
        TaxDTO dto = new TaxDTO(
                null,
                "ICMS",
                "Tax on goods circulation",
                new BigDecimal("18.00")
        );

        Tax tax = mapper.toEntity(dto);

        assertNull(tax.getId());
        assertEquals("ICMS", tax.getName());
        assertEquals("Tax on goods circulation", tax.getDescription());
        assertEquals(new BigDecimal("18.00"), tax.getRate());
    }
}