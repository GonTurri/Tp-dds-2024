package ar.edu.utn.frba.dds.models.repositories.mainTests;

import ar.edu.utn.frba.dds.models.domain.colaboraciones.DonacionVianda;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.calculadores.ICalculadorPuntos;
import ar.edu.utn.frba.dds.models.domain.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.models.domain.colaboradores.autenticacion.Usuario;
import ar.edu.utn.frba.dds.models.domain.heladeras.Vianda;
import ar.edu.utn.frba.dds.models.repositories.IColaboradoresRepository;
import ar.edu.utn.frba.dds.models.repositories.IDonacionesViandaRepository;
import ar.edu.utn.frba.dds.serviceLocator.ServiceLocator;
import java.time.LocalDate;
import java.util.List;

public class MainDonacionesPuntaje {
  public static void main(String[] args) {

    IColaboradoresRepository repoColab = ServiceLocator.get(IColaboradoresRepository.class);
    IDonacionesViandaRepository repoDonacionesVianda = ServiceLocator.get(IDonacionesViandaRepository.class);

    Colaborador c = new Colaborador();
    c.setUsuario(new Usuario("email@email.com", "clave"));
    repoColab.guardar(c);

    for (int i = 0; i < 10; i++) {
      Vianda v = new Vianda();
      v.setColaborador(c);
      DonacionVianda d = new DonacionVianda();
      d.setFecha(LocalDate.now());
      d.setVianda(v);
      d.setColaborador(c);
      repoDonacionesVianda.guardar(d);
    }

    List<DonacionVianda> donaciones = repoDonacionesVianda.buscarPorColaborador(c.getId());

    for (DonacionVianda d : donaciones) {
      ServiceLocator.get(ICalculadorPuntos.class).sumarPuntosPara(c, d);
    }

    repoColab.actualizar(c);
  }
}
