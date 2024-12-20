package ar.edu.utn.frba.dds.controllers;

import ar.edu.utn.frba.dds.dtos.heladeras.HeladeraDto;
import ar.edu.utn.frba.dds.dtos.suscripciones.SuscripcionDto;
import ar.edu.utn.frba.dds.exceptions.ContactoVacioException;
import ar.edu.utn.frba.dds.models.domain.heladeras.Heladera;
import ar.edu.utn.frba.dds.models.domain.suscripciones.Suscripcion;
import ar.edu.utn.frba.dds.services.HeladerasService;
import ar.edu.utn.frba.dds.services.SuscripcionesService;
import ar.edu.utn.frba.dds.utils.ICrudViewsHandler;
import io.javalin.http.Context;
import lombok.AllArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
public class SuscripcionesController implements ICrudViewsHandler {

  private SuscripcionesService suscripcionesService;
  private HeladerasService heladerasService;

  @Override
  public void index(Context context) {

  }

  @Override
  public void show(Context context) {

  }

  @Override
  public void create(Context context) {
    String heladeraId = context.pathParam("id");

    HeladeraDto h = this.heladerasService.getHeladeraDto(heladeraId);

    Map<String, Boolean> mediosDisponibles = this.suscripcionesService.contactosDisponibles(context.sessionAttribute("idColaborador"));

    Map<String, Object> model = new HashMap<>();
    mediosDisponibles.forEach((medio, tiene) -> {
      if (tiene) model.put(medio, medio);
    });
    model.put("heladera", h);
    context.render("app/heladeras/suscripcion.hbs", model);
  }

  @Override
  public void save(Context context) {
    SuscripcionDto dto = SuscripcionDto.fromContext(context);
    Map<String, Object> model = new HashMap<>();
    try {
      Suscripcion nuevaSuscripcion = this.suscripcionesService.guardarSuscripcion(dto);
      Heladera h = this.heladerasService.obtenerHeladera(context.pathParam("id"));
      this.heladerasService.agregarSuscripcionAHeladera(h, nuevaSuscripcion);
      model.put("message", "Tu suscripcion fue registrada con exito");
      context.status(201);
      context.render("app/success.hbs", model);
    } catch (ContactoVacioException e) {
      e.printStackTrace();
      model.put("message", e.getMessage());
      context.status(400);
      context.render("app/error.hbs", model);
    }
  }

  @Override
  public void edit(Context context) {

  }

  @Override
  public void update(Context context) {

  }

  @Override
  public void delete(Context context) {

  }
}
