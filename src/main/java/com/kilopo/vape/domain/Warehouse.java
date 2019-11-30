package com.kilopo.vape.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "warehouse")
public class Warehouse extends BaseDomain {
    private String name;

    public Warehouse(String name) {
        this.name = name;
    }

    public Warehouse() {
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
