package com.concamap.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "shark_squad")
@Getter
@Setter
@NoArgsConstructor
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
    @EqualsAndHashCode.Exclude
    private Set<Users> users;
}
