package com.qualitronix.demo.service;

import com.qualitronix.demo.dto.TempoPorMaquinaDTO;
import com.qualitronix.demo.dto.TempoPorOpDTO;
import com.qualitronix.demo.dto.TempoPorOperadorDTO;
import com.qualitronix.demo.dto.TempoPorOperadorDetalhadoDTO;
import com.qualitronix.demo.repository.RelatorioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioService {

    private final RelatorioRepository relatorioRepository;

    // =====================================================
    // Tempo por OP
    // =====================================================
    public List<TempoPorOpDTO> getTempoPorOp() {
        return relatorioRepository.tempoPorOp().stream()
                .map(this::converterParaHorasMinutosOp)
                .collect(Collectors.toList());
    }

    // =====================================================
    // Tempo por operador
    // =====================================================
    public List<TempoPorOperadorDTO> getTempoPorOperador() {
        return relatorioRepository.tempoPorOperador().stream()
                .map(this::converterParaHorasMinutosOperador)
                .collect(Collectors.toList());
    }

    // =====================================================
    // Tempo por máquina
    // =====================================================
    public List<TempoPorMaquinaDTO> getTempoPorMaquina() {
        return relatorioRepository.tempoPorMaquina().stream()
                .map(this::converterParaHorasMinutosMaquina)
                .collect(Collectors.toList());
    }

    // =====================================================
    // Helpers para converter segundos em HH:mm
    // =====================================================
    private TempoPorOpDTO converterParaHorasMinutosOp(TempoPorOpDTO dto) {
        long horas = dto.getDuracaoSegundos() / 3600;
        long minutos = (dto.getDuracaoSegundos() % 3600) / 60;
        dto.setHorasMinutos(String.format("%d:%02d", horas, minutos));
        return dto;
    }

    private TempoPorOperadorDTO converterParaHorasMinutosOperador(TempoPorOperadorDTO dto) {
        long horas = dto.getTotalSegundos() / 3600;
        long minutos = (dto.getTotalSegundos() % 3600) / 60;
        dto.setHorasMinutos(String.format("%d:%02d", horas, minutos));
        return dto;
    }

    private TempoPorMaquinaDTO converterParaHorasMinutosMaquina(TempoPorMaquinaDTO dto) {
        long horas = dto.getTotalSegundos() / 3600;
        long minutos = (dto.getTotalSegundos() % 3600) / 60;
        dto.setHorasMinutos(String.format("%d:%02d", horas, minutos));
        return dto;
    }

    public List<TempoPorOperadorDetalhadoDTO> getTempoPorOperadorDetalhado() {
        return relatorioRepository.tempoPorOperadorDetalhado().stream()
                .map(dto -> {
                    // Protege caso totalSegundos seja null
                    long segundos = dto.getTotalSegundos() != null ? dto.getTotalSegundos() : 0;
                    long horas = segundos / 3600;
                    long minutos = (segundos % 3600) / 60;
                    dto.setHorasMinutos(String.format("%d:%02d", horas, minutos));
                    return dto;
                })
                .collect(Collectors.toList());
    }
}