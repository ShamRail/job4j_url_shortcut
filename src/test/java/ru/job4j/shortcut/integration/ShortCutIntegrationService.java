package ru.job4j.shortcut.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.job4j.shortcut.config.Profiles;
import ru.job4j.shortcut.model.Link;
import ru.job4j.shortcut.model.dto.RegistrationResultDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(Profiles.DEV)
public class ShortCutIntegrationService {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    private RegistrationResultDTO register(String site) throws Exception {
        MvcResult registrationResult = mvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "site", site
                        )))
        ).andReturn();
        return objectMapper.readValue(
                registrationResult.getResponse().getContentAsString(),
                RegistrationResultDTO.class
        );
    }

    private String login(RegistrationResultDTO registrationResultDTO) throws Exception {
        return mvc.perform(
                post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "login", registrationResultDTO.getLogin(),
                                "password", registrationResultDTO.getPassword()
                        )))
        )
                .andExpect(header().exists("Authorization"))
                .andReturn().getResponse().getHeader("Authorization");
    }

    @Test
    public void whenRegisterAndLogin() throws Exception {
        RegistrationResultDTO registrationResultDTO = register("job4j.ru");
        Assert.assertNotNull(login(registrationResultDTO));
    }

    @Test
    public void whenTryToRegisterAgain() throws Exception {
        RegistrationResultDTO first = register("job4j.ru");
        RegistrationResultDTO second = register("job4j.ru");
        Assert.assertFalse(second.isRegistration());
        Assert.assertEquals(first.getLogin(), second.getLogin());
        Assert.assertEquals(first.getPassword(), second.getPassword());
    }

    @Test
    public void whenConvert() throws Exception {
        String authorization = login(register("job4j.ru"));
        String url = "http://job4j.ru:8888/TrackStudio/staticframeset.html#138";
        Assert.assertNotNull(convert(authorization, url));
    }

    @Test
    public void whenRedirectExists() throws Exception {
        String authorization = login(register("job4j.ru"));
        String url = "http://job4j.ru:8888/TrackStudio/staticframeset.html#138";
        String code = convert(authorization, url);
        mvc.perform(
                get("/redirect/" + code)
                .header("Authorization", authorization)
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(header().exists("Location"));
    }

    @Test
    public void whenRedirectUnExists() throws Exception {
        String authorization = login(register("job4j.ru"));
        mvc.perform(
                get("/redirect/123")
                .header("Authorization", authorization)
        )
                .andExpect(status().is4xxClientError());
    }

    private String convert(String authorization, String url) throws Exception {
        String json = mvc.perform(
                post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                Map.of("url", url)
                        ))
                        .header("Authorization", authorization)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url", Is.is(url)))
                .andExpect(jsonPath("$.code").exists())
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(json, Link.class).getCode();
    }

    @Test
    public void whenLoadStatistic() throws Exception {
        String authorization = login(register("job4j.ru"));
        List<String> urls = List.of(
                "http://job4j.ru:8888/TrackStudio/staticframeset.html#2097",
                "http://job4j.ru:8888/TrackStudio/staticframeset.html#13799",
                "http://job4j.ru:8888/TrackStudio/staticframeset.html#13800"
        );
        List<String> codes = new ArrayList<>();
        for (String url : urls) {
            codes.add(convert(authorization, url));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 1; j <= (i + 1); j++) {
                mvc.perform(get("/redirect/" + codes.get(i)).header("Authorization", authorization));
            }
        }
        List<Link> links = objectMapper.readValue(
                mvc.perform(
                        get("/statistic")
                                .header("Authorization", authorization)
                ).andReturn().getResponse().getContentAsString(),
                new TypeReference<>() { }
        );
        Assert.assertEquals(1, findByCode(links, codes.get(0)).getTotal());
        Assert.assertEquals(2, findByCode(links, codes.get(1)).getTotal());
        Assert.assertEquals(3, findByCode(links, codes.get(2)).getTotal());
    }

    private Link findByCode(List<Link> links, String code) {
        return links.stream()
                .filter(l -> l.getCode().equals(code))
                .findFirst().orElse(null);
    }

}
