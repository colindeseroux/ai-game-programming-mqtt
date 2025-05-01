package fr.phenix333.ai.game.programming.service;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.phenix333.logger.MyLogger;

/**
 * Service class responsible for subscribing to an MQTT broker and handling
 * incoming messages. This class implements the MqttCallback interface to
 * receive messages from the broker.
 * 
 * @author Colin de Seroux
 */
@Service
public class MqttSubscribe implements MqttCallback {

	private static final MyLogger L = MyLogger.create(MqttSubscribe.class);

	@Autowired
	private ExecuteCommand executeCommand;

	@Value("${mqtt.host}")
	private String mqttHost;

	@Value("${mqtt.port}")
	private String mqttPort;

	@Value("${mqtt.user}")
	private String mqttUser;

	@Value("${mqtt.opponent}")
	private String mqttOpponent;

	/**
	 * Initializes variables for receiving messages and subscribing to topics
	 */
	public void subscribeMqtt() {
		L.function("Initialise les variables pour la reception de messages et de s'inscrire au topic");

		L.info("Nouvelle partie : {} VS {}", this.mqttUser, this.mqttOpponent);

		String broker = String.format("tcp://%s:%s", this.mqttHost, this.mqttPort);

		L.info("Broker mqtt : {}", broker);

		try {
			MqttClient client = new MqttClient(broker, MqttAsyncClient.generateClientId());

			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			client.connect(connOpts);
			client.setCallback(this);
			client.subscribe(String.format("awale/%s", this.mqttUser));
		} catch (MqttException e) {
			L.error("Une erreur MQTT est remontee", e);

			// Je pars du principe que vue que le projet se base sur MQTT si la
			// fonction pour recevoir un message ne fonctionne plus le projet ne peux
			// fonctionner
			System.exit(-1);
		}

		this.executeCommand.executeCommand();
	}

	@Override
	public void connectionLost(Throwable cause) {
		L.error("Connection perdue");

		System.exit(-1);
	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		L.info("Nouveau message arrive : {}", message.toString());

		this.executeCommand.sendMessageToProcess(message.toString());
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		L.warn("Delivery complete");
	}

}
