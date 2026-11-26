package backend.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Autorizacion {
    private String decision; // autorizado | rechazado
    private Date fecha;
    private String comentarios;
}