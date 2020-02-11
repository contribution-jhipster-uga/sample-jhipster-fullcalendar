package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import com.mycompany.myapp.domain.enumeration.TypeCalendarProvider;

/**
 * Entity CalendarProvider
 */
@Entity
@Table(name = "calendar_provider")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "calendarprovider")
public class CalendarProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Provider of the calendar
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "provider", nullable = false)
    private TypeCalendarProvider provider;

    /**
     * User identifier of the calendar provider
     */
    @NotNull
    @Column(name = "identifier", nullable = false)
    private String identifier;

    /**
     * Provider of the calendar provider
     */
    @NotNull
    @Column(name = "credential", nullable = false)
    private String credential;

    /**
     * Credential Extra 1 of the calendar provider
     */
    @Column(name = "credential_extra_1")
    private String credentialExtra1;

    /**
     * Credential Extra 2 of the calendar provider
     */
    @Column(name = "credential_extra_2")
    private String credentialExtra2;

    /**
     * Creation date of the calendar provider
     */
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Update date of the calendar provider
     */
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JsonIgnoreProperties("calendarProviders")
    private User ownedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeCalendarProvider getProvider() {
        return provider;
    }

    public CalendarProvider provider(TypeCalendarProvider provider) {
        this.provider = provider;
        return this;
    }

    public void setProvider(TypeCalendarProvider provider) {
        this.provider = provider;
    }

    public String getIdentifier() {
        return identifier;
    }

    public CalendarProvider identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public CalendarProvider credential(String credential) {
        this.credential = credential;
        return this;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getCredentialExtra1() {
        return credentialExtra1;
    }

    public CalendarProvider credentialExtra1(String credentialExtra1) {
        this.credentialExtra1 = credentialExtra1;
        return this;
    }

    public void setCredentialExtra1(String credentialExtra1) {
        this.credentialExtra1 = credentialExtra1;
    }

    public String getCredentialExtra2() {
        return credentialExtra2;
    }

    public CalendarProvider credentialExtra2(String credentialExtra2) {
        this.credentialExtra2 = credentialExtra2;
        return this;
    }

    public void setCredentialExtra2(String credentialExtra2) {
        this.credentialExtra2 = credentialExtra2;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public CalendarProvider createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public CalendarProvider updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getOwnedBy() {
        return ownedBy;
    }

    public CalendarProvider ownedBy(User user) {
        this.ownedBy = user;
        return this;
    }

    public void setOwnedBy(User user) {
        this.ownedBy = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalendarProvider)) {
            return false;
        }
        return id != null && id.equals(((CalendarProvider) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CalendarProvider{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            ", credential='" + getCredential() + "'" +
            ", credentialExtra1='" + getCredentialExtra1() + "'" +
            ", credentialExtra2='" + getCredentialExtra2() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
