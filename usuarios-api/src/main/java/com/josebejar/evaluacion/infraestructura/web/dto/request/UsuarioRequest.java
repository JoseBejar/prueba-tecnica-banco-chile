package com.josebejar.evaluacion.infraestructura.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UsuarioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @JsonProperty("nombre")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "El correo no tiene un formato válido")
    @JsonProperty("correo")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    @JsonProperty("contraseña")
    private String contraseña;

    @Valid
    @NotEmpty(message = "Debe incluir al menos un teléfono")
    @JsonProperty("telefonos")
    private List<TelefonoRequest> telefonos;

    public UsuarioRequest() {
    }
}
