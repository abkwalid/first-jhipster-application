package tech.digiwise.japp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.digiwise.japp.domain.Authority;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
