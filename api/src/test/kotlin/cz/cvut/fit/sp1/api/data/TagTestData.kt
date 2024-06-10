package cz.cvut.fit.sp1.api.data

import cz.cvut.fit.sp1.api.data.model.Tag

class TagTestData {

    companion object {
        fun defaultTag0(): Tag = Tag(
            name = "name0",
            emoji = "emoji0",
        )

        fun defaultTag01(): Tag = Tag(
            name = "name01",
            emoji = "emoji01",
        )

        fun defaultTag10(): Tag = Tag(
            name = "name10",
            emoji = "emoji10",
        )
    }
}