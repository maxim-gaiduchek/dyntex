package cz.cvut.fit.sp1.api.data.model.media

import cz.cvut.fit.sp1.api.data.model.UserAccount
import jakarta.persistence.Entity
import jakarta.persistence.ManyToMany

@Entity
class Mask(
    name: String,
    path: String,
    format: String,
) : Media(name, path, format){
    @ManyToMany(mappedBy = "likedMasks")
    var likedByUsers: MutableList<UserAccount> = mutableListOf()
}
