package backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Valoracion {
    private Integer calificacion;
    private String comentarios;
    private String sugerencias;
}