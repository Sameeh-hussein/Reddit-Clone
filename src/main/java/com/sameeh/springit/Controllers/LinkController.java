package com.sameeh.springit.Controllers;

import com.sameeh.springit.Domain.Comment;
import com.sameeh.springit.Domain.Link;
import com.sameeh.springit.Domain.User;
import com.sameeh.springit.Repository.CommentRepository;
import com.sameeh.springit.service.LinkService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class LinkController {

    private final Logger logger = LoggerFactory.getLogger(LinkController.class);

    @Autowired
    private LinkService linkService;

    @Autowired
    private CommentRepository commentRepository;

    @GetMapping("/")
    public String list(Model model) {
        model.addAttribute("links", linkService.findAll());
        return "index";
    }

    @GetMapping("/link/{id}")
    public String read(@PathVariable Long id, Model model) {
        Optional<Link> link = linkService.findById(id);
        if (link.isPresent()) {
            Comment comment = new Comment();
            comment.setLink(link.get());
            model.addAttribute("link", link.get());
            model.addAttribute("comment", comment);
            model.addAttribute("success", model.containsAttribute("success"));
            return "view";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/link/submit")
    public String newLinkForm(Model model) {
        model.addAttribute("link",new Link());
        return "submit";
    }

    @PostMapping("/link/submit")
    public String createLink(@Valid Link link, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if(bindingResult.hasErrors()){
            logger.info("Validation errors were found while submitting a link.");
            model.addAttribute("link", link);
            return "submit";
        }else{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            link.setUser(user);
            user.addLink(link);
            linkService.save(link);
            logger.info("New link was saved successfully.");
            redirectAttributes.
                    addAttribute("id", link.getId()).
                    addFlashAttribute("success", true);
            System.out.println(link.getId());
            return "redirect:/link/{id}";
        }
    }

    @Secured("ROLE_USER")
    @PostMapping("/link/comments")
    public String addComment(@Valid Comment comment, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if( bindingResult.hasErrors() ) {
            logger.info("Something went wrong.");
        } else {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User user = (User) authentication.getPrincipal();
            comment.setUser(user);
            user.addComment(comment);
            logger.info("New Comment Saved!");
            commentRepository.save(comment);
        }
        return "redirect:/link/" + comment.getLink().getId();
    }
}
