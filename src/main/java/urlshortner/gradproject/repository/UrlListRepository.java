package urlshortner.gradproject.repository;

import urlshortner.gradproject.domain.UrlList;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UrlList entity.
 */
@SuppressWarnings("unused")
public interface UrlListRepository extends JpaRepository<UrlList,Long> {

    @Query("select urlList from UrlList urlList where urlList.user.login = ?#{principal.username}")
    List<UrlList> findByUserIsCurrentUser();

}
