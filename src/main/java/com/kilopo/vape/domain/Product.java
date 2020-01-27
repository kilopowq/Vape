package com.kilopo.vape.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "product")
public class Product extends BaseDomain {

    private String name;

    public Product(String name) {
        this.name = name;
    }

    public Product() {
    }

    @Column(unique = true)
    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
