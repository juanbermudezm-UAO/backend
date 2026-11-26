package backend.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.PartialIndexFilter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

@Component
public class MongoIndexesConfig implements CommandLineRunner {

        @Autowired
        MongoTemplate mongoTemplate;

        @Override
        public void run(String... args) {
                mongoTemplate.indexOps("usuarios").createIndex(
                                new Index().on("documentoIdentificacion", Direction.ASC)
                                                .unique()
                                                .named("uk_usuarios_documentoIdentificacion"));

                mongoTemplate.indexOps("usuarios").createIndex(
                                new Index().on("correo", Direction.ASC)
                                                .unique()
                                                .named("uk_usuarios_correo"));

                mongoTemplate.indexOps("bicicletas").createIndex(
                                new Index().on("numeroSerie", Direction.ASC)
                                                .unique()
                                                .partial(PartialIndexFilter.of(Criteria.where("numeroSerie").type(2)))
                                                .named("uk_bicicletas_numeroSerie"));

                mongoTemplate.indexOps("citas").createIndex(
                                new Index().on("bicicletaId", Direction.ASC)
                                                .on("fechaHora", Direction.ASC)
                                                .unique()
                                                .partial(PartialIndexFilter.of(Criteria.where("estado")
                                                                .in(List.of("solicitada", "confirmada"))))
                                                .named("uk_citas_activas_bicicleta_fechaHora"));

                mongoTemplate.indexOps("ordenesServicios").createIndex(
                                new Index().on("bicicleta.bicicletaId", Direction.ASC)
                                                .unique()
                                                .partial(PartialIndexFilter.of(Criteria.where("estado").in(List.of(
                                                                "recibida",
                                                                "en_diagnostico",
                                                                "pendiente_autorizacion",
                                                                "en_ejecucion",
                                                                "lista_para_entrega"))))
                                                .named("uk_ordenes_activas_por_bicicleta"));
        }
}