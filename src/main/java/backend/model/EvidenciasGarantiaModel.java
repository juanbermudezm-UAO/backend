package backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvidenciasGarantiaModel {
    private String nombreArchivo;
    private String descripcion;
    private String ubicacion;
}