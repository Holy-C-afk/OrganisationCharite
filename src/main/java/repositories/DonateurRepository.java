package repositories;

import dto.DonateurResume;
import entities.Donateur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DonateurRepository extends JpaRepository<Donateur, Long> {
    List<DonateurResume> findByDateFinAfter(LocalDate date);
    Optional<Donateur> findByEmail(String email);
}