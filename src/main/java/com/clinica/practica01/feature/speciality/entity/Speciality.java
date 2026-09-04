package com.clinica.practica01.feature.speciality.entity;

import com.clinica.practica01.core.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "specialities")
public class Speciality extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private String description;

    // Subespecialidad: especialidad padre (null = raiz)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Speciality parent;

    @Builder.Default
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private List<Speciality> children = new ArrayList<>();
}
