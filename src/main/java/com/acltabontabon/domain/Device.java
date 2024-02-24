package com.acltabontabon.domain;

import jakarta.persistence.*;
import java.io.Serializable;

/**
 * A Device.
 */
@Entity
@Table(name = "device")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "power_rating")
    private Double powerRating;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Device id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Device name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPowerRating() {
        return this.powerRating;
    }

    public Device powerRating(Double powerRating) {
        this.setPowerRating(powerRating);
        return this;
    }

    public void setPowerRating(Double powerRating) {
        this.powerRating = powerRating;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Device)) {
            return false;
        }
        return getId() != null && getId().equals(((Device) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Device{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", powerRating=" + getPowerRating() +
            "}";
    }
}
