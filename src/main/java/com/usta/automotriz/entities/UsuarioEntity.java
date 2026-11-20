package com.usta.automotriz.entities;

import com.usta.automotriz.enums.Rol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
@Table(name = "usuarios")
public class UsuarioEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;

    @NotBlank
    @Size(max = 100)
    @Column(name = "nombre_completo", nullable = false,length = 100)
    private String nombreUsuario;

    @NotBlank
    @Size(max = 50)
    @Column(name = "username", nullable = false,length = 50)
    private String userNameUsuario;

    @NotBlank
    @Size(max = 60)
    @Column(name = "password_usuario", nullable = false,length = 60)
    private String passwordUsuario;

    @NotBlank
    @jakarta.validation.constraints.Email(message = "El formato del correo no es válido")
    @Size(max = 100)
    @Column(name = "correo_usuario", nullable = false, unique = true, length = 100)
    private String correoUsuario;

    @NotNull
    @Column(name = "es_activo", nullable = false)
    private Boolean esActivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol;
}
