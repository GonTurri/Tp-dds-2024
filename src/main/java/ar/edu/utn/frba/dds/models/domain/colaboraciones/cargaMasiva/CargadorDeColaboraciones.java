package ar.edu.utn.frba.dds.models.domain.colaboraciones.cargaMasiva;

import ar.edu.utn.frba.dds.helpers.ConfigReader;
import ar.edu.utn.frba.dds.helpers.PasswordGenerator;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.IPuntajeCalculable;
import ar.edu.utn.frba.dds.models.domain.colaboraciones.calculadores.ICalculadorPuntos;
import ar.edu.utn.frba.dds.models.domain.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.models.domain.colaboradores.FormaColaboracion;
import ar.edu.utn.frba.dds.models.domain.colaboradores.TipoColaborador;
import ar.edu.utn.frba.dds.models.domain.colaboradores.TipoPersona;
import ar.edu.utn.frba.dds.models.domain.colaboradores.autenticacion.Permiso;
import ar.edu.utn.frba.dds.models.domain.colaboradores.autenticacion.Rol;
import ar.edu.utn.frba.dds.models.domain.colaboradores.autenticacion.Usuario;
import ar.edu.utn.frba.dds.models.domain.colaboradores.factories.ColaboradorFactory;
import ar.edu.utn.frba.dds.models.domain.colaboradores.factories.UsuarioFactory;
import ar.edu.utn.frba.dds.models.domain.emailSending.MailSenderAdapter;
import ar.edu.utn.frba.dds.models.domain.emailSending.MyEmail;
import ar.edu.utn.frba.dds.models.domain.emailSending.MyMailFactory;
import ar.edu.utn.frba.dds.models.domain.excepciones.CsvInvalidoException;
import ar.edu.utn.frba.dds.models.domain.utils.TipoDocumento;
import ar.edu.utn.frba.dds.models.domain.utils.TipoDocumentoMapper;
import ar.edu.utn.frba.dds.models.repositories.IColaboradoresRepository;
import ar.edu.utn.frba.dds.models.repositories.IFormasColaboracionRespository;
import ar.edu.utn.frba.dds.models.repositories.IRolesRepository;
import ar.edu.utn.frba.dds.serviceLocator.ServiceLocator;
import ar.edu.utn.frba.dds.utils.PasswordHasher;
import ar.edu.utn.frba.dds.utils.PermisosHelper;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CargadorDeColaboraciones class se encarga de cargar colaboraciones.
 */
@Setter
@Getter
public class CargadorDeColaboraciones {
  private CSVReaderAdapter csvReader;
  private MailSenderAdapter mailSender;
  private String filePath;
  private String separator;
  private IColaboradoresRepository colaboradorRepository;
  private IFormasColaboracionRespository formasColaboracionRespository;
  private ConfigReader config;
  private ICalculadorPuntos calculadorPuntos;
  private IRolesRepository rolesRepository;

  /**
   * Constructor con parametros.
   */
  public CargadorDeColaboraciones(String filePath, CSVReaderAdapter csvReader, MailSenderAdapter mailAdapter, IColaboradoresRepository respository, IFormasColaboracionRespository formasColaboracionRespository, ICalculadorPuntos calculadorPuntos, IRolesRepository rolesRepository) throws IOException {
    this.config = new ConfigReader("config.properties");
    this.csvReader = csvReader;
    this.mailSender = mailAdapter;
    this.filePath = filePath;
    this.separator = config.getProperty("separator");
    this.colaboradorRepository = respository;
    this.formasColaboracionRespository = formasColaboracionRespository;
    this.calculadorPuntos = calculadorPuntos;
    this.rolesRepository = rolesRepository;
  }

  /**
   * Metodo cargarColaboraciones que se encarga de cargar colaboraciones.
   */
  public List<IPuntajeCalculable> cargarColaboraciones() throws IOException {
    List<Object> registros = csvReader.readCsv(filePath, separator);
    ArrayList<IPuntajeCalculable> colaboraciones = new ArrayList<>();
    Colaborador colaborador = null;

    for (Object reg : registros) {
      CargaColaboracion carga = (CargaColaboracion) reg;

      Optional<Colaborador> colaboradorOption = colaboradorRepository.buscar(
          new TipoDocumentoMapper().obtenerTipoDeDocumento(carga.getTipoDocumento()),
          carga.getDocumento());
      colaborador = colaboradorOption.orElseGet(() -> crearUsuarioColaboradorNoRegistrado(carga, config));

      Optional<FormaColaboracion> forma = this.formasColaboracionRespository.buscarPorNombre(carga.getFormaColaboracion());
      if (forma.isEmpty()) throw new CsvInvalidoException("El csv no es valido!");

      validarYAgregarPermisos(colaborador, forma.get());

      IPuntajeCalculable colaboracion = CargaToColaboracionMapper.colaboracionFromCarga(carga, colaborador);

      for (int i = 0; i < carga.getCantidad(); i++) {
        colaboraciones.add(colaboracion);
        this.calculadorPuntos.sumarPuntosPara(colaborador, colaboracion);
      }

      this.colaboradorRepository.actualizar(colaborador);
    }
    return colaboraciones;
  }

  private Colaborador crearUsuarioColaboradorNoRegistrado(CargaColaboracion carga, ConfigReader config) {
    try {
      TipoDocumento tipoDoc = new TipoDocumentoMapper().obtenerTipoDeDocumento(carga.getTipoDocumento());
      String claveGenerada = PasswordGenerator.generatePassword(Integer.parseInt(config.getProperty("password.length")));
      Usuario nuevoUsuario = UsuarioFactory.createUsuario(carga.getMail(), PasswordHasher.hashPassword(claveGenerada));
      Colaborador nuevoColaborador = ColaboradorFactory.createColaborador(nuevoUsuario);
      nuevoColaborador.setTipoDocumento(tipoDoc);
      nuevoColaborador.setDocumento(carga.getDocumento());
      nuevoColaborador.setNombre(carga.getNombre());
      nuevoColaborador.setApellido(carga.getApellido());


      Optional<FormaColaboracion> forma = this.formasColaboracionRespository.buscarPorNombre(carga.getFormaColaboracion());
      if (forma.isEmpty()) throw new CsvInvalidoException("El csv no es valido!");
      //TODO: solo para personas humanas ??? ?? ??
      TipoColaborador tipo = new TipoColaborador();
      tipo.setTipo(TipoPersona.PERSONA_HUMANA);
      tipo.agregarFormasColaboracion(forma.get());
      nuevoColaborador.setTipoColaborador(tipo);

      Rol nuevoRol = Rol.of(PermisosHelper.getInstance().fromFormaColaboracion(forma.get()));
      this.rolesRepository.guardar(nuevoRol);
      nuevoUsuario.agregarRoles(nuevoRol);

      MyEmail email = MyMailFactory.createMail(config.getProperty("MAIL-DIR"),
          carga.getMail(),
          config.getProperty("ASUNTO"),
          config.getProperty("CUERPO") + claveGenerada);

      mailSender.enviarMail(email);

      colaboradorRepository.guardar(nuevoColaborador);
      return nuevoColaborador;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void validarYAgregarPermisos(Colaborador colaborador, FormaColaboracion forma) {
    if (!colaborador.getTipoColaborador().tenesFormaColaboracion(forma.getNombreInterno()))
      colaborador.getTipoColaborador().agregarFormasColaboracion(forma);

    if (!colaborador.getUsuario().tenesPermisos(PermisosHelper.getInstance().fromFormaColaboracion(forma).toArray(new Permiso[0]))) {
      colaborador.getUsuario().getRoles().get(0).agregarPermisos(PermisosHelper.getInstance().fromFormaColaboracion(forma).toArray(new Permiso[0]));
    }
  }

}