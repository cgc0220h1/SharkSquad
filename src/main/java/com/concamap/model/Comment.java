package com.concamap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.builder.HashCodeExclude;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "comments", schema = "shark_squad")
@Data
public class Comment {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "content", nullable = false, length = -1)
    private String content;

    @Basic
    @Column(name = "status", nullable = false)
    @JsonIgnore
    private int status;

    @Basic
    @Column(name = "created_date", nullable = false)
    private Timestamp createdDate;

    @Basic
    @Column(name = "updated_date", nullable = false)
    private Timestamp updatedDate;

    @ManyToOne
    @JoinColumn(name = "posts_id", referencedColumnName = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    @HashCodeExclude
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    @ToString.Exclude
    @HashCodeExclude
    private Users users;
}
