package com.concamap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.persistence.*;
import java.sql.Timestamp;

import java.util.Set;

@Entity
@Table(name = "post", schema = "shark_squad")
@Data
public class Post {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "title", nullable = false)
    private String title;

    @Basic
    @Column(name = "content", nullable = false, length = -1)
    private String content;

    @Basic
    @Column(name = "status", nullable = false)
    @JsonIgnore
    private int status;

    @Basic
    @Column(name = "likes", nullable = false)
    private int likes;

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

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    @HashCodeExclude
    private Set<Attachment> attachments;

    @ManyToOne
    @JoinColumn(name = "categories_id", referencedColumnName = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    @HashCodeExclude
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    @HashCodeExclude
    private Users users;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    @HashCodeExclude
    private Set<Comment> comments;
}
