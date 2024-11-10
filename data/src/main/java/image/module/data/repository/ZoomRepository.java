package image.module.data.repository;

import image.module.data.domain.Zoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ZoomRepository extends JpaRepository<Zoom, UUID> {
}
