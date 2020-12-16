package ru.job4j.shortcut.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.shortcut.model.Link;
import ru.job4j.shortcut.model.Site;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest(excludeAutoConfiguration = {
        LiquibaseAutoConfiguration.class
})
public class LinkRepositoryTest {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Test
    public void whenCreate() {
        Site site = siteRepository.save(new Site("a", "b", "c.com"));
        Link link = new Link("c.com/***", site, "123");
        assertEquals(link, linkRepository.save(link));
    }

    @Test
    public void whenFindBySite() {
        Site site = siteRepository.save(new Site("a", "b", "c.com"));
        Link link1 = linkRepository.save(new Link("c.com/1", site, "123"));
        Link link2 = linkRepository.save(new Link("c.com/2", site, "456"));
        Link link3 = linkRepository.save(new Link("c.com/3", site, "789"));
        assertEquals(List.of(link1, link2, link3), linkRepository.findBySite(site));
    }

    @Test
    public void whenFindByCode() {
        Site site = siteRepository.save(new Site("a", "b", "c.com"));
        Link link1 = linkRepository.save(new Link("c.com/1", site, "123"));
        Link link2 = linkRepository.save(new Link("c.com/2", site, "456"));
        Link link3 = linkRepository.save(new Link("c.com/3", site, "789"));
        assertEquals(link2, linkRepository.findByCode(link2.getCode()));
    }

    @Test
    public void whenIncreaseTotal() {
        Site site = siteRepository.save(new Site("a", "b", "c.com"));
        Link link = linkRepository.save(new Link("c.com/1", site, "123"));
        linkRepository.increaseTotal(link);
        linkRepository.increaseTotal(link);
        linkRepository.increaseTotal(link);
        assertEquals(3, linkRepository.findById(link.getId()).orElse(new Link()).getTotal());
    }

}