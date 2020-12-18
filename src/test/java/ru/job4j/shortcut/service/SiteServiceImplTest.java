package ru.job4j.shortcut.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.dto.RegistrationResultDTO;
import ru.job4j.shortcut.repository.SiteRepository;

@RunWith(SpringRunner.class)
public class SiteServiceImplTest {

    @TestConfiguration
    static class TestConfig {

        @MockBean
        private SiteRepository siteRepository;

        @MockBean
        private SequenceGenerator sequenceGenerator;

        @MockBean
        private PasswordEncoder passwordEncoder;

        @Bean
        SiteService siteService() {
            return new SiteServiceImpl(siteRepository, sequenceGenerator, passwordEncoder);
        }
    }

    @Autowired
    private SiteService siteService;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private SequenceGenerator sequenceGenerator;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void whenRegisterNewSite() {
        Site site = new Site("login", "pass", "job4j.ru");
        Mockito.when(siteRepository.findBySite(site.getSite())).thenReturn(null);
        Mockito.when(sequenceGenerator.generate(site.getSite())).thenReturn(site.getLogin());
        Mockito.when(sequenceGenerator.generate()).thenReturn(site.getPassword());
        Mockito.when(passwordEncoder.encode(site.getPassword())).thenReturn(site.getPassword());
        Mockito.when(siteRepository.save(site)).thenReturn(site);
        RegistrationResultDTO expected = new RegistrationResultDTO(
                true, site.getLogin(), site.getPassword()
        );
        Assert.assertEquals(expected, siteService.register(site));
    }

    @Test
    public void whenTryRegisterExistingSite() {
        Site site = new Site("login", "pass", "job4j.ru");
        Mockito.when(siteRepository.findBySite(site.getSite())).thenReturn(site);
        RegistrationResultDTO expected = new RegistrationResultDTO(
                false, site.getLogin(), site.getPassword()
        );
        Assert.assertEquals(expected, siteService.register(site));
    }
}