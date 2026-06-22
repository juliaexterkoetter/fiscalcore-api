package com.fiscalcore.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class TaxDTO {

    private Long id;

    @NotBlank(message = "Tax name is required")
    private String name;

    @NotBlank(message = "Tax description is required")
    private String description;

    @NotNull(message = "Tax rate is required")
    @DecimalMin(value = "0.01", message = "Tax rate must be greater than or equal to 0.01")
    @DecimalMax(value = "100.00", message = "Tax rate must be less than or equal to 100.00")
    private BigDecimal rate;

    public TaxDTO() {}

    public TaxDTO(Long id, String name, String description, BigDecimal rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rate = rate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
}
