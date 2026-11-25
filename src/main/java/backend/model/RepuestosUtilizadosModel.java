package backend.model;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RepuestosUtilizadosModel {
    private ObjectId repuestoId;
    private Integer cantidad;
    private Integer precioUnitarioAplicado;
}