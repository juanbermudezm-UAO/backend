package backend.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("servicios")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiciosModel {
    @Id
    private ObjectId id;
    private String nombre;
    private String categoria; // mantenimiento preventivo | frenos | transmision | ruedas | suspension | componentes electricos
    private String descripcion;
    private Double duracionEstimadaMin;
    private Integer precioReferencia;
    private List<TareasHabitualesModel> tareasHabituales;
    private String recomendacionesGenerales;
}