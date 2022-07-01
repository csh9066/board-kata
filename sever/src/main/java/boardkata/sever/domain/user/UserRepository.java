package boardkata.sever.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    public boolean existsByEmail(String email);
}
