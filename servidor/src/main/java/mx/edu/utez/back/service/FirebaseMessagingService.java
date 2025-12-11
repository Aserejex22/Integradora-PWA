package mx.edu.utez.back.service;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servicio para envío de notificaciones push FCM (Firebase Cloud Messaging)
 * Basado en implementación de Tucanes_DMI_10A
 * 
 * @author TiendaPWA Team
 */
@Service
public class FirebaseMessagingService {

    /**
     * Enviar notificación a un solo dispositivo (repartidor o admin)
     * 
     * @param fcmToken Token FCM del dispositivo
     * @param titulo   Título de la notificación
     * @param cuerpo   Mensaje de la notificación
     * @param data     Datos adicionales (opcional)
     */
    public void enviarNotificacion(String fcmToken, String titulo, String cuerpo, Map<String, String> data) {
        if (fcmToken == null || fcmToken.isEmpty()) {
            System.out.println("⚠️ No se puede enviar notificación: Token FCM vacío");
            return;
        }

        try {
            // Crear datos si no existen
            if (data == null) {
                data = new HashMap<>();
            }

            // Construir el mensaje
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()
                            .setTitle(titulo)
                            .setBody(cuerpo)
                            .build())
                    .putAllData(data)
                    // Configuración para Android
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .setNotification(AndroidNotification.builder()
                                    .setChannelId("tienda_pwa_channel")
                                    .setPriority(AndroidNotification.Priority.HIGH)
                                    .setSound("default")
                                    .build())
                            .build())
                    // Configuración para iOS/Web
                    .setWebpushConfig(WebpushConfig.builder()
                            .setNotification(WebpushNotification.builder()
                                    .setTitle(titulo)
                                    .setBody(cuerpo)
                                    .setIcon("/TiendaPWA-front/img/192.png")
                                    .build())
                            .build())
                    .build();

            // Enviar
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("✅ Notificación enviada exitosamente: " + response);

        } catch (FirebaseMessagingException e) {
            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                System.out.println("⚠️ El token FCM no es válido o el usuario ya no está registrado: " + fcmToken);
            } else if (e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT) {
                System.out.println("⚠️ Token FCM inválido: " + fcmToken);
            } else {
                System.out.println("❌ Error al enviar notificación: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al enviar notificación: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Enviar notificación a múltiples dispositivos (broadcast a todos los
     * repartidores)
     * 
     * @param tokens Lista de tokens FCM
     * @param titulo Título de la notificación
     * @param cuerpo Mensaje de la notificación
     * @param data   Datos adicionales (opcional)
     */
    public void enviarNotificacionMultiple(List<String> tokens, String titulo, String cuerpo,
            Map<String, String> data) {
        if (tokens == null || tokens.isEmpty()) {
            System.out.println("⚠️ No hay tokens para enviar notificaciones");
            return;
        }

        // Filtrar tokens vacíos
        tokens.removeIf(token -> token == null || token.isEmpty());

        if (tokens.isEmpty()) {
            System.out.println("⚠️ Todos los tokens estaban vacíos");
            return;
        }

        try {
            if (data == null) {
                data = new HashMap<>();
            }

            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(tokens)
                    .setNotification(Notification.builder()
                            .setTitle(titulo)
                            .setBody(cuerpo)
                            .build())
                    .putAllData(data)
                    .setAndroidConfig(AndroidConfig.builder()
                            .setPriority(AndroidConfig.Priority.HIGH)
                            .setNotification(AndroidNotification.builder()
                                    .setChannelId("tienda_pwa_channel")
                                    .setPriority(AndroidNotification.Priority.HIGH)
                                    .setSound("default")
                                    .build())
                            .build())
                    .build();

            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);
            System.out.println("📤 Notificaciones enviadas: " + response.getSuccessCount() + " exitosas, "
                    + response.getFailureCount() + " fallidas de " + tokens.size() + " total");

            // Mostrar tokens fallidos
            if (response.getFailureCount() > 0) {
                List<SendResponse> responses = response.getResponses();
                for (int i = 0; i < responses.size(); i++) {
                    if (!responses.get(i).isSuccessful()) {
                        FirebaseMessagingException exception = (FirebaseMessagingException) responses.get(i)
                                .getException();
                        if (exception != null && exception.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED) {
                            System.out.println("⚠️ Token no válido: "
                                    + tokens.get(i).substring(0, Math.min(20, tokens.get(i).length())) + "...");
                        } else {
                            System.err.println("❌ Error en token [" + i + "]: "
                                    + (exception != null ? exception.getMessage() : "Desconocido"));
                        }
                    }
                }
            }
        } catch (FirebaseMessagingException e) {
            System.err.println("❌ Error al enviar notificaciones múltiples: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Enviar notificación simple (sin datos extra)
     */
    public void enviarNotificacionSimple(String fcmToken, String titulo, String cuerpo) {
        enviarNotificacion(fcmToken, titulo, cuerpo, null);
    }
}
