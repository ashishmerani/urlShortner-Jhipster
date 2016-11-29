package urlshortner.gradproject.repository;

import urlshortner.gradproject.domain.UrlList;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UrlList entity.
 */
@SuppressWarnings("unused")
public interface UrlListRepository extends JpaRepository<UrlList,Long> {

}
