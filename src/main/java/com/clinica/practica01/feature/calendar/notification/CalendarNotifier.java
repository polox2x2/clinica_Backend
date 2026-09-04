package com.clinica.practica01.feature.calendar.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

/**
 * Emite cambios del calendario de un medico por WebSocket (broadcast).
 * Cualquiera viendo /topic/calendar/doctor/{id} recibe el aviso y refresca.
 */
@Component
@RequiredArgsConstructor
public class CalendarNotifier {

    private final SimpMessagingTemplate messagingTemplate;

    public void calendarChanged(UUID doctorId, String reason) {
        if (doctorId == null) {
            return;
        }
        Object payload = Map.of(
                "type", "CALENDAR_UPDATED",
                "doctorId", doctorId.toString(),
                "reason", reason);
        messagingTemplate.convertAndSend("/topic/calendar/doctor/" + doctorId, payload);
    }
}
