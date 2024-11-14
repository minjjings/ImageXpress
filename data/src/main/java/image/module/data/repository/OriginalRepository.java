package image.module.data.repository;

import image.module.data.domain.Original;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface OriginalRepository extends JpaRepository<Original, UUID> {



    Original findByOriginalFileName(String originalFileName);
}
