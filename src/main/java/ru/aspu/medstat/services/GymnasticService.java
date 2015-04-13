package ru.aspu.medstat.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.aspu.medstat.entities.Gymnastic;
import ru.aspu.medstat.repositories.GymnasticRepository;

@Component
public class GymnasticService {
	@Autowired
	private GymnasticRepository repo;
	 
	public List<Gymnastic> getAllUserGymnastics(final long userId) {
		return repo.findAllUserGyms(userId);
	}
}
