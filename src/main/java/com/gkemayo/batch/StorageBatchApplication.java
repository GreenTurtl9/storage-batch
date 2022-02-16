package com.gkemayo.batch;

import com.gkemayo.batch.batchutils.BatchJobListener;
import com.gkemayo.batch.batchutils.BatchStepSkipper;
import com.gkemayo.batch.dto.ConvertedInputData;
import com.gkemayo.batch.dto.InputData;
import com.gkemayo.batch.processor.BatchProcessor;
import com.gkemayo.batch.reader.BatchReader;
import com.gkemayo.batch.writer.BatchWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@SpringBootApplication
@EnableBatchProcessing
@RequiredArgsConstructor
public class StorageBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(StorageBatchApplication.class, args);
    }

    @Value("${path.to.the.work.dir}")
    private String workDirPath;

    private final DataSource dataSource;

    private final PlatformTransactionManager transactionManager;

    @Bean
    public JobRepository jobRepositoryObj() throws Exception {
        JobRepositoryFactoryBean jobRepoFactory = new JobRepositoryFactoryBean();
        jobRepoFactory.setTransactionManager(transactionManager);
        jobRepoFactory.setDataSource(dataSource);
        return jobRepoFactory.getObject();
    }

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public BatchReader batchReader() {
        return new BatchReader(workDirPath);
    }

    @Bean
    public BatchProcessor batchProcessor() {
        return new BatchProcessor();
    }

    @Bean
    public BatchWriter batchWriter() {
        return new BatchWriter();
    }

    @Bean
    public BatchJobListener batchJobListener() {
        return new BatchJobListener();
    }

    @Bean
    public BatchStepSkipper batchStepSkipper() {
        return new BatchStepSkipper();
    }

    @Bean
    public Step batchStep() {
        return
                stepBuilderFactory.get("stepStorageLoader").transactionManager(transactionManager)
                        .<InputData, ConvertedInputData>chunk(1)
                        .reader(batchReader())
                        .processor(batchProcessor())
                        .writer(batchWriter()).faultTolerant().skipPolicy(batchStepSkipper()).build();
    }

    @Bean
    public Job batchJob() throws Exception {
        return
                jobBuilderFactory.get("jobStorageLoader").repository(jobRepositoryObj())
                        .incrementer(new RunIdIncrementer())
                        .flow(batchStep()).end().build();
    }

}