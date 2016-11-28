package urlshortner.gradproject.web.rest;

import com.codahale.metrics.annotation.Timed;
import urlshortner.gradproject.domain.UrlList;

import urlshortner.gradproject.repository.UrlListRepository;
import urlshortner.gradproject.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * REST controller for managing UrlList.
 */
@RestController
@RequestMapping("/api")
public class UrlListResource {

    private final Logger log = LoggerFactory.getLogger(UrlListResource.class);
        
    @Inject
    private UrlListRepository urlListRepository;

    /**
     * POST  /url-lists : Create a new urlList.
     *
     * @param urlList the urlList to create
     * @return the ResponseEntity with status 201 (Created) and with body the new urlList, or with status 400 (Bad Request) if the urlList has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/url-lists")
    @Timed
    public ResponseEntity<UrlList> createUrlList(@Valid @RequestBody UrlList urlList) throws URISyntaxException {
        log.debug("REST request to save UrlList : {}", urlList);
        if (urlList.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("urlList", "idexists", "A new urlList cannot already have an ID")).body(null);
        }
        String longUrl = urlList.getLongUrl();
        String shortUrl = shortenUrl(longUrl);
        urlList.setShortUrl(shortUrl);
        urlList.setVisitCount(0);
        UrlList result = urlListRepository.save(urlList);
        return ResponseEntity.created(new URI("/api/url-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("urlList", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /url-lists : Updates an existing urlList.
     *
     * @param urlList the urlList to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated urlList,
     * or with status 400 (Bad Request) if the urlList is not valid,
     * or with status 500 (Internal Server Error) if the urlList couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/url-lists")
    @Timed
    public ResponseEntity<UrlList> updateUrlList(@Valid @RequestBody UrlList urlList) throws URISyntaxException {
        log.debug("REST request to update UrlList : {}", urlList);
        if (urlList.getId() == null) {
            return createUrlList(urlList);
        }
        UrlList result = urlListRepository.save(urlList);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("urlList", urlList.getId().toString()))
            .body(result);
    }

    /**
     * GET  /url-lists : get all the urlLists.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of urlLists in body
     */
    @GetMapping("/url-lists")
    @Timed
    public List<UrlList> getAllUrlLists() {
        log.debug("REST request to get all UrlLists");
        List<UrlList> urlLists = urlListRepository.findAll();
        return urlLists;
    }

    /**
     * GET  /url-lists/:id : get the "id" urlList.
     *
     * @param id the id of the urlList to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the urlList, or with status 404 (Not Found)
     */
    @GetMapping("/url-lists/{id}")
    @Timed
    public ResponseEntity<UrlList> getUrlList(@PathVariable Long id) {
        log.debug("REST request to get UrlList : {}", id);
        UrlList urlList = urlListRepository.findOne(id);
        return Optional.ofNullable(urlList)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /url-lists/:id : delete the "id" urlList.
     *
     * @param id the id of the urlList to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/url-lists/{id}")
    @Timed
    public ResponseEntity<Void> deleteUrlList(@PathVariable Long id) {
        log.debug("REST request to delete UrlList : {}", id);
        urlListRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("urlList", id.toString())).build();
    }
    
    
    
    /*Shorten URL Logic */
    public String shortenUrl(String longUrl){
		/*
		 * 1. convert longUrl into 36 bit hash value
		 * 2. Take Max bits
		 * 3. Convert to String with base 36 encoding
		 * 4. Return value
		 */
		String encodedUrl = "";
		Integer hashKey = (int) UUID.nameUUIDFromBytes(longUrl.getBytes()).getMostSignificantBits();
		encodedUrl = Integer.toString(hashKey, 36);
		return "http://localhost:8080/URLShortner/short/" +encodedUrl;
	}

}
