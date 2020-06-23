package com.concamap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "attachments", schema = "shark_squad")
@Data
public class Attachment {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Basic
    @Column(name = "image_link", nullable = false, columnDefinition = "TEXT")
    private String imageLink;

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
    @JsonIgnore
    private Post post;
}
