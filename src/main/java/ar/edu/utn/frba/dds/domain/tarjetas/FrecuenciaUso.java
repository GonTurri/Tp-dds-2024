package ar.edu.utn.frba.dds.domain.tarjetas;

/**
 * FrecuenciaUso interface permite definir el comportamiento de la frecuencia de uso de una tarjeta.
 */
public interface FrecuenciaUso {

  // TEMPLATE METHOD
  boolean permiteUsar(Tarjeta tarjeta);

}