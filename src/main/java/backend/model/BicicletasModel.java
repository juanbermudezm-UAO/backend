package backend.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document("bicicletas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BicicletasModel {
    @Id
    private ObjectId id;
    private ObjectId propietarioId;
    private String marca;
    private String modelo;
    private String tipo; // urbana | ruta | montaña | electrica
    private String color;
    private String numeroSerie;
    private CaracteristicasTecnicas caracteristicasTecnicas;
}