package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.job4j.shortcut.model.Site;

@Repository
public interface SiteRepository extends JpaRepository<Site, Integer> {

    Site findByLogin(String login);

    Site findBySite(String site);

}
