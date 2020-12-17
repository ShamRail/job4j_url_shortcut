package ru.job4j.shortcut.service;

import ru.job4j.shortcut.model.Link;
import ru.job4j.shortcut.model.Site;

import java.util.List;

public interface LinkService {

    Link registerLink(Link link);
    Link findByCode(String code);
    int updateStatistic(Link link);
    List<Link> findBySite(Site site);

}
