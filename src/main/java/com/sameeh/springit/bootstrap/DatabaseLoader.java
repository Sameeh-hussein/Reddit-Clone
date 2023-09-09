package com.sameeh.springit.bootstrap;

import com.sameeh.springit.Domain.Comment;
import com.sameeh.springit.Domain.Link;
import com.sameeh.springit.Domain.Role;
import com.sameeh.springit.Domain.User;

import com.sameeh.springit.Repository.CommentRepository;
import com.sameeh.springit.Repository.LinkRepository;
import com.sameeh.springit.Repository.RoleRepository;
import com.sameeh.springit.Repository.UserRepository;
import com.sameeh.springit.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.*;

@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private LinkService linkService;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    private Map<String, User> users = new HashMap<>();

    @Override
    public void run(String... args) throws Exception {

        // adding users and their roles
        addUsersAndRoles();

        Map<String,String> links = new HashMap<>();

        links.put("Securing Spring Boot APIs and SPAs with OAuth 2.0","https://auth0.com/blog/securing-spring-boot-apis-and-spas-with-oauth2/?utm_source=reddit&utm_medium=sc&utm_campaign=springboot_spa_securing");
        links.put("Easy way to detect Device in Java Web Application using Spring Mobile - Source code to download from GitHub","https://www.opencodez.com/java/device-detection-using-spring-mobile.htm");
        links.put("Tutorial series about building microservices with SpringBoot (with Netflix OSS)","https://medium.com/@marcus.eisele/implementing-a-microservice-architecture-with-spring-boot-intro-cdb6ad16806c");
        links.put("Detailed steps to send encrypted email using Java / Spring Boot - Source code to download from GitHub","https://www.opencodez.com/java/send-encrypted-email-using-java.htm");
        links.put("Build a Secure Progressive Web App With Spring Boot and React","https://dzone.com/articles/build-a-secure-progressive-web-app-with-spring-boo");
        links.put("Building Your First Spring Boot Web Application - DZone Java","https://dzone.com/articles/building-your-first-spring-boot-web-application-ex");
        links.put("Building Microservices with Spring Boot Fat (Uber) Jar","https://jelastic.com/blog/building-microservices-with-spring-boot-fat-uber-jar/");
        links.put("Spring Cloud GCP 1.0 Released","https://cloud.google.com/blog/products/gcp/calling-java-developers-spring-cloud-gcp-1-0-is-now-generally-available");
        links.put("Simplest way to Upload and Download Files in Java with Spring Boot - Code to download from Github","https://www.opencodez.com/uncategorized/file-upload-and-download-in-java-spring-boot.htm");
        links.put("Add Social Login to Your Spring Boot 2.0 app","https://developer.okta.com/blog/2018/07/24/social-spring-boot");
        links.put("File download example using Spring REST Controller","https://www.jeejava.com/file-download-example-using-spring-rest-controller/");

        links.forEach((k, v) -> {
            User u1 = users.get("user@gmail.com");
            User u2 = users.get("super@gmail.com");
            Link link = new Link(k, v);
            if (k.startsWith("Build")) {
                link.setUser(u1);
                u1.addLink(link);
            } else {
                link.setUser(u2);
                u2.addLink(link);
            }
            linkService.save(link);

            Comment c1 = new Comment("this is great", link);
            Comment c2 = new Comment("WoW !!", link);
            Comment c3 = new Comment("OMG !!", link);

            Comment[] comments = {c1, c2, c3};
            for (Comment comment : comments) {
                if (!k.startsWith("Build")) {
                    c1.setUser(u1);
                    c2.setUser(u1);
                    c3.setUser(u1);
                    u1.addComment(c1);
                    u1.addComment(c2);
                    u1.addComment(c3);
                } else {
                    c1.setUser(u2);
                    c2.setUser(u2);
                    c3.setUser(u2);
                    u2.addComment(c1);
                    u2.addComment(c2);
                    u2.addComment(c3);
                }
                commentRepository.save(comment);
                link.addComment(comment);
            }
        });

        long linkCount = linkService.count();
        System.out.println("Number of links in the database: " + linkCount);
    }

    private void addUsersAndRoles() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String secret = "{bcrypt}" + encoder.encode("password");

        Role userRole = new Role("ROLE_USER");
        roleRepository.save(userRole);
        Role adminRole = new Role("ROLE_ADMIN");
        roleRepository.save(adminRole);

        User user = new User("user@gmail.com", secret, true, "Joe", "User", "joedirt");
        user.addRole(userRole);
        user.setConfirmPassword(secret);
        user.setRegistrationDate(new Date());
        userRepository.save(user);
        users.put("user@gmail.com", user);

        User admin = new User("admin@gmail.com", secret, true, "Joe", "Admin", "masteradmin");
        admin.setAlias("joeadmin");
        admin.setConfirmPassword(secret);
        admin.addRole(adminRole);
        admin.setRegistrationDate(new Date());
        userRepository.save(admin);
        users.put("admin@gmail.com", admin);

        User master = new User("super@gmail.com", secret, true, "Super", "User", "superduper");
        master.addRoles(new HashSet<>(Arrays.asList(userRole, adminRole)));
        master.setConfirmPassword(secret);
        master.setRegistrationDate(new Date());
        userRepository.save(master);
        users.put("super@gmail.com", master);
    }
}
