package fr.phenix333.ai.game.programming;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import fr.phenix333.ai.game.programming.service.MqttSubscribe;
import fr.phenix333.logger.MyLogger;

/*
 * Main class for the MQTT application.
 * This class is responsible for starting the Spring Boot application and initializing the MQTT subscription.
 * It also handles the deletion of any existing Paho MQTT lock files to avoid conflicts.
 * 
 * @author Colin de Seroux
 */
@SpringBootApplication
public class AiGameProgrammingMqttApplication implements CommandLineRunner {

	private static final MyLogger L = MyLogger.create(AiGameProgrammingMqttApplication.class);

	@Autowired
	private MqttSubscribe mqttSubscribe;

	public static void main(String[] args) {
		L.function("");

		SpringApplication.run(AiGameProgrammingMqttApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		L.function("Launching the main code");

		this.deletePaho();

		this.mqttSubscribe.subscribeMqtt();
	}

	private void deletePaho() {
		L.debug("Paho folder suppression (MQTT)");

		File[] fichiers = new File(System.getProperty("user.dir")).listFiles();

		if (fichiers != null) {
			for (File fichier : fichiers) {
				if (fichier.isDirectory() && fichier.getName().startsWith("paho")) {
					File file = new File(fichier, ".lck");
					file.delete();
					fichier.delete();
				}
			}
		}
	}

}
