package com.accrualbatch.services;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import com.accrualbatch.config.BatchProperties;
import com.accrualbatch.data.ContratoItemReader;
import com.accrualbatch.data.ContratoItemWriter;
import com.accrualbatch.data.ParcelaItemReader;
import com.accrualbatch.data.ParcelaItemWriter;
import com.accrualbatch.model.Contrato;
import com.accrualbatch.model.Parcela;
import com.accrualbatch.processor.ContratoProcessor;


@Configuration
@EnableBatchProcessing
public class BatchJobConfig {

    @Bean
    public JobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
        TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }

    @Bean
    public Job processContratoJob(JobRepository jobRepository, Step step1, Step step2) {
        return new JobBuilder("processContratoJob", jobRepository)
                .start(step1)  // Etapa 1: Ler contratos
                .next(step2)
                .build();
    }
    
    
    


    @Bean
    public Step step1(JobRepository jobRepository, 
                      @Qualifier("batchTransactionManager") PlatformTransactionManager transactionManager,
                      ParcelaItemReader reader, ItemProcessor<Parcela, Parcela> parcelaProcessor,
                       ParcelaItemWriter writer) throws Exception {
        return new StepBuilder("step1", jobRepository)
                .<Parcela, Parcela>chunk(100, transactionManager)
                .reader(reader.parcelaReader(null))
                .processor(parcelaProcessor)// Leitura dos contratos
                .writer(writer.parcelaWriter(null))  // Escrita no Spanner
                .build();
    }
    
    @Bean
    public Step step2(JobRepository jobRepository, 
                      @Qualifier("batchTransactionManager") PlatformTransactionManager transactionManager,
                      ContratoItemReader reader, CompositeItemProcessor<Contrato, Contrato> compositeProcessor,
                      ContratoItemWriter writer) throws Exception {
        return new StepBuilder("step2", jobRepository)
                .<Contrato, Contrato>chunk(100, transactionManager)
                .reader(reader.contratoReader(null))
                .processor(compositeProcessor)// Leitura dos contratos
                .writer(writer.contratoCreditoWriter(null))  // Escrita no Spanner
                .build();
    }
    
}
