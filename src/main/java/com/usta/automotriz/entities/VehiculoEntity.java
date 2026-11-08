package com.usta.automotriz.entities;

import com.usta.automotriz.enums.Rol;
import com.usta.automotriz.enums.TipoDocuemento;
import com.usta.automotriz.enums.TipoPlaca;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "vehiculos")
public class VehiculoEntity implements Serializable {
private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vehiculo")
    private Long idVehiculo;

    @NotBlank
    @Size(max = 7)
    @Column(name = "placa_vehiculo",nullable = false,length = 7)
    private String placaVehiculo;


    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_placa", nullable = false)
    private TipoPlaca tipoPlaca;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nombre_dueno_vehi", nullable = false,length = 50)
    private String nombreDuenoVehi;

    @NotBlank
    @Size(max = 10)
    @Column(name = "documento_dueno_vehi", nullable = false,length = 10)
    private String documentoDuenoVehi;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_documento",nullable = false)
    private TipoDocuemento  tipoDocumento;

    @NotBlank
    @Size(max = 13)
    @Column(name = "telefono_dueno",nullable = false,length = 13)
    private  String telefonoDueno;

    @NotNull
    @Column(name = "activo_vehiculo",nullable = false)
    private Boolean activoVehiculo;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "id_modelo",nullable = false)
    private ModeloEntity idModelo;
}
