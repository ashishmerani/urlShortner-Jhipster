package urlshortner.gradproject.web.rest;

import urlshortner.gradproject.UrlShortnerApp;

import urlshortner.gradproject.domain.UrlList;
import urlshortner.gradproject.repository.UrlListRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UrlListResource REST controller.
 *
 * @see UrlListResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UrlShortnerApp.class)
public class UrlListResourceIntTest {

    private static final String DEFAULT_LONG_URL = "AAAAAAAAAA";
    private static final String UPDATED_LONG_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SHORT_URL = "AAAAAAAAAA";
    private static final String UPDATED_SHORT_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_VISIT_COUNT = 1;
    private static final Integer UPDATED_VISIT_COUNT = 2;

    @Inject
    private UrlListRepository urlListRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restUrlListMockMvc;

    private UrlList urlList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UrlListResource urlListResource = new UrlListResource();
        ReflectionTestUtils.setField(urlListResource, "urlListRepository", urlListRepository);
        this.restUrlListMockMvc = MockMvcBuilders.standaloneSetup(urlListResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UrlList createEntity(EntityManager em) {
        UrlList urlList = new UrlList()
                .longUrl(DEFAULT_LONG_URL)
                .shortUrl(DEFAULT_SHORT_URL)
                .visitCount(DEFAULT_VISIT_COUNT);
        return urlList;
    }

    @Before
    public void initTest() {
        urlList = createEntity(em);
    }

    @Test
    @Transactional
    public void createUrlList() throws Exception {
        int databaseSizeBeforeCreate = urlListRepository.findAll().size();

        // Create the UrlList

        restUrlListMockMvc.perform(post("/api/url-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(urlList)))
                .andExpect(status().isCreated());

        // Validate the UrlList in the database
        List<UrlList> urlLists = urlListRepository.findAll();
        assertThat(urlLists).hasSize(databaseSizeBeforeCreate + 1);
        UrlList testUrlList = urlLists.get(urlLists.size() - 1);
        assertThat(testUrlList.getLongUrl()).isEqualTo(DEFAULT_LONG_URL);
        assertThat(testUrlList.getShortUrl()).isEqualTo(DEFAULT_SHORT_URL);
        assertThat(testUrlList.getVisitCount()).isEqualTo(DEFAULT_VISIT_COUNT);
    }

    @Test
    @Transactional
    public void getAllUrlLists() throws Exception {
        // Initialize the database
        urlListRepository.saveAndFlush(urlList);

        // Get all the urlLists
        restUrlListMockMvc.perform(get("/api/url-lists?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(urlList.getId().intValue())))
                .andExpect(jsonPath("$.[*].longUrl").value(hasItem(DEFAULT_LONG_URL.toString())))
                .andExpect(jsonPath("$.[*].shortUrl").value(hasItem(DEFAULT_SHORT_URL.toString())))
                .andExpect(jsonPath("$.[*].visitCount").value(hasItem(DEFAULT_VISIT_COUNT)));
    }

    @Test
    @Transactional
    public void getUrlList() throws Exception {
        // Initialize the database
        urlListRepository.saveAndFlush(urlList);

        // Get the urlList
        restUrlListMockMvc.perform(get("/api/url-lists/{id}", urlList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(urlList.getId().intValue()))
            .andExpect(jsonPath("$.longUrl").value(DEFAULT_LONG_URL.toString()))
            .andExpect(jsonPath("$.shortUrl").value(DEFAULT_SHORT_URL.toString()))
            .andExpect(jsonPath("$.visitCount").value(DEFAULT_VISIT_COUNT));
    }

    @Test
    @Transactional
    public void getNonExistingUrlList() throws Exception {
        // Get the urlList
        restUrlListMockMvc.perform(get("/api/url-lists/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUrlList() throws Exception {
        // Initialize the database
        urlListRepository.saveAndFlush(urlList);
        int databaseSizeBeforeUpdate = urlListRepository.findAll().size();

        // Update the urlList
        UrlList updatedUrlList = urlListRepository.findOne(urlList.getId());
        updatedUrlList
                .longUrl(UPDATED_LONG_URL)
                .shortUrl(UPDATED_SHORT_URL)
                .visitCount(UPDATED_VISIT_COUNT);

        restUrlListMockMvc.perform(put("/api/url-lists")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedUrlList)))
                .andExpect(status().isOk());

        // Validate the UrlList in the database
        List<UrlList> urlLists = urlListRepository.findAll();
        assertThat(urlLists).hasSize(databaseSizeBeforeUpdate);
        UrlList testUrlList = urlLists.get(urlLists.size() - 1);
        assertThat(testUrlList.getLongUrl()).isEqualTo(UPDATED_LONG_URL);
        assertThat(testUrlList.getShortUrl()).isEqualTo(UPDATED_SHORT_URL);
        assertThat(testUrlList.getVisitCount()).isEqualTo(UPDATED_VISIT_COUNT);
    }

    @Test
    @Transactional
    public void deleteUrlList() throws Exception {
        // Initialize the database
        urlListRepository.saveAndFlush(urlList);
        int databaseSizeBeforeDelete = urlListRepository.findAll().size();

        // Get the urlList
        restUrlListMockMvc.perform(delete("/api/url-lists/{id}", urlList.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UrlList> urlLists = urlListRepository.findAll();
        assertThat(urlLists).hasSize(databaseSizeBeforeDelete - 1);
    }
}
