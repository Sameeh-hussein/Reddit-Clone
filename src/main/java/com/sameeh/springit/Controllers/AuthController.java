package com.sameeh.springit.Controllers;

import com.sameeh.springit.Domain.User;
import com.sameeh.springit.service.LinkService;
import com.sameeh.springit.service.MailService;
import com.sameeh.springit.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.Optional;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;
    @Autowired
    private LinkService linkService;
    private static final Logger logger = LoggerFactory.getLogger(LinkController.class);

    @GetMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/profile")
    public String profile(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("links", linkService.findAllBYUserId(user.getId()));
        model.addAttribute("user", user);
        model.addAttribute("SortedLinks", linkService.findAllByUserIdOrderedByCreationDateDesc(user.getId()));
        return "profile";
    }

    @GetMapping("/profile/posts")
    public String posts(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("links", linkService.findAllBYUserId(user.getId()));
        model.addAttribute("user", user);
        model.addAttribute("SortedLinks", linkService.findAllByUserIdOrderedByCreationDateDesc(user.getId()));
        return "posts";
    }
    @GetMapping("/profile/comments")
    public String comments(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        model.addAttribute("links", linkService.findAllBYUserId(user.getId()));
        model.addAttribute("user", user);
        model.addAttribute("SortedLinks", linkService.findAllByUserIdOrderedByCreationDateDesc(user.getId()));
        return "comments";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerNewUser(@Valid User user, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            logger.info("Validation error happened");
            model.addAttribute("user", user);
            model.addAttribute("validationErrors", bindingResult.getAllErrors());
            return "register";
        } else {
            user.setRegistrationDate(new Date());
            User newUser = userService.register(user);
            redirectAttributes
                    .addAttribute("id", newUser.getId())
                    .addFlashAttribute("success", true);
            return "redirect:/register";
        }
    }

    @GetMapping("/activate/{email}/{activationCode}")
    public String activate(@PathVariable String email, @PathVariable String activationCode) {
        Optional<User> user = userService.findByEmailAndActivationCode(email,activationCode);
        if( user.isPresent() ){
            User newUser = user.get();
            newUser.setEnabled(true);
            newUser.setConfirmPassword(newUser.getPassword());
            userService.save(newUser);
            userService.sendWelcomeEmail(newUser);
            return "activated";
        }
        return "redirect:/";
    }
}
