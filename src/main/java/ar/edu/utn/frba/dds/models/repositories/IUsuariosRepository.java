package ar.edu.utn.frba.dds.models.repositories;

import ar.edu.utn.frba.dds.models.domain.colaboradores.autenticacion.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuariosRepository {
    Optional<Usuario> buscar(String id);

    Optional<Usuario> buscar(String email, String contra);

    List<Usuario> buscarTodos();

    void guardar(Usuario usuario);

    void actualizar(Usuario usuario);

    void eliminar(Usuario usuario);
}