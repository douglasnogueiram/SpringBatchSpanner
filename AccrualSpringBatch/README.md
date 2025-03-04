
# Spting Batch with Google Spanner

Java package with a generic example - Loans daily processing

  

## Spanner configuration

> [!NOTE]
> Google Spanner does not have official support from Spring and runs based on a Postgres configuration

 1. Spanner setup

 2. Create database using your desired Google Spanner parameters
 3. Create tables:
 
```SQL
CREATE  TABLE
tb_pf_contrato  (  id_contrato_hash  STRING(255)  NOT  NULL,
id_tenant  INT64  NOT  NULL,
id_contrato_externo  STRING(255),
produto  INT64,
modalidade  INT64,
tipo_pessoa  STRING(2),
data_processamento  TIMESTAMP  OPTIONS  (  allow_commit_timestamp = TRUE),
data_posicao_financeira  DATE,
data_inicio  DATE,
data_liquidacao  DATE,
valor_liberado  NUMERIC,
valor_financiado  NUMERIC,
valor_futuro  NUMERIC,
valor_presente  NUMERIC,
valor_maior  NUMERIC,
rendas_apropriar  NUMERIC,
accrual  NUMERIC,
taxa_multa  NUMERIC,
taxa_mora  NUMERIC,
multa  NUMERIC,
mora  NUMERIC,
penalidades  NUMERIC,
iof  NUMERIC,
saldo  NUMERIC,
status  STRING(255),
tier  NUMERIC,
stop_accrual  STRING(2),
dias_atraso  INT64,
forma_calculo  STRING(255),
curva  INT64,
formula_juros  STRING(255),
periodicidade_juros  STRING(255),
sistema_amortizacao  STRING(255),
tipo_parcela  STRING(255),
parcelas  INT64,
id_evento_gerador  STRING(255),
versao  INT64,
)
PRIMARY  KEY
(id_contrato_hash);
```
 4. Installments (parcelas)
 
```SQL
CREATE  TABLE
tb_pf_parcela  (  id_contrato_hash  STRING(255)  NOT  NULL,
data_processamento  TIMESTAMP  OPTIONS  (  allow_commit_timestamp = TRUE  ),
data_posicao_financeira  DATE,
data_inicio_vigencia  DATE,
data_fim_vigencia  DATE,
dias_parcela  INT64,
id_parcela  INT64,
valor_futuro  NUMERIC,
valor_presente  NUMERIC,
valor_maior  NUMERIC,
valor_total  NUMERIC,
rendas_apropriar  NUMERIC,
juros  NUMERIC,
multa  NUMERIC,
mora  NUMERIC,
saldo  NUMERIC,
status  STRING(255),
dias_atraso  INT64,
tir  NUMERIC,
id_evento_gerador  STRING(255),
versao  INT64,)
PRIMARY  KEY
(id_contrato_hash,
id_parcela),
INTERLEAVE  IN  PARENT  tb_pf_contrato
ON
DELETE CASCADE;
```


 2. Spring Batch configuration

In this example, we have a batch process with a typical ETL model:
* Read data from Google Spanner tables
* Process data
* Write processed data to Google Spanner

The batch runs with 2 main steps:

* Read data from tb_pf_parcela
* Process data (perform a simple multiplication)
* Write processed data to Google Spanner

```JAVA
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
 ```
 
 
* Read data from tb_pf_contrato
* Process data (perform a simple multiplication)
* Write processed data to Google Spanner

```JAVA
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
```

> [!NOTE]
> This exaple does not implement any valid financial calculation


 3. Run the application
 
 * In this example, it's possible to start a new job by HTTP request:
 
 ```bash
 curl -X POST http://localhost:8080/batch/start -H "Content-Type: application/json"
 ```
 
