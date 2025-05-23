package com.dku.objectstorage.user.domain.entity

import com.dku.objectstorage.common.jpa.shared.BaseEntity
import com.dku.objectstorage.user.domain.entity.vo.UserRole
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "user")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(name = "email", nullable = false, unique = true)
    var email: String,

    @Column(name = "password", nullable = false)
    var password: String,

    @Column(name = "name", nullable = false)
    var name: String,

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    var userRole: UserRole = UserRole.User,
) : BaseEntity() {
}
