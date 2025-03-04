package com.accrualbatch.config;

import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.accrualbatch.processor.ContratoProcessor;
import com.accrualbatch.processor.EnriquecedorContratoProcessor;
import com.accrualbatch.model.Contrato;

import java.util.Arrays;

@Configuration
public class BatchProcessorConfig {

    @Bean
    CompositeItemProcessor<Contrato, Contrato> compositeProcessor(
            EnriquecedorContratoProcessor enriquecedorContratoProcessor, // Processador que busca parcelas
            ContratoProcessor contratoProcessor) {
    
        
        CompositeItemProcessor<Contrato, Contrato> compositeProcessor = new CompositeItemProcessor<>();
        compositeProcessor.setDelegates(Arrays.asList(
            enriquecedorContratoProcessor, // 1º Enriquecedor (busca parcelas)
            contratoProcessor             // 2º Processador final
        ));
        
        return compositeProcessor;
    }
}
