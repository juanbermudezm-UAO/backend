package backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Revision {
    private String resultado;
    private Boolean aprobada;
    private String recomendaciones;
}