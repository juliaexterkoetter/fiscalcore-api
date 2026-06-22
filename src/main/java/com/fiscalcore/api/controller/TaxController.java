package com.fiscalcore.api.controller;

import com.fiscalcore.api.dto.TaxDTO;
import com.fiscalcore.api.mapper.TaxMapper;
import com.fiscalcore.api.model.Tax;
import com.fiscalcore.api.service.TaxService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/taxes")
public class TaxController {

    private final TaxService taxService;
    private final TaxMapper taxMapper;

    public TaxController(TaxService taxService, TaxMapper taxMapper) {
        this.taxService = taxService;
        this.taxMapper = taxMapper;
    }

    @GetMapping
    public List<TaxDTO> findAll() {
        List<Tax> taxes = taxService.findAll();
        return taxes.stream().map(taxMapper::toDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public TaxDTO findById(@PathVariable Long id) {
        return taxMapper.toDTO(taxService.findById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaxDTO create(@RequestBody @Valid TaxDTO taxDTO) {
        Tax tax = taxMapper.toEntity(taxDTO);
        Tax created = taxService.create(tax);
        return taxMapper.toDTO(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        taxService.deleteById(id);
    }
}
