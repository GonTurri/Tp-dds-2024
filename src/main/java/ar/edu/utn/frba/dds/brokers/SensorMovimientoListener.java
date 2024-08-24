package ar.edu.utn.frba.dds.brokers;

import ar.edu.utn.frba.dds.brokers.dtos.SensorMovimientoBrokerDto;
import ar.edu.utn.frba.dds.domain.heladeras.Heladera;
import ar.edu.utn.frba.dds.domain.heladeras.SensorMovimiento;
import ar.edu.utn.frba.dds.domain.incidentes.Alerta;
import ar.edu.utn.frba.dds.domain.incidentes.TipoAlerta;
import ar.edu.utn.frba.dds.domain.notifications.NotificationStrategyFactory;
import ar.edu.utn.frba.dds.helpers.DateHelper;
import ar.edu.utn.frba.dds.helpers.TecnicosHelper;
import ar.edu.utn.frba.dds.repositories.ISensorMovimientoRepository;
import ar.edu.utn.frba.dds.repositories.ITecnicosRepository;
import ar.edu.utn.frba.dds.serviceLocator.ServiceLocator;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import java.util.Optional;

@Setter
public class SensorMovimientoListener implements IMqttMessageListener {
  ISensorMovimientoRepository sensorMovimientoRepository;

  @Override
  public void messageArrived(String s, MqttMessage mqttMessage) {
    SensorMovimientoBrokerDto sensorDto = SensorMovimientoBrokerDto.fromString(mqttMessage.toString());
    Optional<SensorMovimiento> sensorMovimientoOpt = sensorMovimientoRepository.buscar(sensorDto.getIdSensor());

    if (sensorMovimientoOpt.isPresent()) {
      Heladera heladera = sensorMovimientoOpt.get().getHeladera();
      Alerta alerta = Alerta.of(heladera, DateHelper.localDateTimeFromTimestamp(sensorDto.getTimestamp()), new TecnicosHelper((ITecnicosRepository) ServiceLocator.get("tecnicosRepository"))
          , new NotificationStrategyFactory(), TipoAlerta.FRAUDE);
      alerta.reportar();
      IAlertasRepository repository = (IAlertasRepository) ServiceLocator.get("alertasRepository");
      repository.guardar(alerta);
    }
  }
}
