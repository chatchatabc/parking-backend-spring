package com.chatchatabc.parking

import com.chatchatabc.parking.domain.SpringContextUtils
import com.chatchatabc.parking.domain.model.User
import java.util.*

val String.user: Optional<User>
    get() {
        if (this.contains("@")) {
            return SpringContextUtils.getUserRepository().findByEmail(this)
        } else if (this.contains("-")) {
            return SpringContextUtils.getUserRepository().findByUserUuid(this)
        } else if (this.matches(Regex("^\\+?[0-9]\\d{1,14}\$"))) {
            return SpringContextUtils.getUserRepository().findByPhone(this)
        }
        return SpringContextUtils.getUserRepository().findByUsername(this)
    }