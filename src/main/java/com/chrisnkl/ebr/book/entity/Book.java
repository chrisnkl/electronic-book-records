package com.chrisnkl.ebr.book.entity;

import com.chrisnkl.ebr.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Entity
@Table(name = "books", indexes = {
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_isbn", columnList = "isbn"),
        @Index(name = "idx_author", columnList = "author_id"),
        @Index(name = "idx_category", columnList = "category_id")
})
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {
        "category",
        "author"
})
public class Book extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", unique = true, nullable = false, length = 13)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    @Column(name = "publisher", nullable = false)
    private String publisher;

    @Column(name = "publication_year", nullable = false)
    private Short publicationYear;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "description")
    private String description;

}
