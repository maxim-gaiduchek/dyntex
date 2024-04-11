package cz.cvut.fit.sp1.api.data.model.base

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.persistence.*
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.util.*

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
@JsonIgnoreProperties(value = ["createdAt", "updatedAt"], allowGetters = true)
abstract class StandardAuditModel : Serializable, EntityWithId {
    @Id
    @SequenceGenerator(
        name = "sequenceGenerator",
        sequenceName = "hibernate_sequence",
        allocationSize = 1000,
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "sequenceGenerator",
    )
    override var id: Long = 0

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    open var createdAt: Date? = null

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at", nullable = false)
    @LastModifiedDate
    open var updatedAt: Date? = null
}
