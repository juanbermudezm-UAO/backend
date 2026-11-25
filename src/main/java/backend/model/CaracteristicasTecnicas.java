package backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaracteristicasTecnicas {
    private String tamanoMarco;
    private String material;
    private String tamanoRueda;
    private String tipoFrenos;
    private String transmision;
    private Suspension suspension;
    private Bateria bateria;
    private Motor motor;
}