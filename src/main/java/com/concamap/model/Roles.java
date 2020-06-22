package com.concamap.model;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "shark_squad")
@Data
public class Roles {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Basic
    @Column(name = "status", nullable = false)
    private int status;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Basic
    @Column(name = "updated_date", nullable = false)
    private Timestamp updatedDate;

    @OneToMany(mappedBy = "roles")
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    @HashCodeExclude
    private Set<Users> users;
}
