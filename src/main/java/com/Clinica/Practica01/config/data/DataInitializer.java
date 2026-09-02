package com.Clinica.Practica01.config.data;

import com.Clinica.Practica01.feature.usuario.entity.Role;
import com.Clinica.Practica01.feature.usuario.entity.User;
import com.Clinica.Practica01.feature.usuario.repository.UserRepository;
import com.Clinica.Practica01.feature.medico.entity.Doctor;
import com.Clinica.Practica01.feature.medico.entity.Speciality;
import com.Clinica.Practica01.feature.medico.repository.DoctorRepository;
import com.Clinica.Practica01.feature.medico.repository.SpecialityRepository;
import com.Clinica.Practica01.feature.paciente.entity.Patient;
import com.Clinica.Practica01.feature.paciente.repository.PatientRepository;
import com.Clinica.Practica01.feature.horario.entity.Schedule;
import com.Clinica.Practica01.feature.horario.repository.ScheduleRepository;
import com.Clinica.Practica01.feature.cita.entity.Appointment;
import com.Clinica.Practica01.feature.cita.entity.AppointmentStatus;
import com.Clinica.Practica01.feature.cita.repository.AppointmentRepository;
import com.Clinica.Practica01.feature.farmacia.entity.Order;
import com.Clinica.Practica01.feature.farmacia.entity.OrderItem;
import com.Clinica.Practica01.feature.farmacia.entity.Product;
import com.Clinica.Practica01.feature.farmacia.repository.OrderRepository;
import com.Clinica.Practica01.feature.farmacia.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final SpecialityRepository specialityRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final ScheduleRepository scheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            log.info("Iniciando la inserción de datos de prueba (Seed)...");

            // 1. Crear Usuarios base
            User adminUser = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .email("admin@clinica.com")
                    .role(Role.ROLE_ADMIN)
                    .build();

            User docUser1 = User.builder()
                    .username("medico1")
                    .password(passwordEncoder.encode("medico123"))
                    .email("medico1@clinica.com")
                    .role(Role.ROLE_MEDICO)
                    .build();

            User docUser2 = User.builder()
                    .username("medico2")
                    .password(passwordEncoder.encode("medico123"))
                    .email("medico2@clinica.com")
                    .role(Role.ROLE_MEDICO)
                    .build();

            User patUser1 = User.builder()
                    .username("paciente1")
                    .password(passwordEncoder.encode("paciente123"))
                    .email("paciente1@clinica.com")
                    .role(Role.ROLE_PACIENTE)
                    .build();

            userRepository.saveAll(List.of(adminUser, docUser1, docUser2, patUser1));

            // 2. Crear Especialidades
            Speciality card = Speciality.builder().name("Cardiología").description("Enfermedades del corazón").build();
            Speciality derm = Speciality.builder().name("Dermatología").description("Cuidado de la piel").build();
            specialityRepository.saveAll(List.of(card, derm));

            // 3. Crear Médicos
            Doctor doctor1 = Doctor.builder()
                    .firstName("Carlos")
                    .lastName("Ramirez")
                    .cmp("CMP12345")
                    .user(docUser1)
                    .speciality(card)
                    .build();

            Doctor doctor2 = Doctor.builder()
                    .firstName("Ana")
                    .lastName("Gomez")
                    .cmp("CMP54321")
                    .user(docUser2)
                    .speciality(derm)
                    .build();

            doctorRepository.saveAll(List.of(doctor1, doctor2));

            // 4. Crear Paciente
            Patient patient1 = Patient.builder()
                    .firstName("Luis")
                    .lastName("Perez")
                    .documentId("12345678")
                    .dateOfBirth(LocalDate.of(1990, 5, 20))
                    .phone("987654321")
                    .user(patUser1)
                    .build();
            patientRepository.save(patient1);

            // 5. Crear Horarios para el Médico 1
            Schedule schedule1 = Schedule.builder()
                    .doctor(doctor1)
                    .availableDate(LocalDate.now().plusDays(1))
                    .startTime(LocalTime.of(9, 0))
                    .endTime(LocalTime.of(10, 0))
                    .isBooked(true) // Reservado por la cita de abajo
                    .build();

            Schedule schedule2 = Schedule.builder()
                    .doctor(doctor1)
                    .availableDate(LocalDate.now().plusDays(1))
                    .startTime(LocalTime.of(10, 0))
                    .endTime(LocalTime.of(11, 0))
                    .isBooked(false)
                    .build();
            scheduleRepository.saveAll(List.of(schedule1, schedule2));

            // 6. Crear Cita
            Appointment appointment1 = Appointment.builder()
                    .patient(patient1)
                    .schedule(schedule1)
                    .status(AppointmentStatus.CONFIRMED)
                    .notes("Chequeo general preventivo")
                    .createdAt(LocalDateTime.now())
                    .build();
            appointmentRepository.save(appointment1);

            // 7. Crear Productos de Farmacia
            Product product1 = Product.builder().name("Paracetamol 500mg").description("Analgésico y antipirético").price(new BigDecimal("5.50")).stock(100).build();
            Product product2 = Product.builder().name("Amoxicilina 500mg").description("Antibiótico de amplio espectro").price(new BigDecimal("15.00")).stock(50).build();
            Product product3 = Product.builder().name("Ibuprofeno 400mg").description("Antiinflamatorio no esteroideo").price(new BigDecimal("8.20")).stock(80).build();
            productRepository.saveAll(List.of(product1, product2, product3));

            // 8. Crear Orden de Venta
            Order order1 = Order.builder()
                    .patient(patient1)
                    .totalAmount(new BigDecimal("20.50"))
                    .orderDate(LocalDateTime.now())
                    .items(new ArrayList<>())
                    .build();

            OrderItem item1 = OrderItem.builder().order(order1).product(product1).quantity(1).unitPrice(new BigDecimal("5.50")).build();
            OrderItem item2 = OrderItem.builder().order(order1).product(product2).quantity(1).unitPrice(new BigDecimal("15.00")).build();

            order1.getItems().add(item1);
            order1.getItems().add(item2);

            orderRepository.save(order1);

            log.info("✅ Datos de prueba insertados exitosamente en la base de datos.");
        } else {
            log.info("La base de datos ya contiene información. Omitiendo la inserción de datos de prueba.");
        }
    }
}
