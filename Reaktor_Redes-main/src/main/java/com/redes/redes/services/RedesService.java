package com.redes.redes.services;

import com.redes.redes.dto.RedesEntradaDTO;
import com.redes.redes.models.Red;
import com.redes.redes.repositories.EstadoRepository;
import com.redes.redes.repositories.RedRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RedesService {

	@Autowired
    RedRepository repository;

	@Autowired
	EstadoRepository estadoRepository;

	public void registrarRed(RedesEntradaDTO redesDto) {
		if (redesDto == null || redesDto.getNombre() == null || redesDto.getNombre().isBlank()) {
			throw new IllegalArgumentException("El nombre de la red es obligatorio");
		}

		String ssid = redesDto.getNombre().trim();
		if (repository.findBySsid(ssid).isPresent()) {
			throw new IllegalArgumentException("La red ya existe");
		}

		Red nuevaRed = new Red(ssid, redesDto.getUsuario(), redesDto.getContrasena(), redesDto.getSeguridad()
		);

		repository.save(nuevaRed);
	}

	public List<Red> verRedes() {
		return repository.findAll();
	}

	@Transactional
	public void eliminarRed(Long id) {
		Red red = repository.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Red no encontrada"));

		estadoRepository.deleteByRed_Id(red.getId());

		String ssid = red.getSsid();

		if (ssid != null && !ssid.isBlank()) {
			estadoRepository.deleteBySsidIgnoreCase(ssid.trim());
		}

		repository.delete(red);
	}


}
