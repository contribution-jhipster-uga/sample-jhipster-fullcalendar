package com.mycompany.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.mycompany.myapp.domain.enumeration.TypeCalendarProvider;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CalendarProvider} entity.
 */
@ApiModel(description = "Entity CalendarProvider")
public class CalendarProviderDTO implements Serializable {

    private Long id;

    /**
     * Provider of the calendar
     */
    @NotNull
    @ApiModelProperty(value = "Provider of the calendar", required = true)
    private TypeCalendarProvider provider;

    /**
     * User identifier of the calendar provider
     */
    @NotNull
    @ApiModelProperty(value = "User identifier of the calendar provider", required = true)
    private String identifier;

    /**
     * Provider of the calendar provider
     */
    @NotNull
    @ApiModelProperty(value = "Provider of the calendar provider", required = true)
    private String credential;

    /**
     * Credential Extra 1 of the calendar provider
     */
    @ApiModelProperty(value = "Credential Extra 1 of the calendar provider")
    private String credentialExtra1;

    /**
     * Credential Extra 2 of the calendar provider
     */
    @ApiModelProperty(value = "Credential Extra 2 of the calendar provider")
    private String credentialExtra2;

    /**
     * Creation date of the calendar provider
     */
    @NotNull
    @ApiModelProperty(value = "Creation date of the calendar provider", required = true)
    private Instant createdAt;

    /**
     * Update date of the calendar provider
     */
    @ApiModelProperty(value = "Update date of the calendar provider")
    private Instant updatedAt;


    private Long ownedById;

    private String ownedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TypeCalendarProvider getProvider() {
        return provider;
    }

    public void setProvider(TypeCalendarProvider provider) {
        this.provider = provider;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getCredentialExtra1() {
        return credentialExtra1;
    }

    public void setCredentialExtra1(String credentialExtra1) {
        this.credentialExtra1 = credentialExtra1;
    }

    public String getCredentialExtra2() {
        return credentialExtra2;
    }

    public void setCredentialExtra2(String credentialExtra2) {
        this.credentialExtra2 = credentialExtra2;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getOwnedById() {
        return ownedById;
    }

    public void setOwnedById(Long userId) {
        this.ownedById = userId;
    }

    public String getOwnedByLogin() {
        return ownedByLogin;
    }

    public void setOwnedByLogin(String userLogin) {
        this.ownedByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CalendarProviderDTO calendarProviderDTO = (CalendarProviderDTO) o;
        if (calendarProviderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), calendarProviderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CalendarProviderDTO{" +
            "id=" + getId() +
            ", provider='" + getProvider() + "'" +
            ", identifier='" + getIdentifier() + "'" +
            ", credential='" + getCredential() + "'" +
            ", credentialExtra1='" + getCredentialExtra1() + "'" +
            ", credentialExtra2='" + getCredentialExtra2() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", ownedById=" + getOwnedById() +
            ", ownedByLogin='" + getOwnedByLogin() + "'" +
            "}";
    }
}
