package com.usta.automotriz.models.services;

import com.usta.automotriz.entities.ReparacionEntity;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remitente;

    public void enviarNotificacionReparacionTerminada(ReparacionEntity reparacion) {
        try {
            String correoDestino = reparacion.getIdVehiculo().getCorreoDueno();
            if (correoDestino == null || correoDestino.isBlank()) {
                return;
            }

            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setFrom(remitente);
            helper.setTo(correoDestino);
            helper.setSubject("Su vehículo (" + reparacion.getIdVehiculo().getPlacaVehiculo() + ") está listo - Taller Automotriz");

            String contenidoHtml = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; border: 1px solid #e0e0e0; border-radius: 8px; overflow: hidden;">
                    <div style="background-color: #1e293b; color: #ffffff; padding: 20px; text-align: center;">
                        <h2 style="margin: 0;">Taller Automotriz</h2>
                        <p style="margin: 5px 0 0; color: #94a3b8;">Notificación de Servicio Finalizado</p>
                    </div>
                    <div style="padding: 25px; color: #334155;">
                        <p>Hola, <strong>%s</strong>:</p>
                        <p>Le informamos que la intervención de su vehículo ha finalizado exitosamente y ya se encuentra listo para ser retirado.</p>
                        <table style="width: 100%%; border-collapse: collapse; margin: 20px 0; background-color: #f8fafc;">
                            <tr>
                                <td style="padding: 10px; border: 1px solid #e2e8f0;"><strong>Placa:</strong></td>
                                <td style="padding: 10px; border: 1px solid #e2e8f0;">%s</td>
                            </tr>
                            <tr>
                                <td style="padding: 10px; border: 1px solid #e2e8f0;"><strong>Modelo:</strong></td>
                                <td style="padding: 10px; border: 1px solid #e2e8f0;">%s %s</td>
                            </tr>
                            <tr>
                                <td style="padding: 10px; border: 1px solid #e2e8f0;"><strong>Tipo de Servicio:</strong></td>
                                <td style="padding: 10px; border: 1px solid #e2e8f0;">%s</td>
                            </tr>
                            <tr>
                                <td style="padding: 10px; border: 1px solid #e2e8f0;"><strong>Observaciones:</strong></td>
                                <td style="padding: 10px; border: 1px solid #e2e8f0;">%s</td>
                            </tr>
                        </table>
                        <p style="font-size: 13px; color: #64748b;">Gracias por confiar en nuestro equipo técnico.</p>
                    </div>
                </div>
                """.formatted(
                    reparacion.getIdVehiculo().getNombreDuenoVehi(),
                    reparacion.getIdVehiculo().getPlacaVehiculo(),
                    reparacion.getIdVehiculo().getIdModelo().getMarcaModelo(),
                    reparacion.getIdVehiculo().getIdModelo().getNombreModelo(),
                    reparacion.getTipoReparacion().name().replace("_", " "),
                    reparacion.getObservacion() != null && !reparacion.getObservacion().isBlank()
                            ? reparacion.getObservacion() : "Sin observaciones adicionales."
            );

            helper.setText(contenidoHtml, true);
            mailSender.send(mensaje);
            System.out.println("Correo enviado exitosamente a: " + correoDestino);
        } catch (Exception e) {
            System.err.println("No se pudo enviar el correo de notificación: " + e.getMessage());
        }
    }
}