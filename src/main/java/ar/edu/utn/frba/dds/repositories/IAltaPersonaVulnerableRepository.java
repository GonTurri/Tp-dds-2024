package ar.edu.utn.frba.dds.repositories;

import ar.edu.utn.frba.dds.domain.colaboraciones.AltaPersonaVulnerable;
import ar.edu.utn.frba.dds.domain.tecnicos.AreaDeCobertura;

import java.util.List;
import java.util.Optional;

public interface IAltaPersonaVulnerableRepository {
    Optional<AltaPersonaVulnerable> buscar(long id);

    List<AltaPersonaVulnerable> buscarTodos();

    void guardar(AltaPersonaVulnerable altaPersonaVulnerable);

    void actualizar(AltaPersonaVulnerable altaPersonaVulnerable);

    void eliminar(AltaPersonaVulnerable altaPersonaVulnerable);
}
