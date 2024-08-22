package ar.edu.utn.frba.dds.domain.suscripciones;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import ar.edu.utn.frba.dds.domain.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.domain.heladeras.Heladera;
import ar.edu.utn.frba.dds.domain.heladeras.Vianda;
import ar.edu.utn.frba.dds.domain.notifications.NotificationStrategy;
import ar.edu.utn.frba.dds.domain.utils.CanalContacto;
import ar.edu.utn.frba.dds.domain.utils.MedioDeContacto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SuscripcionVIandasFaltantesTest {





  @Test
  @DisplayName("pruebo que notifique el observer ante evento")
  void mandarNotificacion(){
    Suscripcion sucripcion;
    NotificationStrategy strategy = Mockito.mock(NotificationStrategy.class);
    doNothing().when(strategy).notificar(any(),any());
    sucripcion = new Suscripcion(new Colaborador(),
        strategy,new SuscripcionViandasFaltantes(),2);

    List<Vianda> viandas = new ArrayList<>();

    viandas.add(new Vianda());
    viandas.add(new Vianda());

    Heladera heladera = new Heladera(LocalDate.now());
    heladera.setCapacidadViandas(5);
    heladera.agregarSuscripcion(sucripcion);
    heladera.setViandas(viandas);
    heladera.agregarVianda(new Vianda());



    verify(strategy,times(1)).notificar(any(),any());
  }

  @Test
  @DisplayName("pruebo que no notifque el observer cuando no debe")
  void noMandaNada(){
    Suscripcion sucripcion;
    NotificationStrategy strategy = Mockito.mock(NotificationStrategy.class);
    doNothing().when(strategy).notificar(any(),any());
    sucripcion = new Suscripcion(new Colaborador(),
        strategy,new SuscripcionViandasFaltantes(),2);

    List<Vianda> viandas = new ArrayList<>();

    viandas.add(new Vianda());

    Heladera heladera = new Heladera(LocalDate.now());
    heladera.setCapacidadViandas(5);
    heladera.agregarSuscripcion(sucripcion);
    heladera.setViandas(viandas);
    heladera.agregarVianda(new Vianda());



    verify(strategy,times(0)).notificar(any(),any());
  }
}
