package com.accrualbatch.repository;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.accrualbatch.model.Parcela;

import java.util.List;

import javax.sql.DataSource;



@Component
public class ParcelaRepository {
	
	private final DataSource applicationDataSource;
	
    private final JdbcTemplate jdbcTemplate;

    public ParcelaRepository(JdbcTemplate jdbcTemplate, @Qualifier("applicationDataSource")DataSource applicationDataSource) {
        this.applicationDataSource = applicationDataSource;
		this.jdbcTemplate = jdbcTemplate;
        jdbcTemplate.setDataSource(applicationDataSource);
    }

    @SuppressWarnings("deprecation")
    public List<Parcela> buscarParcelasPorContrato(String idContratoHash) {
        String sql = """
            SELECT id_contrato_hash, id_parcela, data_inicio_vigencia, data_fim_vigencia, valor_futuro, valor_presente
            FROM tb_pf_parcela
            WHERE id_contrato_hash = ?
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Parcela parcela = new Parcela();
            parcela.setIdContratoHash(rs.getString("id_contrato_hash"));
            parcela.setIdParcela(rs.getInt("id_parcela"));
            parcela.setDataInicioVigencia(rs.getDate("data_inicio_vigencia"));  // Converte para LocalDate
            parcela.setDataFimVigencia(rs.getDate("data_fim_vigencia"));        // Converte para LocalDate
            parcela.setValorFuturo(rs.getBigDecimal("valor_futuro"));
            parcela.setValorPresente(rs.getBigDecimal("valor_presente"));
            return parcela;
        }, idContratoHash); // Usa varargs diretamente
    }
}
    