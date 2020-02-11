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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entity Calendar
 */
@Entity
@Table(name = "calendar")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "calendar")
public class Calendar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * UUID of the event (for iCal)
     */
    @Column(name = "uid")
    private UUID uid;

    /**
     * Title of the calendar
     */
    @Column(name = "title")
    private String title;

    /**
     * Subtitle of the calendar
     */
    @Column(name = "sub_title")
    private String subTitle;

    /**
     * Short description of the calendar
     */
    @Column(name = "description")
    private String description;

    /**
     * Long description of the calendar (Rich text)
     */
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "long_description")
    private String longDescription;

    /**
     * Creation date of the calendar
     */
    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    /**
     * Update date of the calendar
     */
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ManyToOne
    @JsonIgnoreProperties("calendars")
    private User ownedBy;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "calendar_shared_with",
               joinColumns = @JoinColumn(name = "calendar_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "shared_with_id", referencedColumnName = "id"))
    private Set<User> sharedWiths = new HashSet<>();

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

    public Calendar uid(UUID uid) {
        this.uid = uid;
        return this;
    }

    public void setUid(UUID uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public Calendar title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public Calendar subTitle(String subTitle) {
        this.subTitle = subTitle;
        return this;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getDescription() {
        return description;
    }

    public Calendar description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public Calendar longDescription(String longDescription) {
        this.longDescription = longDescription;
        return this;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Calendar createdAt(Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Calendar updatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getOwnedBy() {
        return ownedBy;
    }

    public Calendar ownedBy(User user) {
        this.ownedBy = user;
        return this;
    }

    public void setOwnedBy(User user) {
        this.ownedBy = user;
    }

    public Set<User> getSharedWiths() {
        return sharedWiths;
    }

    public Calendar sharedWiths(Set<User> users) {
        this.sharedWiths = users;
        return this;
    }

    public Calendar addSharedWith(User user) {
        this.sharedWiths.add(user);
        return this;
    }

    public Calendar removeSharedWith(User user) {
        this.sharedWiths.remove(user);
        return this;
    }

    public void setSharedWiths(Set<User> users) {
        this.sharedWiths = users;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Calendar)) {
            return false;
        }
        return id != null && id.equals(((Calendar) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Calendar{" +
            "id=" + getId() +
            ", uid='" + getUid() + "'" +
            ", title='" + getTitle() + "'" +
            ", subTitle='" + getSubTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", longDescription='" + getLongDescription() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
