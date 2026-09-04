package com.clinica.practica01.feature.appointment.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Envia notificaciones de cita a un usuario por WebSocket
 * (destino /user/queue/appointments).
 */
@Component
@RequiredArgsConstructor
public class AppointmentNotifier {

    private static final String DESTINATION = "/queue/appointments";

    private final SimpMessagingTemplate messagingTemplate;

    public void notifyUser(String username, AppointmentNotification notification) {
        if (username == null) {
            return;
        }
        messagingTemplate.convertAndSendToUser(username, DESTINATION, notification);
    }
}
