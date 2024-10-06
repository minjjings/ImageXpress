package image.module.upload;

import image.module.upload.domain.Image;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, UUID> {
}
