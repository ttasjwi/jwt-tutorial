package com.example.jwt.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User (

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "username", length = 50, unique = true)
    var username: String,

    @Column(name = "password", length = 100, nullable = false)
    var password: String,

    @Column(name = "nickname", length = 50, nullable = false)
    var nickname: String,

    @Column(name = "activated", nullable = false)
    var activated: Boolean,

    @ManyToMany
    @JoinTable(
        name = "user_authority",
        joinColumns = [JoinColumn(name = "user_id", referencedColumnName = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "authority_name", referencedColumnName = "authority_name")],
    )
    var authorities: MutableSet<Authority>
)
