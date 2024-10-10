package ar.edu.utn.frba.dds.dtos.personas;

import ar.edu.utn.frba.dds.dtos.DireccionDto;
import ar.edu.utn.frba.dds.dtos.usuarios.UsuarioDto;
import io.javalin.http.Context;
import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class PersonaJuridicaDto {
  private String razonSocial;
  private String tipoOrganizacion;
  private String rubro;
  private DireccionDto direccion;
  private List<FormaColaboracionDto> formasColaboracion;
  private List<MedioContactoDto> mediosDeContacto;
  private UsuarioDto usuarioDto;
  private String claveConf;

  public static PersonaJuridicaDto of(Context context) {
    return PersonaJuridicaDto.builder().razonSocial(context.formParam("razonSocial"))
        .tipoOrganizacion(context.formParam("tipoOrganizacion"))
        .rubro(context.formParam("rubro"))
        .direccion((context.formParam("calle") != null && !context.formParam("calle").isBlank() &&
            context.formParam("altura") != null && !context.formParam("altura").isBlank() &&
            context.formParam("cp") != null && !context.formParam("cp").isBlank())
            ? DireccionDto.builder()
            .calle(context.formParam("calle"))
            .numero(Integer.parseInt(context.formParam("altura")))
            .piso((context.formParam("piso") != null && !context.formParam("piso").isBlank()) ? Integer.valueOf(context.formParam("piso")) : null)
            .codigoPostal(context.formParam("cp"))
            .build()
            : null)
        .claveConf(context.formParam("passConf"))
        .usuarioDto(new UsuarioDto(context.formParam("email"), context.formParam("password")))
        .mediosDeContacto(MedioContactoDto.of(context))
        .formasColaboracion(FormaColaboracionDto.of(context))
        .build();
  }

  public boolean sonClavesIguales() {
    return this.claveConf.equals(this.usuarioDto.getClave());
  }

  public boolean estanCamposLlenos() {
    return this.razonSocial != null && (this.direccion == null || this.direccion.estanCamposLlenos()) && this.rubro != null && this.formasColaboracion != null &&
        this.mediosDeContacto != null && this.usuarioDto != null && this.tipoOrganizacion != null;
  }

}
