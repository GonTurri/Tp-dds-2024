package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.domain.tecnicos.Tecnico;
import ar.edu.utn.frba.dds.domain.utils.TipoDocumento;
import java.util.List;
import java.util.Optional;

public interface ITecnicosRepository {
  Optional<Tecnico> buscar(TipoDocumento tipoDocumento, String documento);
  Optional<Tecnico> buscar(Long id);

  List<Tecnico> buscarTodos();

  void guardar(Tecnico tecnico);

  void actualizar(Tecnico tecnico);

  void eliminar(Tecnico tecnico);
}
