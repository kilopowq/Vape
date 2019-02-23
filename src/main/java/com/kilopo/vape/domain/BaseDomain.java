package com.kilopo.vape.domain;

import javax.persistence.*;

@MappedSuperclass
public class BaseDomain {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    public long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
