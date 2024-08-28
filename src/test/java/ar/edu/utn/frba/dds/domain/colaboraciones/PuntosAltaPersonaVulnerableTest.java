package ar.edu.utn.frba.dds.domain.colaboraciones;

import ar.edu.utn.frba.dds.domain.PersonaVulnerable;
import ar.edu.utn.frba.dds.domain.colaboraciones.calculadores.CalculadorPuntos;
import ar.edu.utn.frba.dds.domain.colaboraciones.calculadores.ICalculadorPuntos;
import ar.edu.utn.frba.dds.domain.colaboradores.Colaborador;
import ar.edu.utn.frba.dds.domain.colaboradores.FormaColaboracion;
import ar.edu.utn.frba.dds.domain.tarjetas.Tarjeta;
import ar.edu.utn.frba.dds.domain.utils.TipoDocumento;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class PuntosAltaPersonaVulnerableTest {
    AltaPersonaVulnerable donacion_1, donacion_2;
    Colaborador colaborador;
    PersonaVulnerable personaVulnerable_1, personaVulnerable_2;
    Tarjeta tarjeta_1, tarjeta_2;

    private ICalculadorPuntos calculadorPuntos;
    FormaColaboracion persona = new FormaColaboracion("REGISTRO_PERSONA");

    @BeforeEach
    void test_init(){
        colaborador = new Colaborador();
        personaVulnerable_1 = new PersonaVulnerable("Ernesto", new GregorianCalendar(1990, Calendar.AUGUST, 4).getTime(),  new GregorianCalendar(2000, Calendar.AUGUST, 4).getTime(), true, "Caballito", TipoDocumento.DNI, "45419968", colaborador, null);
        personaVulnerable_2 = new PersonaVulnerable("Jaime", new GregorianCalendar(1995, Calendar.AUGUST, 8).getTime(),  new GregorianCalendar(2005, Calendar.AUGUST, 2).getTime(), true, "Monte Grande", TipoDocumento.DNI, "45419967", colaborador, null);
        donacion_1 = new AltaPersonaVulnerable();
        donacion_1.setColaborador(colaborador);
        donacion_1.setPersona(personaVulnerable_1);
        donacion_1.setTarjeta(tarjeta_1);
        donacion_1.setFecha(LocalDate.of(2023, 1, 1));
        donacion_2 = new AltaPersonaVulnerable();
        donacion_2.setColaborador(colaborador);
        donacion_2.setPersona(personaVulnerable_2);
        donacion_2.setTarjeta(tarjeta_2);
        donacion_2.setFecha(LocalDate.of(2024, 4, 5));
        this.calculadorPuntos = new CalculadorPuntos();
    }

    @Test
    @DisplayName("Se repartieron 2 tarjetas y se obtuvieron 4 puntos")
    void validarPuntosAcumuladosPersonaVulnerable() {
        calculadorPuntos.sumarPuntosPara(colaborador, donacion_1, donacion_2);
        Assertions.assertEquals(4, colaborador.getPuntosGanados());
    }
}