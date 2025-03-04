package com.accrualbatch;

import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.accrualbatch.config.BatchProperties;

import de.codecentric.boot.admin.server.config.EnableAdminServer;

@SpringBootApplication
@EnableAdminServer
@RestController
@RequestMapping("/batch")
public class AccrualBatchApplication implements CommandLineRunner {
	
	@Autowired
    private JobLauncher jobLauncherNew;

    @Autowired
    private Job processContratoJob;
    
    @Autowired
    private final BatchProperties batchProperties; // 🔹 Injete as configurações

    @Autowired
    public AccrualBatchApplication(JobLauncher jobLauncherNew, Job processContratoJob, BatchProperties batchProperties) {
        this.jobLauncherNew = jobLauncherNew;
        this.processContratoJob = processContratoJob;
        this.batchProperties = batchProperties;
    }


	public static void main(String[] args) {
		SpringApplication.run(AccrualBatchApplication.class, args);
	}
	
    @Override
	public void run(String... args) throws Exception {
        System.out.println(">>> Iniciando Job Batch no Spanner...");
        JobExecution execution = jobLauncherNew.run(processContratoJob, new org.springframework.batch.core.JobParameters());
        System.out.println(">>> Job Finalizado com Status: " + execution.getStatus());
    }
    
    @PostMapping("/start")
    //public String startBatch(@RequestBody Map<String, String> params) {
    public String startBatch() {
        
    	try {
            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addLong("time", System.currentTimeMillis());

            // Use as configurações corretamente
            jobParametersBuilder.addString("initializeSchema", batchProperties.getJdbc().getInitializeSchema());
            jobParametersBuilder.addString("enabled", String.valueOf(batchProperties.getJdbc().isEnabled()));


            //params.forEach(jobParametersBuilder::addString);
           

            //JobExecution jobExecution = jobLauncherNew.run(processContratoJob, jobParametersBuilder.toJobParameters());
            JobExecution jobExecution = jobLauncherNew.run(processContratoJob, jobParametersBuilder.toJobParameters());
            return "Batch iniciado com sucesso! JobExecution ID: " + jobExecution.getId();
        } catch (Exception e) {
            return "Erro ao iniciar batch: " + e.getMessage();
        }
    	
    }

}
