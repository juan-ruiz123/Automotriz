package com.usta.automotriz.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "repuestos")
public class RepuestoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_repuesto")
    private Long idRepuesto;

    @NotBlank
    @Size(max = 50)
    @Column(name = "nombre_repuesto",nullable = false,length = 50)
    private String nombreRepuesto;

    @NotBlank
    @Column(name = "descripcion_repuesto",nullable = false)
    private String descripcionRepuesto;

    @NotNull
    @Column(name = "precio_repuesto",nullable = false)
    private Double precioRepuesto;

    @NotBlank
    @Size(max = 13)
    @Column(name = "eanRepuesto",nullable = false,length = 13)
    private String eanRepuesto;

    @NotNull
    @Column(name = "stock_repuesto",nullable = false)
    private Integer stockRepuesto;

    @NotBlank
    @Size(max = 200)
    @Column(name = "foto_repuesto",length = 200)
    private String fotoRepuesto;

    @NotNull
    @Column(name = "activo_repuesto",nullable = false)
    private Boolean activoRepuesto;
}
