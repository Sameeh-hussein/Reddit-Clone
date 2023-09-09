package com.sameeh.springit.service;

import com.sameeh.springit.Domain.Link;
import com.sameeh.springit.Repository.LinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LinkService {

    @Autowired
    private LinkRepository linkRepository;

    public List<Link> findAll(){
        return linkRepository.findAll();
    }

    public Optional<Link> findById(Long id){
        return linkRepository.findById(id);
    }

    public void save(Link link){
        linkRepository.save(link);
    }

    public List<Link> findAllBYUserId(Long id) {return linkRepository.findAllByUserId(id);}

    public List<Link> findAllByUserIdOrderedByCreationDateDesc(Long id){
        Pageable pageable = PageRequest.of(0, 3);
        return linkRepository.findAllByUserIdOrderByCreationDateDesc(id, pageable);
    }

    public long count() {
        return linkRepository.count();
    }
}
