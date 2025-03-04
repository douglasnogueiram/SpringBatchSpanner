package com.accrualbatch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.accrualbatch.model.Contrato;
import com.accrualbatch.model.Parcela;
import com.accrualbatch.repository.ParcelaRepository;

import java.util.List;

@Component
public class EnriquecedorContratoProcessor implements ItemProcessor<Contrato, Contrato> {

    private final ParcelaRepository parcelaRepository;

    public EnriquecedorContratoProcessor(ParcelaRepository parcelaRepository) {
        this.parcelaRepository = parcelaRepository;
    }

    @Override
    public Contrato process(Contrato contrato) {
        List<Parcela> parcelas = parcelaRepository.buscarParcelasPorContrato(contrato.getIdContratoHash());
        contrato.setParcelas(parcelas);
        return contrato;
    }
}

