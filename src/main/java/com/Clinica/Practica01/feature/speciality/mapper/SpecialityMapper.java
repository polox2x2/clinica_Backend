package com.Clinica.Practica01.feature.speciality.mapper;

import com.Clinica.Practica01.core.mapper.BaseMapper;
import com.Clinica.Practica01.feature.speciality.dto.SpecialityRequest;
import com.Clinica.Practica01.feature.speciality.dto.SpecialityResponse;
import com.Clinica.Practica01.feature.speciality.entity.Speciality;
import com.Clinica.Practica01.feature.speciality.repository.SpecialityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SpecialityMapper implements BaseMapper<Speciality, SpecialityRequest, SpecialityResponse> {

    private final SpecialityRepository specialityRepository;

    @Override
    public Speciality toEntity(SpecialityRequest r) {
        Speciality s = Speciality.builder()
                .name(r.getName())
                .description(r.getDescription())
                .build();
        applyParent(s, r);
        return s;
    }

    @Override
    public void updateEntity(Speciality e, SpecialityRequest r) {
        e.setName(r.getName());
        e.setDescription(r.getDescription());
        applyParent(e, r);
    }

    @Override
    public SpecialityResponse toResponse(Speciality e) {
        SpecialityResponse res = new SpecialityResponse();
        res.setName(e.getName());
        res.setDescription(e.getDescription());
        if (e.getParent() != null) {
            res.setParentId(e.getParent().getId());
            res.setParentName(e.getParent().getName());
        }
        return res;
    }

    private void applyParent(Speciality s, SpecialityRequest r) {
        s.setParent(r.getParentId() == null ? null
                : specialityRepository.findById(r.getParentId()).orElse(null));
    }
}
