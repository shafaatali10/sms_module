package com.shafaat.apps.sms.service.dto;

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

/**
 * Criteria class for the {@link com.shafaat.apps.sms.domain.Sms} entity. This class is used
 * in {@link com.shafaat.apps.sms.web.rest.SmsResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /sms?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class SmsCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter status;

    private StringFilter phoneNumber;

    private StringFilter phoneName;

    private InstantFilter dateTime;

    private StringFilter message;

    private BooleanFilter isPinned;

    public SmsCriteria() {
    }

    public SmsCriteria(SmsCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.status = other.status == null ? null : other.status.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.phoneName = other.phoneName == null ? null : other.phoneName.copy();
        this.dateTime = other.dateTime == null ? null : other.dateTime.copy();
        this.message = other.message == null ? null : other.message.copy();
        this.isPinned = other.isPinned == null ? null : other.isPinned.copy();
    }

    @Override
    public SmsCriteria copy() {
        return new SmsCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getStatus() {
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public StringFilter getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(StringFilter phoneName) {
        this.phoneName = phoneName;
    }

    public InstantFilter getDateTime() {
        return dateTime;
    }

    public void setDateTime(InstantFilter dateTime) {
        this.dateTime = dateTime;
    }

    public StringFilter getMessage() {
        return message;
    }

    public void setMessage(StringFilter message) {
        this.message = message;
    }

    public BooleanFilter getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(BooleanFilter isPinned) {
        this.isPinned = isPinned;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final SmsCriteria that = (SmsCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(status, that.status) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(phoneName, that.phoneName) &&
            Objects.equals(dateTime, that.dateTime) &&
            Objects.equals(message, that.message) &&
            Objects.equals(isPinned, that.isPinned);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        status,
        phoneNumber,
        phoneName,
        dateTime,
        message,
        isPinned
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SmsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (status != null ? "status=" + status + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (phoneName != null ? "phoneName=" + phoneName + ", " : "") +
                (dateTime != null ? "dateTime=" + dateTime + ", " : "") +
                (message != null ? "message=" + message + ", " : "") +
                (isPinned != null ? "isPinned=" + isPinned + ", " : "") +
            "}";
    }

}
