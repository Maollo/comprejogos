package maollo.comprejogos.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import maollo.comprejogos.utils.ERole;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor // Construtor sem argumentos para JPA
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, unique = true)
    private ERole name;

    public Role(ERole name) {
        this.name = name;
    }
}