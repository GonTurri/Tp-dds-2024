package ar.edu.utn.frba.dds.models.repositories;


import ar.edu.utn.frba.dds.models.domain.tarjetas.Tarjeta;
import java.util.List;
import java.util.Optional;

/**
 * IColaboradorRepository interface permite interactuar con las tarjetas de las personas vulnerables.
 */
public interface ITarjetasRepository {
  Optional<Tarjeta> buscarPorCodigo(String codigo);

  Optional<Tarjeta> buscar(String id);

  List<Tarjeta> buscarTodos();

  void guardar(Tarjeta tarjeta);

  void actualizar(Tarjeta tarjeta);

  void actualizar(List<Tarjeta> tarjetas);

  void eliminar(Tarjeta tarjeta);
}
