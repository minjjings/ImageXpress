package image.module.data.repository;

import image.module.data.domain.Detail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DetailRepository extends JpaRepository<Detail, UUID> {


    Detail findByOriginalFileName(String originalFileName);
}
