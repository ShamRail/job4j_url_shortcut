package ru.job4j.shortcut.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.*;
import ru.job4j.shortcut.model.Link;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.dto.RegistrationResultDTO;
import ru.job4j.shortcut.service.LinkService;
import ru.job4j.shortcut.service.SiteService;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
public class ShortcutController {

    private final SiteService siteService;

    private final LinkService linkService;

    public ShortcutController(SiteService siteService, LinkService linkService) {
        this.siteService = siteService;
        this.linkService = linkService;
    }

    @PostMapping("/register")
    public RegistrationResultDTO register(@RequestBody Site site) {
        return siteService.register(site);
    }

    @PostMapping("/convert")
    public Link convert(@RequestBody Map<String, String> body, Principal principal) {
        Site site = siteService.findByLogin(principal.getName());
        return linkService.registerLink(new Link(body.get("url"), site));
    }

    @GetMapping("/redirect/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {
        Link link = linkService.findByCode(code);
        if (link == null) {
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND
            );
        }
        linkService.updateStatistic(link);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Location", link.getUrl());
        return new ResponseEntity<>(
                httpHeaders, HttpStatus.MOVED_PERMANENTLY
        );
    }

    @GetMapping("/statistic")
    public List<Link> statistic(Principal principal) {
        return linkService.findBySite(
                siteService.findByLogin(principal.getName())
        );
    }

}
