package uz.javacourse.jgcp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.javacourse.jgcp.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPinfl(String pinfl);

    boolean existsByPinfl(String pinfl);

    boolean existsByEmail(String email);
}
