package backend.model;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("trabajosOrden")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrabajosOrdenModel {
    @Id
    private ObjectId id;
    private ObjectId ordenId;
    private ObjectId tecnicoId;
    private Servicio servicio;
    private String descripcionIntervencion;
    private String componente;
    private Integer manoObraEstimada;
    private List<RepuestosPrevistosModel> repuestosPrevistos;
    private Autorizacion autorizacion;
    private Ejecucion ejecucion;
    private Revision revision;
    private Garantia garantia;
}