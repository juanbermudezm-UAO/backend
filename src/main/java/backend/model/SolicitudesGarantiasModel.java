package backend.model;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("solicitudesGarantias")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudesGarantiasModel {
    @Id
    private ObjectId id;
    private ObjectId trabajoId;
    private String descripcion;
    private Date fecha;
    private List<EvidenciasGarantiaModel> evidencias;
    private String estado; // registrada | en_revision | cerrada
    private Boolean procede;
    private String solucionAplicada;
}