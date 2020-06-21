package com.concamap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String phone;

    @Basic
    @Column(name = "email", nullable = false, length = 225)
    @JsonIgnore
    private String email;

    @Basic
    @Column(name = "username", nullable = false, length = 100)
    @JsonIgnore
    private String username;

    @Basic
    @Column(name = "password", nullable = false, length = 100)
    @JsonIgnore
    private String password;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Basic
    @Column(name = "updated_time", nullable = false)
    @JsonIgnore
    private Timestamp updatedTime;

    @Basic
    @Column(name = "status", nullable = false)
    @JsonIgnore
    private int status;

    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private Collection<Comment> comments;

    @OneToMany(mappedBy = "users")
    @JsonIgnore
    private Collection<Post> posts;

    @ManyToOne
    @JoinColumn(name = "roles_id", referencedColumnName = "id", nullable = false)
    @JsonIgnore
    private Roles roles;
}
