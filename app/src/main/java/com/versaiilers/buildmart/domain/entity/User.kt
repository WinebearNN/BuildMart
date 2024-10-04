package com.versaiilers.buildmart.domain.entity

import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import java.util.Date


@Entity
data class User(
    @Id var id:Long=0,
    var email: String = "",
    var password: String = "",
    var phoneNumber: String = "",
    var date: Date? = null,
    var displayName: String? = null
)
