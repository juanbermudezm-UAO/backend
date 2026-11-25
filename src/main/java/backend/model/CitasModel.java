package backend.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("citas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CitasModel {
    @Id
    private ObjectId id;
    private ObjectId clienteId;
    private ObjectId bicicletaId;
    private Date fechaHora;
    private String motivoVisita;
    private String estado; // solicitada | confirmada | atendida | cancelada
    private String problemasObservados;
    private List<ObjectId> serviciosInteres;
    private String observacionCambioHorario;
}