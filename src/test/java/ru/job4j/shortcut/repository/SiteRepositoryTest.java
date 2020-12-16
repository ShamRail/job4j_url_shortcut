package ru.job4j.shortcut.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.shortcut.config.Profiles;
import ru.job4j.shortcut.model.Site;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest(excludeAutoConfiguration = {
        LiquibaseAutoConfiguration.class
})
@ActiveProfiles(profiles = {Profiles.TEST})
public class SiteRepositoryTest {

    @Autowired
    private SiteRepository siteDB;

    @Test
    public void whenCreate() {
        Site site = new Site("login", "pass", "vk.com");
        siteDB.save(site);
        assertEquals(site, siteDB.findById(site.getId()).orElse(new Site()));
    }

    @Test
    public void whenFindByLogin() {
        Site site1 = new Site("site1", "pass1", "vk.com");
        Site site2 = new Site("site2", "pass1", "google.com");
        Site site3 = new Site("site3", "pass1", "yandex.ru");
        siteDB.saveAll(List.of(site1, site2, site3));
        assertEquals(site1, siteDB.findByLogin(site1.getLogin()));
    }

    @Test
    public void whenFindBySite() {
        Site site1 = new Site("site1", "pass1", "vk.com");
        Site site2 = new Site("site2", "pass1", "google.com");
        Site site3 = new Site("site3", "pass1", "yandex.ru");
        siteDB.saveAll(List.of(site1, site2, site3));
        assertEquals(site1, siteDB.findBySite(site1.getSite()));
    }

}