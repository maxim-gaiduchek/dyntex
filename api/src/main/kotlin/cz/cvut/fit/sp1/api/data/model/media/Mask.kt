package cz.cvut.fit.sp1.api.data.model.media

import jakarta.persistence.Entity

@Entity
class Mask(
    name: String,
    path: String,
    format: String,
) : Media(name, path, format)
