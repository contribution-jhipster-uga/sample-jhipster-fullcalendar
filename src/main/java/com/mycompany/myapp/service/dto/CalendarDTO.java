package com.mycompany.myapp.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Calendar} entity.
 */
@ApiModel(description = "Entity Calendar")
public class CalendarDTO implements Serializable {

    private Long id;

    /**
     * UUID of the event (for iCal)
     */
    @ApiModelProperty(value = "UUID of the event (for iCal)")
    private UUID uid;

    /**
     * Title of the calendar
     */
    @ApiModelProperty(value = "Title of the calendar")
    private String title;

    /**
     * Subtitle of the calendar
     */
    @ApiModelProperty(value = "Subtitle of the calendar")
    private String subTitle;

    /**
     * Short description of the calendar
     */
    @ApiModelProperty(value = "Short description of the calendar")
    private String description;

    /**
     * Long description of the calendar (Rich text)
     */
    @ApiModelProperty(value = "Long description of the calendar (Rich text)")
    @Lob
    private String longDescription;

    /**
     * Creation date of the calendar
     */
    @NotNull
    @ApiModelProperty(value = "Creation date of the calendar", required = true)
    private Instant createdAt;

    /**
     * Update date of the calendar
     */
    @ApiModelProperty(value = "Update date of the calendar")
    private Instant updatedAt;


    private Long ownedById;

    private String ownedByLogin;

    private Set<UserDTO> sharedWiths = new HashSet<>();

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

    public Set<UserDTO> getSharedWiths() {
        return sharedWiths;
    }

    public void setSharedWiths(Set<UserDTO> users) {
        this.sharedWiths = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CalendarDTO calendarDTO = (CalendarDTO) o;
        if (calendarDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), calendarDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CalendarDTO{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", title='" + getTitle() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", longDescription='" + getLongDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", ownedById=" + getOwnedById() +
            ", ownedByLogin='" + getOwnedByLogin() + "'" +
            "}";
    }
}
