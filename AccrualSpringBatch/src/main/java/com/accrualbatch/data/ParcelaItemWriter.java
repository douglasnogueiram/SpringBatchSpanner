package com.accrualbatch.data;

import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.accrualbatch.model.Contrato;
import com.accrualbatch.model.Parcela;

import java.math.MathContext;
import java.math.RoundingMode;

import javax.sql.DataSource;

@Configuration
public class ParcelaItemWriter {
	
	private static final MathContext RM = new MathContext(9, RoundingMode.HALF_UP);
	
    private final DataSource applicationDataSource;

    public ParcelaItemWriter(DataSource applicationDataSource) {
        this.applicationDataSource = applicationDataSource;
    }

    @Bean
    public JdbcBatchItemWriter<Parcela> parcelaWriter(@Qualifier("applicationDataSource")DataSource applicationDataSource) {
        JdbcBatchItemWriter<Parcela> writer = new JdbcBatchItemWriter<>();
        writer.setDataSource(applicationDataSource);
        
        writer.setSql("UPDATE tb_pf_parcela SET valor_presente = ? WHERE id_contrato_hash = ? and id_parcela = ?");
        writer.setItemPreparedStatementSetter((parcela, ps) -> {
            ps.setBigDecimal(1, parcela.getValorPresente().round(RM));
            ps.setString(2, parcela.getIdContratoHash());
            ps.setInt(3, parcela.getIdParcela());
        });
        return writer;
    }
}
