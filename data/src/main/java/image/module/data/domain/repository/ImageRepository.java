package image.module.data.domain.repository;

import image.module.data.domain.Image;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, UUID> {
  Optional<Image> findByStoredFileName(String storedFileName);
}
