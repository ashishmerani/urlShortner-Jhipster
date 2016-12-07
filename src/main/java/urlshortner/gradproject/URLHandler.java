package urlshortner.gradproject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import urlshortner.gradproject.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import urlshortner.gradproject.repository.UserRepository;
import urlshortner.gradproject.domain.UrlList;
import urlshortner.gradproject.repository.UrlListRepository;
import urlshortner.gradproject.web.rest.UrlListResource;

@RestController

public class URLHandler {
	private final Logger log = LoggerFactory.getLogger(URLHandler.class);
	 @Inject
	private UrlListRepository urlListRepository;
	@Inject
    private UserRepository userRepository;
	
	@RequestMapping("/short/*")
	public ResponseEntity<String> handleshortURLMapping(HttpServletRequest request ) throws URISyntaxException{
		String shortUrl = "http://localhost:8080"+request.getRequestURI().toString();
		System.out.println(shortUrl);
		URI url ;
		boolean flag = false;
		List<UrlList> urlLists = urlListRepository.findAll();
		HttpHeaders httpHeaders = new HttpHeaders();



		for(UrlList item: urlLists){
			if(item.getShortUrl().contentEquals(shortUrl)){

				String longUrl = item.getLongUrl();
				flag = true;
				System.out.println(longUrl);
				url = new URI(longUrl);
				Integer currentvisitCount = item.getVisitCount();
				currentvisitCount++;
				item.setVisitCount(currentvisitCount);
				UrlList result = urlListRepository.save(item);	
				httpHeaders.setLocation(url);
			}
		}
		if(flag){	
			return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
		}
		
		
		return new ResponseEntity<>("URL not Found", HttpStatus.BAD_REQUEST);
		
	}

}
