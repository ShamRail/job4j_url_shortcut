package ru.job4j.shortcut.service;

import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.dto.RegistrationResultDTO;

public interface SiteService {

    RegistrationResultDTO register(Site site);
    Site findByLogin(String login);

}
