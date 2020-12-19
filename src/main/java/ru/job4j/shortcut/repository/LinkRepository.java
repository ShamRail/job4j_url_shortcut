package ru.job4j.shortcut.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.shortcut.model.Link;
import ru.job4j.shortcut.model.Site;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {

    List<Link> findBySite(Site site);

    Link findByCode(String code);

    Link findByUrl(String url);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            value = "update Link l set l.total = :#{#link.total} + 1 where l.id = :#{#link.id}"
    )
    int increaseTotal(Link link);

}
