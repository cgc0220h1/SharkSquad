package com.concamap.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "users", schema = "shark_squad")
@Data
public class Users {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Basic
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Basic
    @Column(name = "phone", nullable = false, length = 50)
    private String phone;

    @Basic
    @Column(name = "email", nullable = false, length = 225)
    private String email;

    @Basic
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @Basic
    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Basic
    @Column(name = "updated_time", nullable = false)
    private Timestamp updatedTime;

    @Basic
    @Column(name = "status", nullable = false)
    private int status;

    @OneToMany(mappedBy = "users")
    private Collection<Comment> commentsById;

    @OneToMany(mappedBy = "users")
    private Collection<Post> postsById;

    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id", nullable = false)
    private Roles rolesByRolesId;
}
