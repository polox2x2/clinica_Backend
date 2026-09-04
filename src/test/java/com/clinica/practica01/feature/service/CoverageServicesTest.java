package com.clinica.practica01.feature.service;

import com.clinica.practica01.core.exception.BusinessException;
import com.clinica.practica01.core.exception.ResourceNotFoundException;
import com.clinica.practica01.feature.absence.entity.DoctorAbsence;
import com.clinica.practica01.feature.absence.repository.DoctorAbsenceRepository;
import com.clinica.practica01.feature.appointment.entity.Appointment;
import com.clinica.practica01.feature.appointment.entity.AppointmentStatus;
import com.clinica.practica01.feature.appointment.repository.AppointmentRepository;
import com.clinica.practica01.feature.availability.entity.DoctorAvailability;
import com.clinica.practica01.feature.availability.repository.DoctorAvailabilityRepository;
import com.clinica.practica01.feature.calendar.notification.CalendarNotifier;
import com.clinica.practica01.feature.calendar.service.CalendarServiceImpl;
import com.clinica.practica01.feature.doctor.dto.DoctorRequest;
import com.clinica.practica01.feature.doctor.entity.Doctor;
import com.clinica.practica01.feature.doctor.mapper.DoctorMapper;
import com.clinica.practica01.feature.doctor.repository.DoctorRepository;
import com.clinica.practica01.feature.doctor.service.DoctorServiceImpl;
import com.clinica.practica01.feature.menu.entity.Menu;
import com.clinica.practica01.feature.menu.mapper.MenuMapper;
import com.clinica.practica01.feature.menu.repository.MenuRepository;
import com.clinica.practica01.feature.menu.service.MenuServiceImpl;
import com.clinica.practica01.feature.order.dto.OrderItemRequest;
import com.clinica.practica01.feature.order.dto.OrderRequest;
import com.clinica.practica01.feature.order.entity.Order;
import com.clinica.practica01.feature.order.mapper.OrderMapper;
import com.clinica.practica01.feature.order.repository.OrderRepository;
import com.clinica.practica01.feature.order.service.OrderServiceImpl;
import com.clinica.practica01.feature.patient.dto.PatientRequest;
import com.clinica.practica01.feature.patient.dto.SelfPatientRequest;
import com.clinica.practica01.feature.patient.entity.Patient;
import com.clinica.practica01.feature.patient.mapper.PatientMapper;
import com.clinica.practica01.feature.patient.repository.PatientRepository;
import com.clinica.practica01.feature.patient.service.PatientServiceImpl;
import com.clinica.practica01.feature.product.entity.Product;
import com.clinica.practica01.feature.product.repository.ProductRepository;
import com.clinica.practica01.feature.schedule.dto.GenerateScheduleRequest;
import com.clinica.practica01.feature.schedule.entity.Schedule;
import com.clinica.practica01.feature.schedule.mapper.ScheduleMapper;
import com.clinica.practica01.feature.schedule.repository.ScheduleRepository;
import com.clinica.practica01.feature.schedule.service.ScheduleServiceImpl;
import com.clinica.practica01.feature.user.entity.User;
import com.clinica.practica01.feature.user.repository.UserRepository;
import com.clinica.practica01.feature.user.service.AccountProvisioner;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CoverageServicesTest {

    @Test
    void calendar_supportsDayWeekMonthFreeAndOccupiedSlots() {
        ScheduleRepository schedules = mock(ScheduleRepository.class);
        AppointmentRepository appointments = mock(AppointmentRepository.class);
        CalendarServiceImpl service = new CalendarServiceImpl(schedules, appointments);
        UUID doctorId = UUID.randomUUID(); LocalDate date = LocalDate.of(2026, 9, 4);
        Schedule slot = Schedule.builder().availableDate(date).startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(9, 30)).booked(true).build(); slot.setId(UUID.randomUUID());
        Patient patient = new Patient(); patient.setId(UUID.randomUUID());
        User user = new User(); user.setFirstName("Ana"); user.setLastName("Diaz"); patient.setUser(user);
        Appointment appointment = Appointment.builder().schedule(slot).patient(patient).status(AppointmentStatus.CONFIRMED).build();
        appointment.setId(UUID.randomUUID());
        when(schedules.findByDoctorIdAndAvailableDateBetweenAndActiveTrue(eq(doctorId), any(), any())).thenReturn(List.of(slot));
        when(appointments.findByScheduleIdInAndStatusInAndActiveTrue(anyList(), anyList())).thenReturn(List.of(appointment));

        assertThat(service.getDoctorCalendar(doctorId, null, date).getFirst().getPatientName()).isEqualTo("Ana Diaz");
        service.getDoctorCalendar(doctorId, "week", date); service.getDoctorCalendar(doctorId, "month", date);
        verify(schedules).findByDoctorIdAndAvailableDateBetweenAndActiveTrue(doctorId, date.with(DayOfWeek.MONDAY), date.with(DayOfWeek.SUNDAY));
        verify(schedules).findByDoctorIdAndAvailableDateBetweenAndActiveTrue(doctorId, date.withDayOfMonth(1), date.withDayOfMonth(30));

        Schedule free = Schedule.builder().availableDate(date).booked(false).build(); free.setId(UUID.randomUUID());
        when(schedules.findByDoctorIdAndAvailableDateAndActiveTrue(doctorId, date)).thenReturn(List.of(slot, free));
        when(schedules.findByAvailableDateAndBookedFalseAndActiveTrue(date)).thenReturn(List.of(free));
        when(appointments.findByScheduleIdInAndStatusInAndActiveTrue(anyList(), anyList())).thenReturn(List.of());
        assertThat(service.getFreeSlots(date, doctorId)).extracting("status").containsExactly("FREE");
        assertThat(service.getFreeSlots(date, null)).hasSize(1);
        assertThat(service.getDoctorCalendar(doctorId, "day", date).getFirst().getStatus()).isEqualTo("FREE");
        assertThat(service.getDoctorCalendar(doctorId, "day", date)).isNotEmpty();
        when(schedules.findByDoctorIdAndAvailableDateBetweenAndActiveTrue(any(), any(), any())).thenReturn(List.of());
        assertThat(service.getDoctorCalendar(doctorId, "day", date)).isEmpty();
    }

    @Test
    void calendarToday_keepsOnlyAppointmentsAndHandlesPatientWithoutUser() {
        ScheduleRepository schedules = mock(ScheduleRepository.class); AppointmentRepository appointments = mock(AppointmentRepository.class);
        CalendarServiceImpl service = new CalendarServiceImpl(schedules, appointments); UUID doctorId = UUID.randomUUID();
        Schedule occupied = Schedule.builder().booked(true).build(); occupied.setId(UUID.randomUUID());
        Schedule free = Schedule.builder().booked(false).build(); free.setId(UUID.randomUUID());
        Patient patient = new Patient(); Appointment appt = Appointment.builder().schedule(occupied).patient(patient).status(AppointmentStatus.PENDING).build(); appt.setId(UUID.randomUUID());
        when(schedules.findByDoctorIdAndAvailableDateAndActiveTrue(eq(doctorId), any())).thenReturn(List.of(occupied, free));
        when(appointments.findByScheduleIdInAndStatusInAndActiveTrue(anyList(), anyList())).thenReturn(List.of(appt));
        assertThat(service.getToday(doctorId)).hasSize(1);
        assertThat(service.getToday(doctorId).getFirst().getPatientName()).isNull();
    }

    @Test
    void scheduleGeneration_createsSlotsSkipsDuplicatesAndAbsences() {
        ScheduleRepository schedules = mock(ScheduleRepository.class); DoctorRepository doctors = mock(DoctorRepository.class);
        DoctorAvailabilityRepository availability = mock(DoctorAvailabilityRepository.class); DoctorAbsenceRepository absences = mock(DoctorAbsenceRepository.class);
        CalendarNotifier notifier = mock(CalendarNotifier.class); ScheduleMapper mapper = new ScheduleMapper(doctors);
        ScheduleServiceImpl service = new ScheduleServiceImpl(schedules, mapper, doctors, availability, absences, notifier);
        UUID doctorId = UUID.randomUUID(); Doctor doctor = new Doctor(); doctor.setId(doctorId);
        LocalDate monday = LocalDate.of(2026, 9, 7);
        GenerateScheduleRequest request = new GenerateScheduleRequest(); request.setDoctorId(doctorId); request.setFromDate(monday); request.setToDate(monday.plusDays(2));
        DoctorAvailability av = DoctorAvailability.builder().dayOfWeek(DayOfWeek.MONDAY).startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(9, 30)).slotDurationMinutes(30).build();
        when(doctors.findById(doctorId)).thenReturn(Optional.of(doctor)); when(availability.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(List.of(av));
        when(absences.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(List.of());
        when(schedules.existsByDoctorIdAndAvailableDateAndStartTimeAndActiveTrue(eq(doctorId), eq(monday), any())).thenReturn(false, true, false);
        when(schedules.save(any())).thenAnswer(i -> i.getArgument(0));
        assertThat(service.generate(request).getGenerated()).isEqualTo(2);
        verify(notifier).calendarChanged(doctorId, "SLOTS_GENERATED");

        DoctorAbsence absence = DoctorAbsence.builder().startDate(monday).endDate(monday).build();
        when(absences.findByDoctorIdAndActiveTrue(doctorId)).thenReturn(List.of(absence));
        assertThat(service.generate(request).getGenerated()).isZero();
        when(doctors.findById(doctorId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.generate(request)).isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void menuTree_filtersPermissionsButRetainsParentOfVisibleChild() {
        MenuRepository repository = mock(MenuRepository.class); MenuServiceImpl service = new MenuServiceImpl(repository, new MenuMapper(repository));
        Menu parent = Menu.builder().label("Admin").displayOrder(1).requiredPermission("ADMIN").build(); parent.setId(UUID.randomUUID());
        Menu child = Menu.builder().label("Reports").displayOrder(2).requiredPermission("REPORT").parent(parent).build(); child.setId(UUID.randomUUID());
        Menu hidden = Menu.builder().label("Secret").displayOrder(3).requiredPermission("SECRET").build(); hidden.setId(UUID.randomUUID());
        when(repository.findByActiveTrueOrderByDisplayOrderAsc()).thenReturn(List.of(parent, child, hidden));
        var tree = service.getTree(Set.of("REPORT"));
        assertThat(tree).hasSize(1); assertThat(tree.getFirst().getChildren()).hasSize(1);
        assertThat(service.getTree(Set.of())).isEmpty();
    }

    @Test
    void patientAdminAndSelfService_coverHappyAndErrorPaths() {
        PatientRepository patients = mock(PatientRepository.class); UserRepository users = mock(UserRepository.class);
        AccountProvisioner provisioner = mock(AccountProvisioner.class); PatientMapper mapper = new PatientMapper();
        PatientServiceImpl service = new PatientServiceImpl(patients, mapper, users, provisioner);
        when(patients.save(any())).thenAnswer(i -> i.getArgument(0));
        User user = new User(); user.setId(UUID.randomUUID()); user.setUsername("ana"); when(provisioner.create(any(), any(), any(), any(), any())).thenReturn(user);
        PatientRequest request = new PatientRequest(); request.setFirstName("Ana"); request.setLastName("Diaz"); request.setEmail("a@x.test"); request.setPassword("pw"); request.setDocumentId("123");
        assertThat(service.create(request).getUsername()).isEqualTo("ana");

        Patient patient = Patient.builder().documentId("123").user(user).build(); patient.setActive(true); UUID id = UUID.randomUUID(); patient.setId(id);
        when(patients.findById(id)).thenReturn(Optional.of(patient));
        request.setDocumentId("456"); assertThat(service.update(id, request).getDocumentId()).isEqualTo("456");
        verify(provisioner).updateProfile(user, "Ana", "Diaz", "a@x.test");
        patient.setUser(null); service.update(id, request);

        when(users.findByUsername("ana")).thenReturn(Optional.of(user)); when(patients.findByUserId(user.getId())).thenReturn(Optional.of(patient));
        assertThat(service.getMe("ana").getDocumentId()).isEqualTo("456");
        SelfPatientRequest self = new SelfPatientRequest(); self.setDocumentId("789"); self.setPhone("999"); self.setDateOfBirth(LocalDate.of(1990, 1, 1));
        when(patients.findByUserId(user.getId())).thenReturn(Optional.empty()); assertThat(service.createMe("ana", self).getDocumentId()).isEqualTo("789");
        when(patients.findByUserId(user.getId())).thenReturn(Optional.of(patient)); assertThat(service.updateMe("ana", self).getPhone()).isEqualTo("999");
        assertThatThrownBy(() -> service.createMe("ana", self)).isInstanceOf(BusinessException.class);
        when(users.findByUsername("missing")).thenReturn(Optional.empty()); assertThatThrownBy(() -> service.getMe("missing")).isInstanceOf(ResourceNotFoundException.class);
        when(patients.findByUserId(user.getId())).thenReturn(Optional.empty()); assertThatThrownBy(() -> service.getMe("ana")).isInstanceOf(BusinessException.class);
    }

    @Test
    void doctorCreateAndUpdate_coverUserAndNoUser() {
        DoctorRepository repository = mock(DoctorRepository.class); DoctorMapper mapper = mock(DoctorMapper.class); AccountProvisioner provisioner = mock(AccountProvisioner.class); UserRepository users = mock(UserRepository.class);
        DoctorServiceImpl service = new DoctorServiceImpl(repository, mapper, provisioner, users); DoctorRequest request = new DoctorRequest();
        request.setFirstName("Doc"); request.setLastName("Tor"); request.setEmail("d@x.test"); request.setPassword("pw");
        User user = new User(); Doctor doctor = new Doctor(); when(provisioner.create(any(), any(), any(), any(), eq("Medico"))).thenReturn(user);
        when(mapper.toEntity(request)).thenReturn(doctor); when(repository.save(any())).thenAnswer(i -> i.getArgument(0));
        service.create(request); assertThat(doctor.getUser()).isSameAs(user); verify(mapper).toResponseWithBase(doctor);
        UUID id = UUID.randomUUID(); doctor.setActive(true); when(repository.findById(id)).thenReturn(Optional.of(doctor)); service.update(id, request);
        verify(provisioner).updateProfile(user, "Doc", "Tor", "d@x.test");
        doctor.setUser(null); service.update(id, request);
    }

    @Test
    void orderCreation_calculatesTotalAndUpdatesStock() {
        OrderRepository orders = mock(OrderRepository.class); ProductRepository products = mock(ProductRepository.class);
        PatientRepository patients = mock(PatientRepository.class); OrderServiceImpl service = new OrderServiceImpl(orders, new OrderMapper(), products, patients);
        UUID productId = UUID.randomUUID(); UUID patientId = UUID.randomUUID(); Patient patient = new Patient();
        Product product = Product.builder().name("Medicine").price(new BigDecimal("7.50")).stock(10).build(); product.setActive(true);
        when(products.findById(productId)).thenReturn(Optional.of(product)); when(patients.findById(patientId)).thenReturn(Optional.of(patient));
        when(orders.save(any())).thenAnswer(i -> i.getArgument(0));
        OrderItemRequest item = new OrderItemRequest(); item.setProductId(productId); item.setQuantity(3);
        OrderRequest request = new OrderRequest(); request.setPatientId(patientId); request.setItems(List.of(item));
        assertThat(service.create(request).getTotal()).isEqualByComparingTo("22.50");
        assertThat(product.getStock()).isEqualTo(7); verify(products).save(product);

        product.setStock(1);
        assertThatThrownBy(() -> service.create(request)).isInstanceOf(BusinessException.class);
        when(products.findById(productId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.create(request)).isInstanceOf(ResourceNotFoundException.class);

        Product inactive = Product.builder().name("Old").price(BigDecimal.ONE).stock(10).build(); inactive.setActive(false);
        when(products.findById(productId)).thenReturn(Optional.of(inactive));
        assertThatThrownBy(() -> service.create(request)).isInstanceOf(ResourceNotFoundException.class);

        Product active = Product.builder().name("Medicine").price(BigDecimal.ONE).stock(10).build(); active.setActive(true);
        when(products.findById(productId)).thenReturn(Optional.of(active)); request.setPatientId(null);
        service.create(request); verify(orders, atLeastOnce()).save(any(Order.class));
    }
}
