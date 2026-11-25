package backend.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("ordenesServicios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrdenesServiciosModel {
    @Id
    private ObjectId id;
    private ObjectId clienteId;
    private Bicicleta bicicleta;
    private ObjectId citaId;
    private Date fechaRecepcion;
    private Date fechaEstimadaEntrega;
    private ObjectId coordinadorId;
    private List<ObjectId> tecnicosAsignados;
    private String estado; // recibida | en_diagnostico | pendiente_autorizacion | en_ejecucion | lista_para_entrega | entregada | cancelada
    private Recepcion recepcion;
    private Diagnostico diagnostico;
    private Entrega entrega;
    private Valoracion valoracion;
    private List<HistorialEstadosModel> historialEstados;
}