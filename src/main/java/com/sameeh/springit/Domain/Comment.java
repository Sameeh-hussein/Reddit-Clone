package com.sameeh.springit.Domain;

import com.sameeh.springit.service.BeanUtil;
import jakarta.persistence.*;
import lombok.*;
import org.ocpsoft.prettytime.PrettyTime;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

//@Table(name = "Comment") // what you need to appear
@Entity
@RequiredArgsConstructor
@Getter
@Setter
public class Comment extends Audituble{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // generate id auto increase 1, 2, 3 ....
    @Column(name = "Comment ID") //what you need tp appear
    private Long id;

    private String body;

    @ManyToOne // many comment in one link
    private Link link;

    @ManyToOne
    private User user;
    public Comment(String body, Link link) {
        this.body = body;
        this.link = link;
    }

    public String getPrettyTime() {
        PrettyTime pt = BeanUtil.getBean(PrettyTime.class);
        return pt.format(convertToDateViaInstant(getCreationDate()));
    }

    private Date convertToDateViaInstant(LocalDateTime dateToConvert) {
        return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }
}
