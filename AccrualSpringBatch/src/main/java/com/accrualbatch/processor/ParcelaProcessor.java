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
public class ParcelaProcessor implements ItemProcessor<Parcela, Parcela> {
	
	private static final MathContext RM = new MathContext(9, RoundingMode.HALF_UP);

    @Override
    public Parcela process(Parcela parcela) {

    	BigDecimal valorPresenteAtualizado = Objects.requireNonNullElse(parcela.getValorFuturo(), BigDecimal.ZERO).multiply(new BigDecimal("20.0"))
            .divide(BigDecimal.valueOf(100), RM);
        
        parcela.setValorPresente(valorPresenteAtualizado.round(RM));
      
        return parcela;
    }
}
