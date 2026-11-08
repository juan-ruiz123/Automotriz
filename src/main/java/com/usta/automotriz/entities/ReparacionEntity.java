package com.usta.automotriz.entities;

import com.usta.automotriz.enums.EstadoReparacion;
import com.usta.automotriz.enums.Rol;
import com.usta.automotriz.enums.TipoReparacion;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "reparaciones")
public class ReparacionEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReparacion;

    @NotBlank
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "fecha_reparacion",nullable = false)
    private LocalDateTime fechaReparacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_reparacion", nullable = false)
    private TipoReparacion tipoReparacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_reparacion", nullable = false)
    private EstadoReparacion estadoReparacion;

    @Column(name = "observacion", nullable = true)
    private String observacion;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "id_vehiculo",nullable = false)
    private VehiculoEntity idVehiculo;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "id_usuario",nullable = false)
    private UsuarioEntity idUsuario;

}
