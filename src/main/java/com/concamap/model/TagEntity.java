package com.concamap.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tag", schema = "blog")
public class TagEntity {
    private long id;
    private String description;
    private String name;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagEntity tagEntity = (TagEntity) o;
        return id == tagEntity.id &&
                Objects.equals(description, tagEntity.description) &&
                Objects.equals(name, tagEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name);
    }
}
