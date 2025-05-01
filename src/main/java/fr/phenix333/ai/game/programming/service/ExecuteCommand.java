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
		L.function("Starts the command for the project");

		this.mqttPublish.mqttPublish();

		L.info("The command to launch : {}", this.command);

		try {
			ProcessBuilder builder = new ProcessBuilder(this.command.split(" "));
			Process process = builder.start();

			this.processInput = process.getOutputStream();

			// Thread to read standard output
			new Thread(() -> {
				try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
					String line;

					while ((line = reader.readLine()) != null) {
						this.mqttPublish.publish(line);
					}
				} catch (Exception e) {
					L.error("Error reading process output", e);

					System.exit(-1);
				}
			}).start();

			// Thread to read error output (for your logging)
			// This is useful for debugging and understanding what the process is doing
			new Thread(() -> {
				try (BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
					String line;

					while ((line = errorReader.readLine()) != null) {
						L.debug("YOUR DEBUG : {}", line);
					}
				} catch (Exception e) {
					L.error("Error reading process error output", e);

					System.exit(-1);
				}
			}).start();
		} catch (Exception e) {
			L.error("An exception has been raised", e);

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
				L.error("Process not yet started or input stream null");
			}
		} catch (Exception e) {
			L.error("Error sending message to process", e);

			System.exit(-1);
		}
	}

}
