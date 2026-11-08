package com.usta.automotriz.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@Table(name = "modelos")
public class ModeloEntity implements Serializable {
private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_modelo")
    private Long idModelo;

    @NotBlank
    @Size(max = 30)
    @Column(name = "nombre_modelo",nullable = false,length = 30)
    private String nombreModelo;

    @NotBlank
    @Size(max = 30)
    @Column(name = "marca_modelo",nullable = false,length = 30)
    private String marcaModelo;

    @NotNull
    @Column(name = "activo_modelo",nullable = false)
    private Boolean activoModelo;

    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinTable(name = "modelos_repuestos", joinColumns = @JoinColumn(name = "id_modelo",referencedColumnName = "id_modelo"),inverseJoinColumns =
    @JoinColumn(name = "id_repuesto",referencedColumnName = "id_repuesto"))
    private Collection<RepuestoEntity> repuestos = new ArrayList<>();



}
