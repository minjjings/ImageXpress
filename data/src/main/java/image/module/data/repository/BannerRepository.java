package image.module.data.repository;

import image.module.data.domain.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface BannerRepository extends JpaRepository<Banner, UUID> {
}
