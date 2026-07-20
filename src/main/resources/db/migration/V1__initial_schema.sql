-- Create tables --
CREATE TABLE authors (
                         id UUID PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,

                         created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                         updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                         version BIGINT NOT NULL
);

CREATE TABLE categories (
                            id UUID PRIMARY KEY,
                            name VARCHAR(50) NOT NULL UNIQUE,

                            created_at TIMESTAMP WITH TIME ZONE NOT NULL,
                            updated_at TIMESTAMP WITH TIME ZONE NOT NULL,
                            version BIGINT NOT NULL
);

CREATE TABLE books (

                       id UUID PRIMARY KEY,

                       isbn VARCHAR(13) NOT NULL UNIQUE,

                       title VARCHAR(255) NOT NULL,

                       author_id UUID NOT NULL,

                       publisher VARCHAR(255) NOT NULL,

                       publication_year SMALLINT NOT NULL,

                       category_id UUID NOT NULL,

                       description OID,

                       created_at TIMESTAMP WITH TIME ZONE NOT NULL,

                       updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

                       version BIGINT NOT NULL,

                       CONSTRAINT fk_book_author
                           FOREIGN KEY (author_id)
                               REFERENCES authors(id),

                       CONSTRAINT fk_book_category
                           FOREIGN KEY (category_id)
                               REFERENCES categories(id)
);

-- Create Indexes --
CREATE INDEX idx_book_title
    ON books(title);

CREATE INDEX idx_book_author
    ON books(author_id);

CREATE INDEX idx_book_category
    ON books(category_id);

CREATE INDEX idx_author_name
    ON authors(name);

CREATE INDEX idx_category_name
    ON categories(name);