package com.accrualbatch.data;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.accrualbatch.model.Contrato;
import com.accrualbatch.model.Parcela;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.sql.DataSource;

@Configuration
public class ContratoItemWriter {
	
    private final DataSource applicationDataSource;
    
    private static final MathContext RM = new MathContext(8, RoundingMode.HALF_UP);

    public ContratoItemWriter(DataSource applicationDataSource) {
        this.applicationDataSource = applicationDataSource;
    }

    @Bean
    public JdbcBatchItemWriter<Contrato> contratoCreditoWriter(@Qualifier("applicationDataSource")DataSource applicationDataSource) {
        JdbcBatchItemWriter<Contrato> writer = new JdbcBatchItemWriter<>();
       
        writer.setDataSource(applicationDataSource);
       
        writer.setSql("UPDATE tb_pf_contrato SET valor_presente = ?, valor_futuro = ?, rendas_apropriar = ?, accrual = ? WHERE id_contrato_hash = ?");
       
        writer.setItemPreparedStatementSetter((contrato, ps) -> {
        	
        	System.out.println("Contrato a armazenar: " + contrato.getIdContratoHash() + " Valor presente: " + contrato.getValorPresente());
            ps.setBigDecimal(1, contrato.getValorPresente());
            ps.setBigDecimal(2, contrato.getValorFuturo());
            ps.setBigDecimal(3, contrato.getRendasApropriar());
            ps.setBigDecimal(4, contrato.getAccrual());
            ps.setString(5, contrato.getIdContratoHash());
            
        });
        return writer;
    }
}

