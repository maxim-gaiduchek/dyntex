package cz.cvut.fit.sp1.api.data.model.media

import cz.cvut.fit.sp1.api.data.model.UserAccount
import cz.cvut.fit.sp1.api.data.model.base.StandardAuditModel
import jakarta.persistence.Entity
import jakarta.persistence.OneToOne

@Entity
class Avatar(
    var name: String,
    var path: String,
    var height: Int,
    var width: Int
) : StandardAuditModel() {
    @OneToOne
    var userAccount: UserAccount? = null
}
