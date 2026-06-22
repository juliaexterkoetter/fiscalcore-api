package com.fiscalcore.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiscalcore.api.dto.TaxDTO;
import com.fiscalcore.api.mapper.TaxMapper;
import com.fiscalcore.api.model.Tax;
import com.fiscalcore.api.service.TaxService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class TaxControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TaxService taxService;

    @Mock
    private TaxMapper taxMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TaxController(taxService, taxMapper))
                .build();
    }

    @Test
    void testCreate() throws Exception {
        TaxDTO request = new TaxDTO(
                null,
                "ICMS",
                "Tax on goods circulation",
                new BigDecimal("18.00")
        );

        Tax tax = createTax();

        TaxDTO response = new TaxDTO(
                1L,
                "ICMS",
                "Tax on goods circulation",
                new BigDecimal("18.00")
        );

        when(taxMapper.toEntity(any(TaxDTO.class))).thenReturn(tax);
        when(taxService.create(any(Tax.class))).thenReturn(tax);
        when(taxMapper.toDTO(tax)).thenReturn(response);

        mockMvc.perform(post("/taxes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ICMS"))
                .andExpect(jsonPath("$.description").value("Tax on goods circulation"))
                .andExpect(jsonPath("$.rate").value(18.00));
    }

    @Test
    void testFindAll() throws Exception {
        Tax tax = createTax();

        TaxDTO response = new TaxDTO(
                1L,
                "ICMS",
                "Tax on goods circulation",
                new BigDecimal("18.00")
        );

        when(taxService.findAll()).thenReturn(List.of(tax));
        when(taxMapper.toDTO(tax)).thenReturn(response);

        mockMvc.perform(get("/taxes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("ICMS"))
                .andExpect(jsonPath("$[0].description").value("Tax on goods circulation"))
                .andExpect(jsonPath("$[0].rate").value(18.00));
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