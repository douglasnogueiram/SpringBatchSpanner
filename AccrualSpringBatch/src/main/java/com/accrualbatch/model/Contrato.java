package com.accrualbatch.model;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Data
public class Contrato implements Serializable {
    private static final long serialVersionUID = 1L;
	private int idTennant;
    private String idContratoHash;
    private String idContratoExterno;
    private int produto;
    private int modalidade;
    private String tipoPessoa;
    private Date dataPosicaoFinanceira;
    private Date dataImplantacao;
    private Date dataInicio;
    private Date dataLiquidacao;
    private BigDecimal valorLiberado;
    private BigDecimal valorEntrada;
    private BigDecimal valorFinanciado;
    private BigDecimal valorFuturo;
    private BigDecimal valorPresente;
    private BigDecimal valorMaior;
    private BigDecimal rendasApropriar;
    private BigDecimal accrual;
    private BigDecimal multa;
    private BigDecimal mora;
    private BigDecimal taxaPreFixada;
    private BigDecimal taxaPreFixadaDia;
    private String indexador;
    private String moeda;
    private int prazo;
    private String forma_calculo;
    private int curva;
    private String formulaJuros;
    private String periodicidadeJuros;
    private String sistemaAmortizacao;
    private String tipoParcela;
    private List<Parcela> parcelas;
    private List<Componente> componentes;
    private int versao;


    public class Componente implements Serializable{
        public String tipo;
        public BigDecimal valor;
    }
} 