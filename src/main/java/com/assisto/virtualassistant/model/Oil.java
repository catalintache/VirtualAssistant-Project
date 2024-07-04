package com.assisto.virtualassistant.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(
        name = "oils"
)
public class Oil {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    private String type;
    private String viscosity;
    private BigDecimal price;

    public Oil() {
    }

    public Oil(String type, String viscosity, BigDecimal price) {
        this.type = type;
        this.viscosity = viscosity;
        this.price = price;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getViscosity() {
        return this.viscosity;
    }

    public void setViscosity(String viscosity) {
        this.viscosity = viscosity;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
