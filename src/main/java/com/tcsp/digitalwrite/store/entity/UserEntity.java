package com.tcsp.digitalwrite.store.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode( of = {"id"})
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usr")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long id;

    String token;

    Double typingSpeed;

    Double accuracy;

    Double frequencyKeystroke;

    @ManyToOne
    @JoinColumn(name = "system_id")
    SystemEntity system;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable( name = "user_role", joinColumns = @JoinColumn( name = "user_id"))
    @Enumerated(EnumType.STRING)
    Set<Role> roles;

}
