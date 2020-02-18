package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import com.mycompany.myapp.domain.enumeration.TypeCalendarEventStatus;

/**
 * Entity CalendarEvent
 */
@Entity
@Table(name = "calendar_event")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "calendarevent")
public class CalendarEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * UUID of the event (required by RFC 5545 iCalendar)
     */
    @Column(name = "uid")
    private UUID uid;

    /**
     * Title of the event
     */
    @Column(name = "title")
    private String title;

    /**
     * Subtitle of the event
     */
    @Column(name = "sub_title")
    private String subTitle;

    /**
     * Short description of the event
     */
    @Column(name = "description")
    private String description;

    /**
     * Long description of the event (Rich text)
     */
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "long_description")
    private String longDescription;

    /**
     * Status of the event
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TypeCalendarEventStatus status;

    /**
     * Priority of the event
     */
    @Min(value = 0)
    @Column(name = "priority")
    private Integer priority;

    /**
     * Place of the event
     */
    @Column(name = "place")
    private String place;

    /**
     * location of the event (GPS)
     */
    @Pattern(regexp = "^((-?[1-8]?\\d(?:\\.\\d{1,18})?|90(?:\\.0{1,18})?),(-?(?:1[0-7]|[1-9])?\\d(?:\\.\\d{1,18})?|180(?:\\.0{1,18})?)(,[0-9]{2}))?$")
    @Column(name = "location")
    private String location;

    /**
     * CSS theme of the event
     */
    @Column(name = "css_theme")
    private String cssTheme;

    /**
     * Public URL of the event
     */
    @Size(max = 200)
    @Column(name = "url", length = 200)
    private String url;

    /**
     * Flag for a public event
     */
    @NotNull
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    /**
     * Start date of the event
     */
    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    /**
     * End date of the event (should be after startDate)
     */
    @Column(name = "end_date")
    private Instant endDate;

    /**
     * Horaires of the event
     */
    @Column(name = "opening_hours")
    private String openingHours;

    /**
     * Image of the event
     */
    
    @Lob
    @Column(name = "image")
    private byte[] image;

    @Column(name = "image_content_type")
    private String imageContentType;

    @Size(min = 40, max = 40)
    @Pattern(regexp = "[a-f0-9]{40}")
    @Column(name = "image_sha_1", length = 40)
    private String imageSha1;

    @Size(max = 200)
    @Column(name = "image_url", length = 200)
    private String imageUrl;

    /**
     * Thumbnail of the event image
     */
    
    @Lob
    @Column(name = "thumbnail")
    private byte[] thumbnail;

    @Column(name = "thumbnail_content_type")
    private String thumbnailContentType;

    @Size(min = 40, max = 40)
    @Pattern(regexp = "[a-f0-9]{40}")
    @Column(name = "thumbnail_sha_1", length = 40)
    private String thumbnailSha1;

    /**
     * RFC 5545 iCalendar of the event
     */
    
    @Lob
    @Column(name = "ical")
    private byte[] ical;

    @Column(name = "ical_content_type")
    private String icalContentType;

    /**
     * Creation date of the event
     */
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Update date of the event
     */
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JsonIgnoreProperties("calendarEvents")
    private User createdBy;

    @ManyToOne
    @JsonIgnoreProperties("calendarEvents")
    private Calendar calendar;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return uid;
    }

    public CalendarEvent uid(UUID uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public CalendarEvent title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public CalendarEvent subTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public CalendarEvent description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public CalendarEvent longDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public TypeCalendarEventStatus getStatus() {
        return status;
    }

    public CalendarEvent status(TypeCalendarEventStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(TypeCalendarEventStatus status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public CalendarEvent priority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPlace() {
        return place;
    }

    public CalendarEvent place(String place) {
        this.place = place;
        return this;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLocation() {
        return location;
    }

    public CalendarEvent location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCssTheme() {
        return cssTheme;
    }

    public CalendarEvent cssTheme(String cssTheme) {
        this.cssTheme = cssTheme;
        return this;
    }

    public void setCssTheme(String cssTheme) {
        this.cssTheme = cssTheme;
    }

    public String getUrl() {
        return url;
    }

    public CalendarEvent url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isIsPublic() {
        return isPublic;
    }

    public CalendarEvent isPublic(Boolean isPublic) {
        this.isPublic = isPublic;
        return this;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public CalendarEvent startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public CalendarEvent endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public CalendarEvent openingHours(String openingHours) {
        this.openingHours = openingHours;
        return this;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public byte[] getImage() {
        return image;
    }

    public CalendarEvent image(byte[] image) {
        this.image = image;
        return this;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public CalendarEvent imageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
        return this;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageSha1() {
        return imageSha1;
    }

    public CalendarEvent imageSha1(String imageSha1) {
        this.imageSha1 = imageSha1;
        return this;
    }

    public void setImageSha1(String imageSha1) {
        this.imageSha1 = imageSha1;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public CalendarEvent imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public CalendarEvent thumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
        return this;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    public CalendarEvent thumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
        return this;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public String getThumbnailSha1() {
        return thumbnailSha1;
    }

    public CalendarEvent thumbnailSha1(String thumbnailSha1) {
        this.thumbnailSha1 = thumbnailSha1;
        return this;
    }

    public void setThumbnailSha1(String thumbnailSha1) {
        this.thumbnailSha1 = thumbnailSha1;
    }

    public byte[] getIcal() {
        return ical;
    }

    public CalendarEvent ical(byte[] ical) {
        this.ical = ical;
        return this;
    }

    public void setIcal(byte[] ical) {
        this.ical = ical;
    }

    public String getIcalContentType() {
        return icalContentType;
    }

    public CalendarEvent icalContentType(String icalContentType) {
        this.icalContentType = icalContentType;
        return this;
    }

    public void setIcalContentType(String icalContentType) {
        this.icalContentType = icalContentType;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public CalendarEvent createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public CalendarEvent updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public CalendarEvent createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public CalendarEvent calendar(Calendar calendar) {
        this.calendar = calendar;
        return this;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CalendarEvent)) {
            return false;
        }
        return id != null && id.equals(((CalendarEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", title='" + getTitle() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", longDescription='" + getLongDescription() + "'" +
            ", status='" + getStatus() + "'" +
            ", priority=" + getPriority() +
            ", place='" + getPlace() + "'" +
            ", location='" + getLocation() + "'" +
            ", cssTheme='" + getCssTheme() + "'" +
            ", url='" + getUrl() + "'" +
            ", isPublic='" + isIsPublic() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", openingHours='" + getOpeningHours() + "'" +
            ", image='" + getImage() + "'" +
            ", imageContentType='" + getImageContentType() + "'" +
            ", imageSha1='" + getImageSha1() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailContentType='" + getThumbnailContentType() + "'" +
            ", thumbnailSha1='" + getThumbnailSha1() + "'" +
            ", ical='" + getIcal() + "'" +
            ", icalContentType='" + getIcalContentType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
