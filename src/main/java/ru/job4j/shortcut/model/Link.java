package ru.job4j.shortcut.model;

import javax.persistence.*;
import java.util.Objects;

@Table(name = "links")
@Entity
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String url;

    @ManyToOne
    private Site site;

    public Link() { }

    public Link(String url, Site site) {
        this.url = url;
        this.site = site;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Link link = (Link) o;
        return Objects.equals(id, link.id)
                && Objects.equals(url, link.url)
                && Objects.equals(site, link.site);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, url, site);
    }

    @Override
    public String toString() {
        return "Link{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", site=" + site +
                '}';
    }
}

