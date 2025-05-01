package fr.phenix333.ai.game.programming.service;

import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.phenix333.logger.MyLogger;

/**
 * Service class responsible for publishing messages to an MQTT broker. This
 * class handles the connection to the broker and the publishing of messages.
 * 
 * @author Colin de Seroux
 */
@Service
public class MqttPublish {

	private static final MyLogger L = MyLogger.create(MqttSubscribe.class);

	@Value("${mqtt.host}")
	private String mqttHost;

	@Value("${mqtt.port}")
	private String mqttPort;

	@Value("${mqtt.user}")
	private String mqttUser;

	@Value("${mqtt.opponent}")
	private String mqttOpponent;

	private MqttClient client;

	/**
	 * Initializes mqttPublish for later use
	 */
	public void mqttPublish() {
		L.function("Initialize mqttPublish for later use");

		String broker = String.format("tcp://%s:%s", this.mqttHost, this.mqttPort);

		MemoryPersistence persistence = new MemoryPersistence();

		try {
			this.client = new MqttClient(broker, MqttAsyncClient.generateClientId(), persistence);

			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);

			this.client.connect(connOpts);
		} catch (MqttException e) {
			L.error("An MQTT error has occurred", e);

			// I assume that since the project is based on MQTT, if the function for sending
			// a message no longer works, the project cannot function.
			System.exit(-1);
		}
	}

	/**
	 * Send a message on a specific topic
	 *
	 * @param theMessage -> String : the message to send
	 */
	public void publish(String theMessage) {
		L.function("Send a message on a specific topic | message : {}", theMessage);

		MqttMessage message = new MqttMessage(theMessage.getBytes());

		try {
			this.client.publish(String.format("awale/%s", this.mqttOpponent), message);
		} catch (MqttException e) {
			L.error("An MQTT error has occurred", e);

			// I assume that since the project is based on MQTT, if the function for sending
			// a message no longer works, the project cannot function.
			System.exit(-1);
		}

		L.info("New message sent : {}", theMessage);
	}

}
