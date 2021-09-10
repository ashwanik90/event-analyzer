package com.event.analyzer.config;

import com.event.analyzer.reader.EventLineMapper;
import com.event.analyzer.repository.Event;
import com.event.analyzer.writer.NoOpItemWriter;
import com.event.analyzer.processor.EventProcessor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.PathResource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {
    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Value("${batch.size:1000}")
    public int chunkSize;

    @Value("${input.file.path}")
    public String inputFilePath;


    @Bean
    public Job eventAnalyzeJob(Step step) {
        return jobBuilderFactory.get("eventAnalyzerJob")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .listener(listener())
                .build();
    }


    @Bean
    public Step step() {
        return stepBuilderFactory.get("eventAnalyzerStep")
                .<Event, Event>chunk(chunkSize).reader(reader())
                .processor(processor()).writer(writer()).build();
    }

    @Bean
    public FlatFileItemReader<Event> reader() {
        return new FlatFileItemReaderBuilder<Event>()
                .name("fileReader")
                .resource(new PathResource(inputFilePath))
                .lineMapper(new EventLineMapper())
                .build();
    }

    @Bean
    public ItemWriter<Event> writer() {
        return new NoOpItemWriter();
    }

    @Bean
    public ItemProcessor<Event, Event> processor() {
        return new EventProcessor();
    }

    @Bean
    public JobExecutionListener listener() {
        return new JobListener();
    }
}