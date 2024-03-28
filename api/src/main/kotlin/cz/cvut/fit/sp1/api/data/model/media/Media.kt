package cz.cvut.fit.sp1.api.data.model.media

import cz.cvut.fit.sp1.api.data.model.UserAccount
import cz.cvut.fit.sp1.api.data.model.base.StandardAuditModel
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
open class Media(
    open var name: String,
    open var path: String,
    open var format: String,
) : StandardAuditModel() {
    open var size: Long = 0
    open var description: String? = ""
    open var aspectRatio: Double = 0.0
    open var quality: String = "" // TODO: maybe should create enum type

    @ManyToMany(mappedBy = "likedMedia")
    open var likedBy: MutableList<UserAccount> = mutableListOf()

    @ManyToOne
    open var createdBy: UserAccount? = null
}
