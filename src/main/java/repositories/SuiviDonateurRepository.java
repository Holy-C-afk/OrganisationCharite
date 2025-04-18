package repositories;

import entities.SuiviDonateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuiviDonateurRepository extends JpaRepository<SuiviDonateur, Long> {
}