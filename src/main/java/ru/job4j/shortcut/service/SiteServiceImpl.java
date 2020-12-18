package ru.job4j.shortcut.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.model.dto.RegistrationResultDTO;
import ru.job4j.shortcut.repository.SiteRepository;

@Service
public class SiteServiceImpl implements SiteService {

    private SiteRepository siteRepository;

    private SequenceGenerator sequenceGenerator;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public SiteServiceImpl(
            SiteRepository siteRepository,
            SequenceGenerator sequenceGenerator,
            PasswordEncoder passwordEncoder) {
        this.siteRepository = siteRepository;
        this.sequenceGenerator = sequenceGenerator;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public RegistrationResultDTO register(Site site) {
        Site dbSite = siteRepository.findBySite(site.getSite());
        if (dbSite != null) {
            return new RegistrationResultDTO(
                    false, dbSite.getLogin(), dbSite.getPassword()
            );
        }
        site.setLogin(sequenceGenerator.generate(site.getSite()));
        String password = sequenceGenerator.generate();
        site.setPassword(passwordEncoder.encode(password));
        siteRepository.save(site);
        return new RegistrationResultDTO(
                true, site.getLogin(), password
        );
    }

    @Override
    public Site findByLogin(String login) {
        return siteRepository.findByLogin(login);
    }

}
