package com.dku.objectstorage.storage.dto

import com.dku.objectstorage.storage.domain.entity.vo.Permission

data class PermissionChangeRequest(
    val permission: Permission,
    val password: String? = null
)
