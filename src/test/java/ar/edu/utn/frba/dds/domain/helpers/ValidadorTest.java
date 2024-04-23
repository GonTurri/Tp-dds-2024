package ar.edu.utn.frba.dds.domain.helpers;

import ar.edu.utn.frba.dds.domain.helpers.validaciones.ValidacionLargoClaves;
import ar.edu.utn.frba.dds.domain.helpers.validaciones.ValidacionListaClavesNuevo;
import org.junit.jupiter.api.*;

class ValidadorTest {

    ValidadorClaves unValidador;

    @BeforeEach
    void test_init() {
        this.unValidador = new ValidadorClaves();
        this.unValidador.agregarValidaciones(new ValidacionLargoClaves(), new ValidacionListaClavesNuevo());
    }

    @Test
    @DisplayName("contrasena correcta")
    void validarContrasenaCorrecta() {
        Assertions.assertTrue(unValidador.esValida("thomas_ariel_luca"));
    }

    @Test
    @DisplayName("contrasena entre las 10.000 peores")
    void validarContrasenaEnLista10000() {
        Assertions.assertFalse(unValidador.esValida("password"));
        Assertions.assertEquals("La clave aparece en la lista de las 10.000 peores contrasenias", unValidador.getMotivoNoValida());
    }

    @Test
    @DisplayName("contrasena muy corta")
    void validarContrasenaCorta() {
        Assertions.assertFalse(unValidador.esValida("123"));
        Assertions.assertEquals("La contrasena es muy corta, debe tener como minimo " + ValidacionLargoClaves.getLONGITUD_MINIMA() + " caracteres.", unValidador.getMotivoNoValida());
    }

    @Test
    @DisplayName("contrasena muy larga")
    void validarContrasenaLarga() {
        Assertions.assertFalse(unValidador.esValida("D$w9&XqPz4!sGv2@rN#lJ*cEoF+tH(3zL-xM/vK,nA.bQ1:wG^fR%yT&uI=8Y7U6iO;9P0"));
        Assertions.assertEquals("La contrasena es muy larga, debe tener como maximo " + ValidacionLargoClaves.getLONGITUD_MAXIMA() + " caracteres.", unValidador.getMotivoNoValida());
    }
}