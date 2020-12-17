package ru.job4j.shortcut.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.shortcut.model.Link;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.repository.LinkRepository;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class LinkServiceImplTest {

    @TestConfiguration
    static class Config {

        @MockBean
        private SequenceGenerator sequenceGenerator;

        @MockBean
        private LinkRepository linkRepository;

        @Bean
        LinkService linkService() {
            return new LinkServiceImpl(sequenceGenerator, linkRepository);
        }

    }

    @Autowired
    private SequenceGenerator sequenceGenerator;

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private LinkService linkService;

    @Test
    public void whenSaveNewLink() {
        Link link = new Link("url", new Site(), "123");
        Mockito.when(linkRepository.findByUrl(link.getUrl())).thenReturn(null);
        Mockito.when(sequenceGenerator.generate()).thenReturn(link.getCode());
        Mockito.when(linkRepository.save(link)).thenReturn(link);
        Assert.assertEquals(link, linkService.registerLink(link));
    }

    @Test
    public void whenRegisterExistingLink() {
        Link link = new Link("url", new Site(), "123");
        Mockito.when(linkRepository.findByUrl(link.getUrl())).thenReturn(link);
        Assert.assertEquals(link, linkService.registerLink(link));
    }

}