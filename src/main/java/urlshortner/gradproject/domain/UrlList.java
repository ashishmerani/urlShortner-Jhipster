package urlshortner.gradproject.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A UrlList.
 */
@Entity
@Table(name = "url_list")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UrlList implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "long_url")
    private String longUrl;

    @Lob
    @Column(name = "short_url")
    private String shortUrl;

    @Column(name = "visit_count")
    private Integer visitCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public UrlList longUrl(String longUrl) {
        this.longUrl = longUrl;
        return this;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public UrlList shortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
        return this;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public UrlList visitCount(Integer visitCount) {
        this.visitCount = visitCount;
        return this;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UrlList urlList = (UrlList) o;
        if(urlList.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, urlList.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UrlList{" +
            "id=" + id +
            ", longUrl='" + longUrl + "'" +
            ", shortUrl='" + shortUrl + "'" +
            ", visitCount='" + visitCount + "'" +
            '}';
    }
}
