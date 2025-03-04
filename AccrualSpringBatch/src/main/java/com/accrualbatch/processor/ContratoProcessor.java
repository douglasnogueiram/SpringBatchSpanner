package com.accrualbatch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.accrualbatch.model.Contrato;
import com.accrualbatch.model.Parcela;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Objects;

@Component
public class ContratoProcessor implements ItemProcessor<Contrato, Contrato> {
	
	private static final MathContext RM = new MathContext(9, RoundingMode.HALF_UP);

    @Override
    public Contrato process(Contrato contrato) {
        

    	//BigDecimal valorFinanciadoAtualizado = contrato.getValorFinanciado().multiply(new BigDecimal("20.0"))
        //    .divide(BigDecimal.valueOf(100), RM);
        
    	BigDecimal valorPresenteAtualizado = Objects.requireNonNullElse(contrato.getValorPresente(), BigDecimal.ZERO)
    	        .multiply(BigDecimal.ONE)
    	        .round(RM);

    	BigDecimal valorFuturoAtualizado = Objects.requireNonNullElse(contrato.getValorFuturo(), BigDecimal.ZERO)
    	        .multiply(BigDecimal.ONE)
    	        .round(RM);

    	BigDecimal valorRendasApropriarAtualizado = Objects.requireNonNullElse(contrato.getRendasApropriar(), BigDecimal.ZERO)
    	        .multiply(BigDecimal.ONE)
    	        .round(RM);

    	BigDecimal valorAccrualAtualizado = Objects.requireNonNullElse(contrato.getAccrual(), BigDecimal.ZERO)
    	        .multiply(BigDecimal.ONE)
    	        .round(RM);
    	
    	contrato.setValorPresente(valorPresenteAtualizado);
    	contrato.setValorFuturo(valorFuturoAtualizado);
    	contrato.setRendasApropriar(valorRendasApropriarAtualizado);
    	contrato.setAccrual(valorAccrualAtualizado);
    	
    	System.out.println("Contrato: " + contrato.getIdContratoHash() + " Valor presente: " + valorPresenteAtualizado);
        return contrato;
    }
}

