package cz.cvut.fit.sp1.api.data

import cz.cvut.fit.sp1.api.data.model.UserAccount

class UserAccountTestData {

    companion object {
        fun defaultUser0(): UserAccount = UserAccount(
            name = "name0",
            email = "email0",
            password = "password",
            token = "token0",
            authToken = "authToken0",
        )

        fun defaultUser01(): UserAccount = UserAccount(
            name = "name01",
            email = "email01",
            password = "password",
            token = "token01",
            authToken = "authToken01",
        )

        fun defaultUser10(): UserAccount = UserAccount(
            name = "name10",
            email = "email10",
            password = "password",
            token = "token10",
            authToken = "authToken10",
        )
    }
}