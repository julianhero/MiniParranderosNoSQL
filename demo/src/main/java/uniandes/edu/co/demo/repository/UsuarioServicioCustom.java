package uniandes.edu.co.demo.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioServicioCustom {
    private final MongoTemplate mongoTemplate;

    public UsuarioServicioCustom(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public List<Document> obtenerTop20Conductores() {

        List<Document> pipeline = List.of(

        // Agrupar por conductor_id y contar servicios
        new Document("$group", new Document()
            .append("_id", "$conductor_id")
            .append("totalServicios", new Document("$sum", 1))
        ),

        // Ordenar desc
        new Document("$sort", new Document()
            .append("totalServicios", -1)
        ),

        // Limitar a 20
        new Document("$limit", 20),

        // Traer detalle del conductor desde la colección "usuarios"
        new Document("$lookup", new Document()
            .append("from", "usuarios")
            .append("localField", "_id")      // el conductor_id ya está en _id del group
            .append("foreignField", "_id")    // _id del usuario conductor
            .append("as", "conductor_detalle")
        ),

        // Convertir array en objeto
        new Document("$unwind", "$conductor_detalle"),

        // Elegir campos de salida
        new Document("$project", new Document()
            .append("_id", 0)
            .append("conductor_id", "$_id")
            .append("totalServicios", 1)
            .append("nombre", "$conductor_detalle.nombre")
            .append("correo", "$conductor_detalle.correo")
            .append("celular", "$conductor_detalle.celular")
            .append("cedula", "$conductor_detalle.cedula")
        )
    );

    return mongoTemplate
            .getCollection("servicios")
            .aggregate(pipeline)
            .into(new java.util.ArrayList<>());
}
}