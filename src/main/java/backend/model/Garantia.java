package backend.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Garantia {
    private Integer duracion; // duración en días
    private Date fechaInicio;
    private String condiciones;
    private String exclusiones;
}