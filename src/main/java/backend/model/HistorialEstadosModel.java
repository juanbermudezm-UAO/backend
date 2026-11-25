package backend.model;

import java.util.Date;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistorialEstadosModel {
    private String estado; // recibida | en_diagnostico | pendiente_autorizacion | en_ejecucion | lista_para_entrega | entregada | cancelada
    private Date fecha;
    private ObjectId usuarioId;
    private String observacion;
}