package com.acltabontabon.domain;

import com.acltabontabon.domain.enumeration.ServiceType;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A ServiceProvider.
 */
@Entity
@Table(name = "service_provider")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ServiceProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "unit_measurement")
    private String unitMeasurement;

    @Column(name = "notes")
    private String notes;

    @Column(name = "rate")
    private Double rate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ServiceType type;

    @Column(name = "last_update")
    private Instant lastUpdate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ServiceProvider id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ServiceProvider name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnitMeasurement() {
        return this.unitMeasurement;
    }

    public ServiceProvider unitMeasurement(String unitMeasurement) {
        this.setUnitMeasurement(unitMeasurement);
        return this;
    }

    public void setUnitMeasurement(String unitMeasurement) {
        this.unitMeasurement = unitMeasurement;
    }

    public String getNotes() {
        return this.notes;
    }

    public ServiceProvider notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Double getRate() {
        return this.rate;
    }

    public ServiceProvider rate(Double rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public ServiceType getType() {
        return this.type;
    }

    public ServiceProvider type(ServiceType type) {
        this.setType(type);
        return this;
    }

    public void setType(ServiceType type) {
        this.type = type;
    }

    public Instant getLastUpdate() {
        return this.lastUpdate;
    }

    public ServiceProvider lastUpdate(Instant lastUpdate) {
        this.setLastUpdate(lastUpdate);
        return this;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServiceProvider)) {
            return false;
        }
        return getId() != null && getId().equals(((ServiceProvider) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ServiceProvider{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", unitMeasurement='" + getUnitMeasurement() + "'" +
            ", notes='" + getNotes() + "'" +
            ", rate=" + getRate() +
            ", type='" + getType() + "'" +
            ", lastUpdate='" + getLastUpdate() + "'" +
            "}";
    }
}
