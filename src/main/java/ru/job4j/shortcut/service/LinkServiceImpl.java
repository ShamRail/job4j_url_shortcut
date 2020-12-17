package ru.job4j.shortcut.service;

import org.springframework.stereotype.Service;
import ru.job4j.shortcut.model.Link;
import ru.job4j.shortcut.model.Site;
import ru.job4j.shortcut.repository.LinkRepository;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {

    private final SequenceGenerator sequenceGenerator;

    private final LinkRepository linkRepository;

    public LinkServiceImpl(SequenceGenerator sequenceGenerator, LinkRepository linkRepository) {
        this.sequenceGenerator = sequenceGenerator;
        this.linkRepository = linkRepository;
    }

    @Override
    public Link registerLink(Link link) {
        Link dbLink = linkRepository.findByUrl(link.getUrl());
        if (dbLink != null) {
            return dbLink;
        }
        link.setCode(sequenceGenerator.generate());
        linkRepository.save(link);
        return link;
    }

    @Override
    public Link findByCode(String code) {
        return linkRepository.findByCode(code);
    }

    @Override
    public int updateStatistic(Link link) {
        return linkRepository.increaseTotal(link);
    }

    @Override
    public List<Link> findBySite(Site site) {
        return linkRepository.findBySite(site);
    }
}
