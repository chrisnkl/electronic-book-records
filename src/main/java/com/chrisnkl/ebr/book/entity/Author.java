package com.chrisnkl.ebr.book.entity;

import com.chrisnkl.ebr.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "authors", indexes = @jakarta.persistence.Index(name = "idx_author_name", columnList = "name"))
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString
public class Author extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name",  nullable = false)
    private String name;

}
