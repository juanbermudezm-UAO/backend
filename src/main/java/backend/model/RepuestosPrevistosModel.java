package backend.model;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepuestosPrevistosModel {
    private ObjectId repuestoId;
    private String nombre;
    private String especificaciones;
    private Integer cantidad;
    private Integer precioUnitarioEstimado;
}