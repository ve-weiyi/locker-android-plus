package com.ve.module.lockit.common.service.model

import android.view.autofill.AutofillId

data class ParsedStructure(
    var usernameId: AutofillId?,
    var passwordId: AutofillId?
) {
    fun isValid(): Boolean {
        return usernameId != null || passwordId != null
    }
}