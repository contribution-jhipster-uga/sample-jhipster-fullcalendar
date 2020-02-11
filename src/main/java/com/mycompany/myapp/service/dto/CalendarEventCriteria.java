package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.TypeCalendarEventStatus;
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
 * Criteria class for the {@link com.mycompany.myapp.domain.CalendarEvent} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CalendarEventResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /calendar-events?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CalendarEventCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TypeCalendarEventStatus
     */
    public static class TypeCalendarEventStatusFilter extends Filter<TypeCalendarEventStatus> {

        public TypeCalendarEventStatusFilter() {
        }

        public TypeCalendarEventStatusFilter(TypeCalendarEventStatusFilter filter) {
            super(filter);
        }

        @Override
        public TypeCalendarEventStatusFilter copy() {
            return new TypeCalendarEventStatusFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private UUIDFilter uid;

    private StringFilter title;

    private StringFilter subTitle;

    private StringFilter description;

    private TypeCalendarEventStatusFilter status;

    private IntegerFilter priority;

    private StringFilter place;

    private StringFilter location;

    private StringFilter cssTheme;

    private StringFilter url;

    private BooleanFilter isPublic;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private StringFilter openingHours;

    private StringFilter imageSha1;

    private StringFilter imageUrl;

    private StringFilter thumbnailSha1;

    private InstantFilter createdAt;

    private InstantFilter updatedAt;

    private LongFilter createdById;

    private LongFilter calendarId;

    public CalendarEventCriteria() {
    }

    public CalendarEventCriteria(CalendarEventCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.uid = other.uid == null ? null : other.uid.copy();
        this.title = other.title == null ? null : other.title.copy();
        this.subTitle = other.subTitle == null ? null : other.subTitle.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.priority = other.priority == null ? null : other.priority.copy();
        this.place = other.place == null ? null : other.place.copy();
        this.location = other.location == null ? null : other.location.copy();
        this.cssTheme = other.cssTheme == null ? null : other.cssTheme.copy();
        this.url = other.url == null ? null : other.url.copy();
        this.isPublic = other.isPublic == null ? null : other.isPublic.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.openingHours = other.openingHours == null ? null : other.openingHours.copy();
        this.imageSha1 = other.imageSha1 == null ? null : other.imageSha1.copy();
        this.imageUrl = other.imageUrl == null ? null : other.imageUrl.copy();
        this.thumbnailSha1 = other.thumbnailSha1 == null ? null : other.thumbnailSha1.copy();
        this.createdAt = other.createdAt == null ? null : other.createdAt.copy();
        this.updatedAt = other.updatedAt == null ? null : other.updatedAt.copy();
        this.createdById = other.createdById == null ? null : other.createdById.copy();
        this.calendarId = other.calendarId == null ? null : other.calendarId.copy();
    }

    @Override
    public CalendarEventCriteria copy() {
        return new CalendarEventCriteria(this);
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

    public TypeCalendarEventStatusFilter getStatus() {
        return status;
    }

    public void setStatus(TypeCalendarEventStatusFilter status) {
        this.status = status;
    }

    public IntegerFilter getPriority() {
        return priority;
    }

    public void setPriority(IntegerFilter priority) {
        this.priority = priority;
    }

    public StringFilter getPlace() {
        return place;
    }

    public void setPlace(StringFilter place) {
        this.place = place;
    }

    public StringFilter getLocation() {
        return location;
    }

    public void setLocation(StringFilter location) {
        this.location = location;
    }

    public StringFilter getCssTheme() {
        return cssTheme;
    }

    public void setCssTheme(StringFilter cssTheme) {
        this.cssTheme = cssTheme;
    }

    public StringFilter getUrl() {
        return url;
    }

    public void setUrl(StringFilter url) {
        this.url = url;
    }

    public BooleanFilter getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(BooleanFilter isPublic) {
        this.isPublic = isPublic;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
    }

    public StringFilter getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(StringFilter openingHours) {
        this.openingHours = openingHours;
    }

    public StringFilter getImageSha1() {
        return imageSha1;
    }

    public void setImageSha1(StringFilter imageSha1) {
        this.imageSha1 = imageSha1;
    }

    public StringFilter getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(StringFilter imageUrl) {
        this.imageUrl = imageUrl;
    }

    public StringFilter getThumbnailSha1() {
        return thumbnailSha1;
    }

    public void setThumbnailSha1(StringFilter thumbnailSha1) {
        this.thumbnailSha1 = thumbnailSha1;
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

    public LongFilter getCreatedById() {
        return createdById;
    }

    public void setCreatedById(LongFilter createdById) {
        this.createdById = createdById;
    }

    public LongFilter getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(LongFilter calendarId) {
        this.calendarId = calendarId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CalendarEventCriteria that = (CalendarEventCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(uid, that.uid) &&
            Objects.equals(title, that.title) &&
            Objects.equals(subTitle, that.subTitle) &&
            Objects.equals(description, that.description) &&
            Objects.equals(status, that.status) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(place, that.place) &&
            Objects.equals(location, that.location) &&
            Objects.equals(cssTheme, that.cssTheme) &&
            Objects.equals(url, that.url) &&
            Objects.equals(isPublic, that.isPublic) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(openingHours, that.openingHours) &&
            Objects.equals(imageSha1, that.imageSha1) &&
            Objects.equals(imageUrl, that.imageUrl) &&
            Objects.equals(thumbnailSha1, that.thumbnailSha1) &&
            Objects.equals(createdAt, that.createdAt) &&
            Objects.equals(updatedAt, that.updatedAt) &&
            Objects.equals(createdById, that.createdById) &&
            Objects.equals(calendarId, that.calendarId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        uid,
        title,
        subTitle,
        description,
        status,
        priority,
        place,
        location,
        cssTheme,
        url,
        isPublic,
        startDate,
        endDate,
        openingHours,
        imageSha1,
        imageUrl,
        thumbnailSha1,
        createdAt,
        updatedAt,
        createdById,
        calendarId
        );
    }

    @Override
    public String toString() {
        return "CalendarEventCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (uid != null ? "uid=" + uid + ", " : "") +
                (title != null ? "title=" + title + ", " : "") +
                (subTitle != null ? "subTitle=" + subTitle + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (priority != null ? "priority=" + priority + ", " : "") +
                (place != null ? "place=" + place + ", " : "") +
                (location != null ? "location=" + location + ", " : "") +
                (cssTheme != null ? "cssTheme=" + cssTheme + ", " : "") +
                (url != null ? "url=" + url + ", " : "") +
                (isPublic != null ? "isPublic=" + isPublic + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (openingHours != null ? "openingHours=" + openingHours + ", " : "") +
                (imageSha1 != null ? "imageSha1=" + imageSha1 + ", " : "") +
                (imageUrl != null ? "imageUrl=" + imageUrl + ", " : "") +
                (thumbnailSha1 != null ? "thumbnailSha1=" + thumbnailSha1 + ", " : "") +
                (createdAt != null ? "createdAt=" + createdAt + ", " : "") +
                (updatedAt != null ? "updatedAt=" + updatedAt + ", " : "") +
                (createdById != null ? "createdById=" + createdById + ", " : "") +
                (calendarId != null ? "calendarId=" + calendarId + ", " : "") +
            "}";
    }

}
