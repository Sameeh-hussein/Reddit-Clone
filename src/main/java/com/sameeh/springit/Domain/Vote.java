package com.sameeh.springit.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;
import org.springframework.stereotype.Controller;

@Entity
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Vote extends Audituble{

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private short direction;

    @ManyToOne
    @NonNull
    private Link link;
}
