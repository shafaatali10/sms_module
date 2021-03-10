package com.shafaat.apps.sms.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Sms.
 */
@Entity
@Table(name = "sms")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sms implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_name")
    private String phoneName;

    @Column(name = "date_time")
    private Instant dateTime;

    @Column(name = "message")
    private String message;

    @Column(name = "is_pinned")
    private Boolean isPinned;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public Sms status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Sms phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public Sms phoneName(String phoneName) {
        this.phoneName = phoneName;
        return this;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public Instant getDateTime() {
        return dateTime;
    }

    public Sms dateTime(Instant dateTime) {
        this.dateTime = dateTime;
        return this;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }

    public String getMessage() {
        return message;
    }

    public Sms message(String message) {
        this.message = message;
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean isIsPinned() {
        return isPinned;
    }

    public Sms isPinned(Boolean isPinned) {
        this.isPinned = isPinned;
        return this;
    }

    public void setIsPinned(Boolean isPinned) {
        this.isPinned = isPinned;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sms)) {
            return false;
        }
        return id != null && id.equals(((Sms) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Sms{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", phoneName='" + getPhoneName() + "'" +
            ", dateTime='" + getDateTime() + "'" +
            ", message='" + getMessage() + "'" +
            ", isPinned='" + isIsPinned() + "'" +
            "}";
    }
}
