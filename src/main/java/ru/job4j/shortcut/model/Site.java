package ru.job4j.shortcut.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name = "sites")
@Entity
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String login;

    private String password;

    private String site;

    @OneToMany(mappedBy = "site")
    private List<Link> links = new ArrayList<>();

    public Site() { }

    public Site(String site) {
        this.site = site;
    }

    public Site(String login, String password, String site) {
        this.login = login;
        this.password = password;
        this.site = site;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
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
        Site site1 = (Site) o;
        return Objects.equals(id, site1.id)
                && Objects.equals(login, site1.login)
                && Objects.equals(password, site1.password)
                && Objects.equals(site, site1.site);
    }

    @Override
    public String toString() {
        return "Site{"
                + "id=" + id
                + ", login='" + login + '\''
                + ", password='" + password + '\''
                + ", site='" + site + '\''
                + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, site);
    }
}
