package cz.cvut.fit.sp1.api.data.model

import cz.cvut.fit.sp1.api.data.model.base.StandardAuditModel
import cz.cvut.fit.sp1.api.data.model.media.Avatar
import cz.cvut.fit.sp1.api.data.model.media.Media
import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import java.util.*

@Entity
class UserAccount(
    var name: String,
    var email: String,
    var password: String,
    var token: String,
    var authToken: String,
) : StandardAuditModel() {
    @ColumnDefault("2")
    var role: AccountRole = AccountRole.USER

    @ManyToMany(fetch = FetchType.LAZY)
    var likedMedia: MutableList<Media> = mutableListOf()

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    var createdMedia: MutableList<Media> = mutableListOf()

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    var createdTags: MutableList<Tag> = mutableListOf()

    @OneToOne(mappedBy = "userAccount", cascade = [CascadeType.ALL], orphanRemoval = true)
    var avatar: Avatar? = null

    var authEnable: Boolean? = false

    var dateOfRecovery: Date? = null
}
