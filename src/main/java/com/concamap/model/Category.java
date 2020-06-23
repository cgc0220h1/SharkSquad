package com.concamap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

import java.util.Set;

@Entity
@Table(name = "categories", schema = "shark_squad")
@Getter
@Setter
@NoArgsConstructor
public class Category {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Basic
    @Column(name = "description")
    private String description;

    @Basic
    @Column(name = "status")
    @JsonIgnore
    private Integer status;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Basic
    @Column(name = "updated_date", nullable = false)
    private Timestamp updatedDate;

    @Basic
    @Column(name = "anchor_name", nullable = false, unique = true)
    @JsonIgnore
    private String anchorName;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Post> posts;
}
