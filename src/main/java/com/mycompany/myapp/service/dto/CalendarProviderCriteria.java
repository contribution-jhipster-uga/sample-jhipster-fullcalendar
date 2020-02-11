package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.TypeCalendarProvider;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.CalendarProvider} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CalendarProviderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /calendar-providers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CalendarProviderCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TypeCalendarProvider
     */
    public static class TypeCalendarProviderFilter extends Filter<TypeCalendarProvider> {

        public TypeCalendarProviderFilter() {
        }

        public TypeCalendarProviderFilter(TypeCalendarProviderFilter filter) {
            super(filter);
        }

        @Override
        public TypeCalendarProviderFilter copy() {
            return new TypeCalendarProviderFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private TypeCalendarProviderFilter provider;

    private StringFilter identifier;

    private StringFilter credential;

    private StringFilter credentialExtra1;

    private StringFilter credentialExtra2;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter ownedById;

    public CalendarProviderCriteria() {
    }

    public CalendarProviderCriteria(CalendarProviderCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.provider = other.provider == null ? null : other.provider.copy();
        this.identifier = other.identifier == null ? null : other.identifier.copy();
        this.credential = other.credential == null ? null : other.credential.copy();
        this.credentialExtra1 = other.credentialExtra1 == null ? null : other.credentialExtra1.copy();
        this.credentialExtra2 = other.credentialExtra2 == null ? null : other.credentialExtra2.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.ownedById = other.ownedById == null ? null : other.ownedById.copy();
    }

    @Override
    public CalendarProviderCriteria copy() {
        return new CalendarProviderCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public TypeCalendarProviderFilter getProvider() {
        return provider;
    }

    public void setProvider(TypeCalendarProviderFilter provider) {
        this.provider = provider;
    }

    public StringFilter getIdentifier() {
        return identifier;
    }

    public void setIdentifier(StringFilter identifier) {
        this.identifier = identifier;
    }

    public StringFilter getCredential() {
        return credential;
    }

    public void setCredential(StringFilter credential) {
        this.credential = credential;
    }

    public StringFilter getCredentialExtra1() {
        return credentialExtra1;
    }

    public void setCredentialExtra1(StringFilter credentialExtra1) {
        this.credentialExtra1 = credentialExtra1;
    }

    public StringFilter getCredentialExtra2() {
        return credentialExtra2;
    }

    public void setCredentialExtra2(StringFilter credentialExtra2) {
        this.credentialExtra2 = credentialExtra2;
    }

    public InstantFilter getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(InstantFilter createdAt) {
        this.createdAt = createdAt;
    }

    public InstantFilter getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(InstantFilter updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LongFilter getOwnedById() {
        return ownedById;
    }

    public void setOwnedById(LongFilter ownedById) {
        this.ownedById = ownedById;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CalendarProviderCriteria that = (CalendarProviderCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(provider, that.provider) &&
            Objects.equals(identifier, that.identifier) &&
            Objects.equals(credential, that.credential) &&
            Objects.equals(credentialExtra1, that.credentialExtra1) &&
            Objects.equals(credentialExtra2, that.credentialExtra2) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(ownedById, that.ownedById);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        provider,
        identifier,
        credential,
        credentialExtra1,
        credentialExtra2,
        createdAt,
        updatedAt,
        ownedById
        );
    }

    @Override
    public String toString() {
        return "CalendarProviderCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (provider != null ? "provider=" + provider + ", " : "") +
                (identifier != null ? "identifier=" + identifier + ", " : "") +
                (credential != null ? "credential=" + credential + ", " : "") +
                (credentialExtra1 != null ? "credentialExtra1=" + credentialExtra1 + ", " : "") +
                (credentialExtra2 != null ? "credentialExtra2=" + credentialExtra2 + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (ownedById != null ? "ownedById=" + ownedById + ", " : "") +
            "}";
    }

}
