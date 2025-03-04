package com.accrualbatch.data;

import org.springframework.batch.item.database.JdbcCursorItemReader;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.accrualbatch.model.Contrato;
import com.accrualbatch.model.Parcela;

import javax.sql.DataSource;

@Configuration
public class ParcelaItemReader {
	
    private final DataSource applicationDataSource;

    public ParcelaItemReader(DataSource applicationDataSource) {
        this.applicationDataSource = applicationDataSource;
    }

    @Bean
    public JdbcCursorItemReader<Parcela> parcelaReader(
            @Qualifier("applicationDataSource") DataSource applicationDataSource) throws Exception {
    	
    	JdbcCursorItemReader<Parcela> reader = new JdbcCursorItemReader<>();
        
    	reader.setDataSource(applicationDataSource);
        
        reader.setSql("SELECT "
        		+ "id_contrato_hash, "
        		+ "id_parcela, "
        		+ "data_processamento, "
        		+ "data_posicao_financeira, "
        		+ "data_inicio_vigencia,"
        		+ "data_fim_vigencia,"
        		+ "dias_parcela,"
        		+ "valor_futuro,"
        		+ "valor_presente,"
        		+ "valor_maior,"
        		+ "valor_total,"
        		+ "rendas_apropriar,"
        		+ "juros,"
        		+ "multa,"
        		+ "mora,"
        		+ "saldo,"
        		+ "status,"
        		+ "dias_atraso,"
        		+ "tir,"
        		+ "id_evento_gerador,"
        		+ "versao"
        		+ " FROM tb_pf_parcela ORDER BY id_parcela");
        
        reader.setRowMapper((rs, rowNum) -> {
            
        	Parcela parcela = new Parcela();
            
            parcela.setIdContratoHash(rs.getString("id_contrato_hash"));
            parcela.setIdParcela(rs.getInt("id_parcela"));
            parcela.setDataProcessamento(rs.getDate("data_processamento"));
            parcela.setDataPosicaoFinanceira(rs.getDate("data_posicao_financeira"));
            parcela.setDataInicioVigencia(rs.getDate("data_inicio_vigencia"));
            parcela.setDataFimVigencia(rs.getDate("data_fim_vigencia"));
            parcela.setDiasParcela(rs.getInt("dias_parcela"));
            parcela.setValorFuturo(rs.getBigDecimal("valor_futuro"));
            parcela.setValorPresente(rs.getBigDecimal("valor_presente"));
            parcela.setValorMaior(rs.getBigDecimal("valor_maior"));
            parcela.setRendasApropriar(rs.getBigDecimal("rendas_apropriar"));
            parcela.setJuros(rs.getBigDecimal("juros"));
            parcela.setMulta(rs.getBigDecimal("multa"));
            parcela.setMora(rs.getBigDecimal("mora"));
            parcela.setSaldo(rs.getBigDecimal("saldo"));
            parcela.setStatus(rs.getString("status"));
            parcela.setDiasAtraso(rs.getInt("dias_atraso"));
            parcela.setTir(rs.getBigDecimal("tir"));
            parcela.setIdEventoGerador(rs.getString("id_evento_gerador"));
            parcela.setVersao(rs.getInt("versao"));
           
     
            return parcela;
        });


        return reader;
    }
    
}