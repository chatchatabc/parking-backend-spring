package com.chatchatabc.parking.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public class FlagEntity {
    @JsonIgnore
    @Column
    private int flag = 0;

    private boolean getBitValue(int bitIndex) {
        return (this.flag & (1 << bitIndex)) != 0;
    }

    private void setBitValue(int bitIndex, boolean value) {
        if (value) {
            this.flag |= (1 << bitIndex);
        } else {
            this.flag &= ~(1 << bitIndex);
        }
    }

}
