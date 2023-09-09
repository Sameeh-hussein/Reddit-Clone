package com.sameeh.springit.Controllers;

import com.sameeh.springit.Domain.Link;
import com.sameeh.springit.Domain.Vote;
import com.sameeh.springit.Repository.LinkRepository;
import com.sameeh.springit.Repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class VoteController {

    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Secured({"ROLE_USER"})
    @GetMapping("/vote/link/{linkID}/direction/{direction}/votecount/{voteCount}")
    public int vote(@PathVariable Long linkID, @PathVariable short direction, @PathVariable int voteCount) {
        Optional<Link> optionalLink = linkRepository.findById(linkID);
        if( optionalLink.isPresent() ) {
            Link link = optionalLink.get();
            Vote vote = new Vote(direction,link);
            voteRepository.save(vote);

            int updatedVoteCount = voteCount + direction;
            link.setVoteCount(updatedVoteCount);
            linkRepository.save(link);
            return updatedVoteCount;
        }

        return voteCount;
    }
}