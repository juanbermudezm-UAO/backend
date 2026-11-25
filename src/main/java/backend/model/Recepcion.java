package backend.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recepcion {
    private String problemaReportado;
    private String condicionesObservadas;
    private List<String> accesoriosEntregados;
    private List<FotografiasModel> fotografias;
}