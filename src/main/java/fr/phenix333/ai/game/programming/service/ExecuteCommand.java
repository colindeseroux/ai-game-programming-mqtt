package fr.phenix333.ai.game.programming.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fr.phenix333.logger.MyLogger;

/*
 * Service class responsible for executing a command and handling its input and output.
 * 
 * @author Colin de Seroux
 */
@Service
public class ExecuteCommand {

	private static final MyLogger L = MyLogger.create(ExecuteCommand.class);

	@Autowired
	private MqttPublish mqttPublish;

	@Value("${command}")
	private String command;

	private OutputStream processInput;

	/**
	 * Initialize the service and set up the MQTT publisher.
	 */
	public void executeCommand() {
		L.function("Lance la commande pour le projet");

		this.mqttPublish.mqttPublish();

		L.info("La commande a lancer : {}", this.command);

		try {
			ProcessBuilder builder = new ProcessBuilder(this.command.split(" "));
			Process process = builder.start();

			this.processInput = process.getOutputStream();

			new Thread(() -> {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line;

					while ((line = reader.readLine()) != null) {
						this.mqttPublish.publish(line);
					}
				} catch (Exception e) {
					L.error("Erreur lors de la lecture de la sortie du processus", e);

					System.exit(-1);
				}
			}).start();
		} catch (Exception e) {
			L.error("Une exception est remontee", e);

			System.exit(-1);
		}
	}

	/**
	 * Send a message to the process's input stream.
	 *
	 * @param message -> String : the message to send
	 */
	public void sendMessageToProcess(String message) {
		try {
			if (this.processInput != null) {
				this.processInput.write((message + "\n").getBytes());
				this.processInput.flush();
			} else {
				L.error("Le processus n'est pas encore démarré ou le flux d'entrée est null");
			}
		} catch (Exception e) {
			L.error("Erreur lors de l'envoi du message au processus", e);

			System.exit(-1);
		}
	}

}
