package backend.model;

import org.bson.types.ObjectId;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bicicleta {
    private ObjectId bicicletaId;
    private String marca;
    private String modelo;
}