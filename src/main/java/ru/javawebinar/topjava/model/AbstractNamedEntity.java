package ru.javawebinar.topjava.model;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


@MappedSuperclass
public abstract class AbstractNamedEntity extends AbstractBaseEntity {

    @NotBlank
    @Column(name = "name", nullable = false)
    protected String name;

    public AbstractNamedEntity() {
    }

    protected AbstractNamedEntity(Integer id, String name) {
        super(id);
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("Entity %s (%s, '%s')", getClass().getName(), getId(), name);
    }
}