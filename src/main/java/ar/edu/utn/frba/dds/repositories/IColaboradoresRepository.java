package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.domain.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.domain.utils.TipoDocumento;
import java.util.List;
import java.util.Optional;

/**
 * IColaboradorRepository interface permite interactuar con los colaboradores.
 */
public interface IColaboradoresRepository {
  Optional<Colaborador> buscar(TipoDocumento tipoDocumento, Integer documento);

  List<Colaborador> buscarTodos();

  void guardar(Colaborador colaborador);

  void actualizar(Colaborador colaborador);

  void eliminar(Colaborador colaborador);
}