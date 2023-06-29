package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class FlagEntity {
    @JsonIgnore
    @Column
    protected int flag = 0;

    protected boolean getBitValue(int bitIndex) {
        return (this.flag & (1 << bitIndex)) != 0;
    }

    protected void setBitValue(int bitIndex, boolean value) {
        if (value) {
            this.flag |= (1 << bitIndex);
        } else {
            this.flag &= ~(1 << bitIndex);
        }
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
