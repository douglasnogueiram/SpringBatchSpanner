package com.accrualbatch.data;

import org.springframework.batch.item.database.JdbcCursorItemReader;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.accrualbatch.model.Contrato;



import javax.sql.DataSource;

@Configuration
public class ContratoItemReader {
	
    private final DataSource applicationDataSource;

    public ContratoItemReader(DataSource applicationDataSource) {
        this.applicationDataSource = applicationDataSource;
    }

    @Bean
    public JdbcCursorItemReader<Contrato> contratoReader(
            @Qualifier("applicationDataSource") DataSource applicationDataSource) throws Exception {
    	JdbcCursorItemReader<Contrato> reader = new JdbcCursorItemReader<>();
       
    	reader.setDataSource(applicationDataSource);
        
        reader.setSql("SELECT\n"
        		+ "    a.id_contrato_hash,\n"
        		+ "    SUM(b.valor_presente) AS total_valor_presente,\n"
        		+ "    SUM(b.valor_futuro) AS total_valor_futuro,\n"
        		+ "    SUM(b.valor_maior) AS total_valor_maior,\n"
        		+ "    SUM(b.rendas_apropriar) AS total_rendas_apropriar,\n"
        		+ "    SUM(b.juros) AS total_accrual,\n"
        		+ "    SUM(b.multa) AS total_multa,\n"
        		+ "    SUM(b.mora) AS total_mora\n"
        		+ " FROM\n"
        		+ "    tb_pf_contrato AS a\n"
        		+ "INNER JOIN\n"
        		+ "    tb_pf_parcela AS b ON a.id_contrato_hash = b.id_contrato_hash\n"
        		+ "GROUP BY\n"
        		+ "    a.id_contrato_hash");
        
        reader.setRowMapper((rs, rowNum) -> {
            Contrato contrato = new Contrato();
            contrato.setIdContratoHash(rs.getString("id_contrato_hash"));
            contrato.setValorPresente(rs.getBigDecimal("total_valor_presente"));
            contrato.setValorFuturo(rs.getBigDecimal("total_valor_futuro"));
            contrato.setValorMaior(rs.getBigDecimal("total_valor_maior"));
            contrato.setRendasApropriar(rs.getBigDecimal("total_rendas_apropriar"));
            contrato.setAccrual(rs.getBigDecimal("total_accrual"));
            contrato.setMulta(rs.getBigDecimal("total_multa"));
            contrato.setMora(rs.getBigDecimal("total_mora"));
           
            return contrato;
        });


        return reader;
    }
    
}

