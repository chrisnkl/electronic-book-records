package com.chrisnkl.ebr.batch.config;

import com.chrisnkl.ebr.batch.domain.BookCsvRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.nio.charset.StandardCharsets;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class BookBatchConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    @StepScope
    public FlatFileItemReader<BookCsvRecord> bookReader(@Value("#{jobParameters[filePath]}") String filePath) {

        FlatFileItemReader<BookCsvRecord> reader = new FlatFileItemReader<>();

        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1); // SKip header
        reader.setName("bookReader");
        reader.setEncoding(StandardCharsets.UTF_8.name());

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(",");
        tokenizer.setNames(
                "isbn",
                "title",
                "author",
                "category",
                "publisher",
                "publicationYear",
                "description"
        );

        BeanWrapperFieldSetMapper<BookCsvRecord> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(BookCsvRecord.class);
        DefaultLineMapper<BookCsvRecord> lineMapper = new DefaultLineMapper<>();

        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(mapper);

        reader.setLineMapper(lineMapper);

        return reader;
    }

}
