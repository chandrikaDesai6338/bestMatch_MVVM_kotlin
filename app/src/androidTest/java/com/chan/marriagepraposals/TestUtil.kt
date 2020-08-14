package com.chan.marriagepraposals

import com.chan.marriagepraposals.db.User

/**
 * Created by Chan on 14/08/20.
 */
object TestUtil {
    fun createUser() = User(
        uid = "123ert",
        name = "Becky",
        age = 23,
        dob = "dob",
        address = "address",
        picture = "picture",
        username = "username",
        password = "password",
        email = "email",
        status = "status"
    )
}