package com.chatchatabc.service.domain.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.Data
import org.hibernate.annotations.CreationTimestamp
import java.util.*

@Data
@Entity
@Table(name = "files")
open class File {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    open lateinit var id: String

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    open lateinit var uploadedBy: User

    @Column
    open lateinit var parentId: String

    @Column
    open lateinit var filename: String

    @Column
    open lateinit var mimetype: String

    @Column
    open var fileOrder: Int = 0

    @CreationTimestamp
    open lateinit var createdAt: Date
}