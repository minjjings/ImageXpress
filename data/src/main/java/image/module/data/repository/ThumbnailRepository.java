package image.module.data.repository;

import image.module.data.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ThumbnailRepository extends JpaRepository<Thumbnail, UUID> {


    Thumbnail findByOriginalFileName(String originalFileName);
}
