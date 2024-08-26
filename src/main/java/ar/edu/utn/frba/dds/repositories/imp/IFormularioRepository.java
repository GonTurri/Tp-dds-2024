package ar.edu.utn.frba.dds.repositories.imp;

import ar.edu.utn.frba.dds.domain.colaboradores.form.Formulario;
import ar.edu.utn.frba.dds.domain.tecnicos.AreaDeCobertura;

import java.util.List;
import java.util.Optional;

public interface IFormularioRepository {
    Optional<Formulario> buscar(long id);

    List<Formulario> buscarTodos();

    void guardar(Formulario formulario);

    void actualizar(Formulario formulario);

    void eliminar(Formulario formulario);
}
