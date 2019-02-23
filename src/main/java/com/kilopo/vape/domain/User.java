package com.kilopo.vape.domain;

        import org.springframework.lang.NonNull;

        import javax.persistence.Entity;
        import javax.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseDomain {

    @NonNull
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}