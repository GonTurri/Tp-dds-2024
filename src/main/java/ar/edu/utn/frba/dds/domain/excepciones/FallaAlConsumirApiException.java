package ar.edu.utn.frba.dds.domain.excepciones;

public class FallaAlConsumirApiException extends RuntimeException {
  public FallaAlConsumirApiException(String message) {
    super(message);
  }
}