package com.tcsp.digitalwrite.store.entity;

import com.tcsp.digitalwrite.shared.Constants;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.Instant;

@Data
@EqualsAndHashCode( of = {"id"})
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "session")
public class SessionEntity {

    @Id
    String id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    UserEntity user;

    @Builder.Default
    Instant createdAt = Instant.now();

    @Builder.Default
    Instant expiredAt = Instant.now().plusSeconds(Constants.EXPIRED_SECONDS);
}
