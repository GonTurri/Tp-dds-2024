package ar.edu.utn.frba.dds.brokers.dtos;

import lombok.Getter;

@Getter
public class SensorTemperaturaBrokerDto {
  // formato del mensaje: ID_SENSOR;TEMPERATURA;TIMESTAMP
  public String idSensor;
  public float temperatura;
  public long timestamp;

  private SensorTemperaturaBrokerDto(String idSensor, float temperatura, long timestamp) {
    this.idSensor = idSensor;
    this.temperatura = temperatura;
    this.timestamp = timestamp;
  }

  public static SensorTemperaturaBrokerDto fromString(String s) {
    String[] fields = s.split(";");
    return new SensorTemperaturaBrokerDto(fields[0], Float.parseFloat(fields[1]), Long.parseLong(fields[2]));
  }

  public String toString() {
    return String.format("ID: %s --- Temp: %f --- Timestamp: %d", this.idSensor, this.temperatura, this.timestamp);
  }
}
