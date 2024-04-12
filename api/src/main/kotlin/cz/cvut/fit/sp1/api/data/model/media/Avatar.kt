package cz.cvut.fit.sp1.api.data.model.media

import jakarta.persistence.Entity

@Entity
class Avatar(
    name: String,
    path: String,
    format: String,
    aspecRatio: Double,
) : Media(name, path, format)
