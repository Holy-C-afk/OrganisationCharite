package services;

import dto.DonateurResume;
import entities.Donateur;
import org.springframework.stereotype.Service;
import repositories.DonateurRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DonateurService {

    private final DonateurRepository donateurRepository;

    public DonateurService(DonateurRepository donateurRepository) {
        this.donateurRepository = donateurRepository;
    }

    public List<Donateur> getAll() {
        return donateurRepository.findAll();
    }

    public Donateur save(Donateur donateur) {
        return donateurRepository.save(donateur);
    }

    public Optional<Donateur> getByEmail(String email) {
        return donateurRepository.findByEmail(email);
    }

    public List<DonateurResume> getActifs() {
        return donateurRepository.findByDateFinAfter(LocalDate.now());
    }

    public void delete(Long id) {
        donateurRepository.deleteById(id);
    }
}