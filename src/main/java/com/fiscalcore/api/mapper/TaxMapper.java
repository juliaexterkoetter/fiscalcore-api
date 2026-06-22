package com.fiscalcore.api.mapper;

import org.springframework.stereotype.Component;

import com.fiscalcore.api.dto.TaxDTO;
import com.fiscalcore.api.model.Tax;

@Component
public class TaxMapper {

    public TaxDTO toDTO(Tax tax) {
        return new TaxDTO(
                tax.getId(),
                tax.getName(),
                tax.getDescription(),
                tax.getRate()
        );
    }

    public Tax toEntity(TaxDTO dto) {
        Tax tax = new Tax();
        tax.setName(dto.getName());
        tax.setDescription(dto.getDescription());
        tax.setRate(dto.getRate());
        return tax;
    }
}
