package com.clinica.practica01.feature.mapper;

import com.clinica.practica01.feature.absence.dto.DoctorAbsenceRequest;
import com.clinica.practica01.feature.absence.entity.DoctorAbsence;
import com.clinica.practica01.feature.absence.mapper.DoctorAbsenceMapper;
import com.clinica.practica01.feature.availability.dto.DoctorAvailabilityRequest;
import com.clinica.practica01.feature.availability.entity.DoctorAvailability;
import com.clinica.practica01.feature.availability.mapper.DoctorAvailabilityMapper;
import com.clinica.practica01.feature.doctor.dto.DoctorRequest;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import com.clinica.practica01.feature.doctor.mapper.DoctorMapper;
import com.clinica.practica01.feature.doctor.repository.DoctorRepository;
import com.clinica.practica01.feature.menu.dto.MenuRequest;
import com.clinica.practica01.feature.menu.entity.Menu;
import com.clinica.practica01.feature.menu.mapper.MenuMapper;
import com.clinica.practica01.feature.menu.repository.MenuRepository;
import com.clinica.practica01.feature.permission.entity.Permission;
import com.clinica.practica01.feature.permission.mapper.PermissionMapper;
import com.clinica.practica01.feature.permission.repository.PermissionRepository;
import com.clinica.practica01.feature.product.entity.Product;
import com.clinica.practica01.feature.product.repository.ProductRepository;
import com.clinica.practica01.feature.role.dto.RoleRequest;
import com.clinica.practica01.feature.role.entity.Role;
import com.clinica.practica01.feature.role.mapper.RoleMapper;
import com.clinica.practica01.feature.role.repository.RoleRepository;
import com.clinica.practica01.feature.schedule.dto.ScheduleRequest;
import com.clinica.practica01.feature.schedule.entity.Schedule;
import com.clinica.practica01.feature.schedule.mapper.ScheduleMapper;
import com.clinica.practica01.feature.speciality.dto.SpecialityRequest;
import com.clinica.practica01.feature.speciality.entity.Speciality;
import com.clinica.practica01.feature.speciality.mapper.SpecialityMapper;
import com.clinica.practica01.feature.speciality.repository.SpecialityRepository;
import com.clinica.practica01.feature.stockentry.dto.StockEntryRequest;
import com.clinica.practica01.feature.stockentry.entity.StockEntry;
import com.clinica.practica01.feature.stockentry.mapper.StockEntryMapper;
import com.clinica.practica01.feature.user.dto.UserRequest;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.mapper.UserMapper;
import com.clinica.practica01.feature.user.service.UsernameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class RemainingMappersTest {

    @Test
    void menuMapper_mapsCreateUpdateResponseAndNullableParent() {
        MenuRepository repository = mock(MenuRepository.class);
        MenuMapper mapper = new MenuMapper(repository);
        UUID parentId = UUID.randomUUID();
        Menu parent = new Menu();
        parent.setId(parentId);
        when(repository.findById(parentId)).thenReturn(Optional.of(parent));

        MenuRequest request = new MenuRequest();
        request.setLabel("Agenda"); request.setIcon("calendar"); request.setRoute("/agenda");
        request.setOrder(2); request.setRequiredPermission("READ"); request.setParentId(parentId);
        Menu entity = mapper.toEntity(request);
        assertThat(entity.getParent()).isSameAs(parent);
        assertThat(mapper.toResponse(entity).getParentId()).isEqualTo(parentId);

        request.setLabel("Citas"); request.setParentId(null);
        mapper.updateEntity(entity, request);
        assertThat(entity.getLabel()).isEqualTo("Citas");
        assertThat(entity.getParent()).isNull();
        assertThat(mapper.toResponse(entity).getParentId()).isNull();
    }

    @Test
    void userMapper_mapsRolesPasswordsAndResponse() {
        RoleRepository roles = mock(RoleRepository.class);
        PasswordEncoder encoder = mock(PasswordEncoder.class);
        UsernameGenerator usernames = mock(UsernameGenerator.class);
        UserMapper mapper = new UserMapper(roles, encoder, usernames);
        UUID roleId = UUID.randomUUID();
        Role role = Role.builder().name("ADMIN").build();
        when(roles.findById(roleId)).thenReturn(Optional.of(role));
        when(usernames.generate("Ana", "Diaz")).thenReturn("adiaz");
        when(encoder.encode(anyString())).thenAnswer(i -> "encoded:" + i.getArgument(0));
        UserRequest request = new UserRequest();
        request.setFirstName("Ana"); request.setLastName("Diaz"); request.setEmail("a@x.test");
        request.setPassword(null); request.setRoleIds(Set.of(roleId));

        User user = mapper.toEntity(request);
        assertThat(user.getUsername()).isEqualTo("adiaz");
        assertThat(user.getPassword()).isEqualTo("encoded:");
        assertThat(user.getRoles()).containsExactly(role);
        request.setFirstName("Anita"); request.setPassword("new"); request.setRoleIds(null);
        mapper.updateEntity(user, request);
        assertThat(user.getPassword()).isEqualTo("encoded:new");
        assertThat(user.getRoles()).isEmpty();
        request.setPassword(" "); mapper.updateEntity(user, request);
        assertThat(user.getPassword()).isEqualTo("encoded:new");
        assertThat(mapper.toResponse(user).getUsername()).isEqualTo("adiaz");
    }

    @Test
    void doctorMapper_mapsSpecialityUserAndNullAssociations() {
        SpecialityRepository repository = mock(SpecialityRepository.class);
        DoctorMapper mapper = new DoctorMapper(repository);
        UUID specialityId = UUID.randomUUID();
        Speciality speciality = Speciality.builder().name("Cardiology").build(); speciality.setId(specialityId);
        when(repository.findById(specialityId)).thenReturn(Optional.of(speciality));
        DoctorRequest request = new DoctorRequest(); request.setCmp("CMP1"); request.setSpecialityId(specialityId);
        Doctor doctor = mapper.toEntity(request);
        User user = new User(); user.setId(UUID.randomUUID()); user.setUsername("doc");
        user.setFirstName("Ada"); user.setLastName("Lovelace"); user.setEmail("doc@x.test"); doctor.setUser(user);
        assertThat(mapper.toResponse(doctor).getSpecialityName()).isEqualTo("Cardiology");
        request.setCmp("CMP2"); request.setSpecialityId(null); mapper.updateEntity(doctor, request);
        assertThat(doctor.getSpeciality()).isNull();
        doctor.setUser(null); assertThat(mapper.toResponse(doctor).getUserId()).isNull();
    }

    @Test
    void availabilityAndScheduleMappers_mapDoctorDetailsAndNulls() {
        DoctorRepository repository = mock(DoctorRepository.class);
        UUID doctorId = UUID.randomUUID();
        User user = new User(); user.setFirstName("Grace"); user.setLastName("Hopper");
        Doctor doctor = new Doctor(); doctor.setId(doctorId); doctor.setUser(user);
        when(repository.findById(doctorId)).thenReturn(Optional.of(doctor));

        DoctorAvailabilityMapper availabilityMapper = new DoctorAvailabilityMapper(repository);
        DoctorAvailabilityRequest ar = new DoctorAvailabilityRequest(); ar.setDoctorId(doctorId);
        ar.setDayOfWeek(DayOfWeek.MONDAY); ar.setStartTime(LocalTime.of(8, 0));
        ar.setEndTime(LocalTime.of(12, 0)); ar.setSlotDurationMinutes(30);
        DoctorAvailability availability = availabilityMapper.toEntity(ar);
        assertThat(availabilityMapper.toResponse(availability).getDoctorName()).isEqualTo("Grace Hopper");
        ar.setSlotDurationMinutes(45); availabilityMapper.updateEntity(availability, ar);
        assertThat(availability.getSlotDurationMinutes()).isEqualTo(45);
        availability.setDoctor(new Doctor()); assertThat(availabilityMapper.toResponse(availability).getDoctorName()).isNull();
        availability.setDoctor(null); assertThat(availabilityMapper.toResponse(availability).getDoctorId()).isNull();

        ScheduleMapper scheduleMapper = new ScheduleMapper(repository);
        ScheduleRequest sr = new ScheduleRequest(); sr.setDoctorId(doctorId); sr.setAvailableDate(LocalDate.of(2026, 9, 4));
        sr.setStartTime(LocalTime.of(9, 0)); sr.setEndTime(LocalTime.of(10, 0));
        Schedule schedule = scheduleMapper.toEntity(sr);
        assertThat(schedule.isBooked()).isFalse();
        assertThat(scheduleMapper.toResponse(schedule).getDoctorName()).isEqualTo("Grace Hopper");
        sr.setEndTime(LocalTime.of(11, 0)); scheduleMapper.updateEntity(schedule, sr);
        assertThat(schedule.getEndTime()).isEqualTo(LocalTime.of(11, 0));
        schedule.setDoctor(new Doctor()); assertThat(scheduleMapper.toResponse(schedule).getDoctorName()).isNull();
        schedule.setDoctor(null); assertThat(scheduleMapper.toResponse(schedule).getDoctorId()).isNull();
    }

    @Test
    void absenceSpecialityAndStockMappers_coverAssociationsAndNulls() {
        UUID doctorId = UUID.randomUUID(); Doctor doctor = new Doctor(); doctor.setId(doctorId);
        User user = new User(); user.setFirstName("John"); user.setLastName("Doe"); doctor.setUser(user);
        DoctorRepository doctors = mock(DoctorRepository.class); when(doctors.findById(doctorId)).thenReturn(Optional.of(doctor));
        DoctorAbsenceMapper absenceMapper = new DoctorAbsenceMapper(doctors);
        DoctorAbsenceRequest absenceRequest = new DoctorAbsenceRequest(); absenceRequest.setDoctorId(doctorId);
        absenceRequest.setStartDate(LocalDate.of(2026, 9, 5)); absenceRequest.setEndDate(LocalDate.of(2026, 9, 6)); absenceRequest.setReason("Travel");
        DoctorAbsence absence = absenceMapper.toEntity(absenceRequest);
        assertThat(absenceMapper.toResponse(absence).getDoctorName()).isEqualTo("John Doe");
        absenceRequest.setReason("Health"); absenceMapper.updateEntity(absence, absenceRequest);
        assertThat(absence.getReason()).isEqualTo("Health");
        absence.setDoctor(new Doctor()); assertThat(absenceMapper.toResponse(absence).getDoctorName()).isNull();
        absence.setDoctor(null); assertThat(absenceMapper.toResponse(absence).getDoctorId()).isNull();

        SpecialityRepository specialities = mock(SpecialityRepository.class); Speciality parent = Speciality.builder().name("Medicine").build();
        UUID parentId = UUID.randomUUID(); parent.setId(parentId); when(specialities.findById(parentId)).thenReturn(Optional.of(parent));
        SpecialityMapper specialityMapper = new SpecialityMapper(specialities); SpecialityRequest specialityRequest = new SpecialityRequest();
        specialityRequest.setName("Cardiology"); specialityRequest.setDescription("Heart"); specialityRequest.setParentId(parentId);
        Speciality speciality = specialityMapper.toEntity(specialityRequest);
        assertThat(specialityMapper.toResponse(speciality).getParentName()).isEqualTo("Medicine");
        specialityRequest.setParentId(null); specialityMapper.updateEntity(speciality, specialityRequest);
        assertThat(specialityMapper.toResponse(speciality).getParentId()).isNull();

        ProductRepository products = mock(ProductRepository.class); Product product = Product.builder().name("Gauze").build();
        UUID productId = UUID.randomUUID(); product.setId(productId); when(products.findById(productId)).thenReturn(Optional.of(product));
        StockEntryMapper stockMapper = new StockEntryMapper(products); StockEntryRequest stockRequest = new StockEntryRequest();
        stockRequest.setProductId(productId); stockRequest.setQuantity(5); stockRequest.setUnitCost(BigDecimal.TEN); stockRequest.setNote("Initial");
        StockEntry stock = stockMapper.toEntity(stockRequest); stockMapper.updateEntity(stock, stockRequest);
        assertThat(stockMapper.toResponse(stock).getProductName()).isEqualTo("Gauze");
        stock.setProduct(null); assertThat(stockMapper.toResponse(stock).getProductId()).isNull();
    }

    @Test
    void roleMapper_mapsPermissionsAndHandlesNullIds() {
        PermissionRepository permissions = mock(PermissionRepository.class); PermissionMapper permissionMapper = new PermissionMapper();
        RoleMapper mapper = new RoleMapper(permissions, permissionMapper); UUID permissionId = UUID.randomUUID();
        Permission permission = Permission.builder().name("READ").description("Read access").build(); permission.setId(permissionId);
        when(permissions.findById(permissionId)).thenReturn(Optional.of(permission));
        RoleRequest request = new RoleRequest(); request.setName("ADMIN"); request.setDescription("Admin"); request.setPermissionIds(Set.of(permissionId));
        Role role = mapper.toEntity(request);
        assertThat(mapper.toResponse(role).getPermissions()).hasSize(1);
        request.setPermissionIds(null); request.setDescription("Updated"); mapper.updateEntity(role, request);
        assertThat(role.getPermissions()).isEmpty();
        assertThat(role.getDescription()).isEqualTo("Updated");
    }
}
