package com.mycompany.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import com.mycompany.myapp.domain.enumeration.TypeCalendarEventStatus;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.CalendarEvent} entity.
 */
@ApiModel(description = "Entity CalendarEvent")
public class CalendarEventDTO implements Serializable {

    private Long id;

    /**
     * UUID of the event (required by RFC 5545 iCalendar)
     */
    @ApiModelProperty(value = "UUID of the event (required by RFC 5545 iCalendar)")
    private UUID uid;

    /**
     * Title of the event
     */
    @ApiModelProperty(value = "Title of the event")
    private String title;

    /**
     * Subtitle of the event
     */
    @ApiModelProperty(value = "Subtitle of the event")
    private String subTitle;

    /**
     * Short description of the event
     */
    @ApiModelProperty(value = "Short description of the event")
    private String description;

    /**
     * Long description of the event (Rich text)
     */
    @ApiModelProperty(value = "Long description of the event (Rich text)")
    @Lob
    private String longDescription;

    /**
     * Status of the event
     */
    @ApiModelProperty(value = "Status of the event")
    private TypeCalendarEventStatus status;

    /**
     * Priority of the event
     */
    @Min(value = 0)
    @ApiModelProperty(value = "Priority of the event")
    private Integer priority;

    /**
     * Place of the event
     */
    @ApiModelProperty(value = "Place of the event")
    private String place;

    /**
     * location of the event (GPS)
     */
    @Pattern(regexp = "^((-?[1-8]?\\d(?:\\.\\d{1,18})?|90(?:\\.0{1,18})?),(-?(?:1[0-7]|[1-9])?\\d(?:\\.\\d{1,18})?|180(?:\\.0{1,18})?)(,[0-9]{2}))?$")
    @ApiModelProperty(value = "location of the event (GPS)")
    private String location;

    /**
     * CSS theme of the event
     */
    @ApiModelProperty(value = "CSS theme of the event")
    private String cssTheme;

    /**
     * Public URL of the event
     */
    @Size(max = 200)
    @ApiModelProperty(value = "Public URL of the event")
    private String url;

    /**
     * Flag for a public event
     */
    @NotNull
    @ApiModelProperty(value = "Flag for a public event", required = true)
    private Boolean isPublic;

    /**
     * Start date of the event
     */
    @NotNull
    @ApiModelProperty(value = "Start date of the event", required = true)
    private Instant startDate;

    /**
     * End date of the event (should be after startDate)
     */
    @ApiModelProperty(value = "End date of the event (should be after startDate)")
    private Instant endDate;

    /**
     * Horaires of the event
     */
    @ApiModelProperty(value = "Horaires of the event")
    private String openingHours;

    /**
     * Image of the event
     */
    
    @ApiModelProperty(value = "Image of the event")
    @Lob
    private byte[] image;

    private String imageContentType;
    @Size(min = 40, max = 40)
    @Pattern(regexp = "[a-f0-9]{40}")
    private String imageSha1;

    @Size(min = 200)
    private String imageUrl;

    /**
     * Thumbnail of the event image
     */
    
    @ApiModelProperty(value = "Thumbnail of the event image")
    @Lob
    private byte[] thumbnail;

    private String thumbnailContentType;
    @Size(min = 40, max = 40)
    @Pattern(regexp = "[a-f0-9]{40}")
    private String thumbnailSha1;

    /**
     * RFC 5545 iCalendar of the event
     */
    
    @ApiModelProperty(value = "RFC 5545 iCalendar of the event")
    @Lob
    private byte[] ical;

    private String icalContentType;
    /**
     * Creation date of the event
     */
    @NotNull
    @ApiModelProperty(value = "Creation date of the event", required = true)
    private Instant createdAt;

    /**
     * Update date of the event
     */
    @ApiModelProperty(value = "Update date of the event")
    private Instant updatedAt;


    private Long createdById;

    private String createdByLogin;

    private Long calendarId;

    private String calendarTitle;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUid() {
        return uid;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public TypeCalendarEventStatus getStatus() {
        return status;
    }

    public void setStatus(TypeCalendarEventStatus status) {
        this.status = status;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCssTheme() {
        return cssTheme;
    }

    public void setCssTheme(String cssTheme) {
        this.cssTheme = cssTheme;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean isIsPublic() {
        return isPublic;
    }

    public void setIsPublic(Boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getImageContentType() {
        return imageContentType;
    }

    public void setImageContentType(String imageContentType) {
        this.imageContentType = imageContentType;
    }

    public String getImageSha1() {
        return imageSha1;
    }

    public void setImageSha1(String imageSha1) {
        this.imageSha1 = imageSha1;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnailContentType() {
        return thumbnailContentType;
    }

    public void setThumbnailContentType(String thumbnailContentType) {
        this.thumbnailContentType = thumbnailContentType;
    }

    public String getThumbnailSha1() {
        return thumbnailSha1;
    }

    public void setThumbnailSha1(String thumbnailSha1) {
        this.thumbnailSha1 = thumbnailSha1;
    }

    public byte[] getIcal() {
        return ical;
    }

    public void setIcal(byte[] ical) {
        this.ical = ical;
    }

    public String getIcalContentType() {
        return icalContentType;
    }

    public void setIcalContentType(String icalContentType) {
        this.icalContentType = icalContentType;
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

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public Long getCalendarId() {
        return calendarId;
    }

    public void setCalendarId(Long calendarId) {
        this.calendarId = calendarId;
    }

    public String getCalendarTitle() {
        return calendarTitle;
    }

    public void setCalendarTitle(String calendarTitle) {
        this.calendarTitle = calendarTitle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CalendarEventDTO calendarEventDTO = (CalendarEventDTO) o;
        if (calendarEventDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), calendarEventDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CalendarEventDTO{" +
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
            ", imageSha1='" + getImageSha1() + "'" +
            ", imageUrl='" + getImageUrl() + "'" +
            ", thumbnail='" + getThumbnail() + "'" +
            ", thumbnailSha1='" + getThumbnailSha1() + "'" +
            ", ical='" + getIcal() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", createdById=" + getCreatedById() +
            ", createdByLogin='" + getCreatedByLogin() + "'" +
            ", calendarId=" + getCalendarId() +
            ", calendarTitle='" + getCalendarTitle() + "'" +
            "}";
    }
}
