package ru.job4j.shortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.shortcut.model.Link;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.dto.RegistrationResultDTO;
import ru.job4j.shortcut.service.LinkService;
import ru.job4j.shortcut.service.SiteService;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(ShortcutController.class)
public class ShortcutControllerTest {

    @MockBean
    SiteService siteService;

    @MockBean
    LinkService linkService;

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void whenSuccessRegisterSite() throws Exception {
        Site site = new Site("login", "pass", "vk.com");
        RegistrationResultDTO resultDTO = new RegistrationResultDTO(
                true, site.getLogin(), site.getPassword()
        );
        Mockito.when(siteService.register(site)).thenReturn(resultDTO);
        mvc.perform(
                post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(site))
        )
                .andExpect(jsonPath("$.registration", Is.is(true)))
                .andExpect(jsonPath("$.login", Is.is(site.getLogin())))
                .andExpect(jsonPath("$.password", Is.is(site.getPassword())));
    }

    @Test
    @WithMockUser
    public void whenConvertLink() throws Exception {
        Site site = new Site("login", "pass", "vk.com");
        Mockito.when(siteService.findByLogin("user")).thenReturn(site);
        Link link = new Link("url", site, "code");
        Mockito.when(linkService.registerLink(link)).thenReturn(link);
        mvc.perform(
                post("/convert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of(
                        "url", "url"
                )))
        )
                .andExpect(jsonPath("$.url", Is.is(link.getUrl())))
                .andExpect(jsonPath("$.total", Is.is(0)))
                .andExpect(jsonPath("$.code", Is.is(link.getCode())));
    }

    @Test
    public void whenRedirectAndInvalidCode() throws Exception {
        String code = "code";
        Mockito.when(linkService.findByCode(code)).thenReturn(null);
        mvc.perform(
                get("/redirect/" + code)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    public void whenRedirect() throws Exception {
        String code = "code";
        Site site = new Site("login", "pass", "vk.com");
        Link link = new Link("url", site, code);
        Mockito.when(linkService.findByCode(code)).thenReturn(link);
        Mockito.when(linkService.updateStatistic(link)).thenReturn(1);
        mvc.perform(
                get("/redirect/" + code)
        ).andExpect(status().is3xxRedirection())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Is.is(link.getUrl())));
    }

    @Test
    @WithMockUser
    public void whenLoadStatistic() throws Exception {
        Site site = new Site("login", "pass", "vk.com");
        Mockito.when(siteService.findByLogin("user")).thenReturn(site);
        Link link1 = new Link("url1", site, "code1");
        link1.setTotal(1);
        Link link2 = new Link("url2", site, "code2");
        link2.setTotal(2);
        Link link3 = new Link("url3", site, "code3");
        link3.setTotal(3);
        Mockito.when(linkService.findBySite(site)).thenReturn(List.of(link1, link2, link3));
        mvc.perform(
                get("/statistic")
        )
                .andExpect(jsonPath("$.[0].url", Is.is(link1.getUrl())))
                .andExpect(jsonPath("$.[0].code", Is.is(link1.getCode())))
                .andExpect(jsonPath("$.[0].total", Is.is(link1.getTotal())))

                .andExpect(jsonPath("$.[1].url", Is.is(link2.getUrl())))
                .andExpect(jsonPath("$.[1].code", Is.is(link2.getCode())))
                .andExpect(jsonPath("$.[1].total", Is.is(link2.getTotal())))

                .andExpect(jsonPath("$.[2].url", Is.is(link3.getUrl())))
                .andExpect(jsonPath("$.[2].code", Is.is(link3.getCode())))
                .andExpect(jsonPath("$.[2].total", Is.is(link3.getTotal())));
    }

}