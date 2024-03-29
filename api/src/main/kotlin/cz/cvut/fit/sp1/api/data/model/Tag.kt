package cz.cvut.fit.sp1.api.data.model

import cz.cvut.fit.sp1.api.data.model.base.StandardAuditModel
import cz.cvut.fit.sp1.api.data.model.media.Media
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne

@Entity
class Tag(
    var name: String,
    var emoji: String,
) : StandardAuditModel() {
    @ManyToOne(fetch = FetchType.LAZY)
    var createdBy: UserAccount? = null

    @ManyToMany(fetch = FetchType.LAZY)
    var media: MutableList<Media> = mutableListOf()
}
