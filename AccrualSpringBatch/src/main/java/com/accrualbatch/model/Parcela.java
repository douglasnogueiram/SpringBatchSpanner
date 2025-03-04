package com.accrualbatch.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Data	
public class Parcela implements Serializable{
	private static final long serialVersionUID = 1L;
	private String idContratoHash;
	private int idParcela;
	private Date dataProcessamento;
	private Date dataPosicaoFinanceira;
	private Date dataInicioVigencia;
	private Date dataFimVigencia;
	private int diasParcela;
    private BigDecimal valorFuturo;
    private BigDecimal valorPresente;
    private BigDecimal valorMaior;
    private BigDecimal valorTotal;
    private BigDecimal rendasApropriar;
    private BigDecimal juros;
    private BigDecimal multa;
    private BigDecimal mora;
    private BigDecimal saldo;
    private String status;
    private int diasAtraso;
    private BigDecimal tir;
    private String idEventoGerador;
    private int versao;

}
