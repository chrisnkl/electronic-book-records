package com.chrisnkl.ebr.batch.config;

import com.chrisnkl.ebr.batch.domain.BookCsvRecord;
import com.chrisnkl.ebr.batch.processor.BookItemProcessor;
import com.chrisnkl.ebr.batch.writer.BookItemWriter;
import com.chrisnkl.ebr.book.entity.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
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

    @Value("${batch.books.delimiter:,}")
    private String delimiter;

    @Value("${batch.books.chunkSize:100}")
    private int chunkSize;

    @Bean
    @StepScope
    public FlatFileItemReader<BookCsvRecord> bookReader(@Value("#{jobParameters['filePath']}") String filePath) throws Exception {

        FlatFileItemReader<BookCsvRecord> reader = new FlatFileItemReader<>();

        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1); // SKip header
        reader.setEncoding(StandardCharsets.UTF_8.name());
        reader.setStrict(true);
        reader.setName("bookReader");

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(delimiter);
        tokenizer.setQuoteCharacter('"');
        tokenizer.setNames(
                "isbn",
                "title",
                "author",
                "category",
                "publisher",
                "publicationYear",
                "description"
        );

        DefaultLineMapper<BookCsvRecord> lineMapper = new DefaultLineMapper<>();
        lineMapper.setLineTokenizer(tokenizer);

        BeanWrapperFieldSetMapper<BookCsvRecord> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(BookCsvRecord.class);

        lineMapper.setFieldSetMapper(mapper);
        reader.setLineMapper(lineMapper);

        // If any property is missing, the reader will throw an exception at startup
        reader.afterPropertiesSet();

        return reader;
    }

    @Bean
    public Step importBooksStep(
            FlatFileItemReader<BookCsvRecord> reader,
            BookItemProcessor processor,
            BookItemWriter writer
    ) {
        return new StepBuilder("importBooksStep", jobRepository)
                .<BookCsvRecord, Book>chunk(chunkSize, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job importBooksJob(Step importBooksStep) {
        return new JobBuilder("importBooksJob", jobRepository)
                .start(importBooksStep)
                .build();
    }

}
