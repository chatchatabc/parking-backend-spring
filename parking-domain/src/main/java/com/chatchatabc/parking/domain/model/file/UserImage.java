package com.chatchatabc.parking.domain.model.file;

import com.chatchatabc.parking.domain.model.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "user_images")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserImage extends CloudFileEntity {
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
