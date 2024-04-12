package cz.cvut.fit.sp1.api.data.model

import cz.cvut.fit.sp1.api.data.model.base.StandardAuditModel
import cz.cvut.fit.sp1.api.data.model.media.Media
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import org.hibernate.annotations.ColumnDefault

@Entity
class UserAccount(
    var name: String,
    var email: String,
    var password: String,
    var token: String,
) : StandardAuditModel() {
    @ColumnDefault("2")
    var role: AccountRole = AccountRole.USER

    @ManyToMany(fetch = FetchType.LAZY)
    var likedMedia: MutableList<Media> = mutableListOf()

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    var createdMedia: MutableList<Media> = mutableListOf()

    @OneToMany(mappedBy = "createdBy", fetch = FetchType.LAZY)
    var createdTags: MutableList<Tag> = mutableListOf()
}
