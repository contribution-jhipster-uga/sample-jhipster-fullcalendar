package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;
import io.github.jhipster.service.filter.UUIDFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Calendar} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CalendarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /calendars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CalendarCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uid;

    private StringFilter title;

    private StringFilter subTitle;

    private StringFilter description;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter ownedById;

    private LongFilter sharedWithId;

    public CalendarCriteria() {
    }

    public CalendarCriteria(CalendarCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uid = other.uid == null ? null : other.uid.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.subTitle = other.subTitle == null ? null : other.subTitle.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.ownedById = other.ownedById == null ? null : other.ownedById.copy();
        this.sharedWithId = other.sharedWithId == null ? null : other.sharedWithId.copy();
    }

    @Override
    public CalendarCriteria copy() {
        return new CalendarCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public UUIDFilter getUid() {
        return uid;
    }

    public void setUid(UUIDFilter uid) {
        this.uid = uid;
    }

    public StringFilter getTitle() {
        return title;
    }

    public void setTitle(StringFilter title) {
        this.title = title;
    }

    public StringFilter getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(StringFilter subTitle) {
        this.subTitle = subTitle;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
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

    public LongFilter getSharedWithId() {
        return sharedWithId;
    }

    public void setSharedWithId(LongFilter sharedWithId) {
        this.sharedWithId = sharedWithId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CalendarCriteria that = (CalendarCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(title, that.title) &&
            Objects.equals(subTitle, that.subTitle) &&
            Objects.equals(description, that.description) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(ownedById, that.ownedById) &&
            Objects.equals(sharedWithId, that.sharedWithId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uid,
        title,
        subTitle,
        description,
        createdAt,
        updatedAt,
        ownedById,
        sharedWithId
        );
    }

    @Override
    public String toString() {
        return "CalendarCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (subTitle != null ? "subTitle=" + subTitle + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (ownedById != null ? "ownedById=" + ownedById + ", " : "") +
                (sharedWithId != null ? "sharedWithId=" + sharedWithId + ", " : "") +
            "}";
    }

}
