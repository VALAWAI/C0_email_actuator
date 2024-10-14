/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/
package eu.valawai.c0.email_actuator.mov;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

/**
 * The test resource that runs the Master Of VALAWAI that can be used to test
 * the component.
 *
 * @author VALAWAI
 */
public class MOVTestResource implements QuarkusTestResourceLifecycleManager {

	/**
	 * The name of the mongo docker image to use.
	 */
	public static final String MONGO_DOCKER_NAME = "mongo:latest";

	/**
	 * The name of the rabbit mq docker image to use.
	 */
	public static final String RABBITMQ_DOCKER_NAME = "rabbitmq:latest";

	/**
	 * The name of the Master Of VALAWAI docker image to use.
	 */
	public static final String MOV_DOCKER_NAME = "valawai/mov:latest";

	/**
	 * The name of the property where the Master Of VALAWAI is listening.
	 */
	public static final String MOV_URL_CONFIG_PROPERTY_NAME = "mov.port";

	/**
	 * The mongo service container.
	 */
	static GenericContainer<?> mongoContainer = new GenericContainer<>(DockerImageName.parse(MONGO_DOCKER_NAME))
			.withStartupAttempts(1).withEnv("MONGO_INITDB_ROOT_USERNAME", "root")
			.withEnv("MONGO_INITDB_ROOT_PASSWORD", "password").withEnv("MONGO_INITDB_DATABASE", "movDB")
			.withCopyFileToContainer(
					MountableFile.forClasspathResource(
							MOVTestResource.class.getPackageName().replaceAll("\\.", "/") + "/initialize-movDB.js"),
					"/docker-entrypoint-initdb.d/init-mongo.js")
			.withExposedPorts(27017).waitingFor(Wait.forListeningPort());

	/**
	 * The RabbitMQ service container.
	 */
	static GenericContainer<?> rabbitMQContainer = new GenericContainer<>(DockerImageName.parse(RABBITMQ_DOCKER_NAME))
			.withStartupAttempts(1).withEnv("RABBITMQ_DEFAULT_USER", "mov").withEnv("RABBITMQ_DEFAULT_PASS", "password")
			.withExposedPorts(5672).waitingFor(Wait.forListeningPort());

	/**
	 * The Master Of VALAWAI service container.
	 */
	static GenericContainer<?> movContainer = null;

	/**
	 * Start the mocked server.
	 *
	 * {@inheritDoc}
	 */
	@SuppressWarnings("resource")
	@Override
	public Map<String, String> start() {

		final var config = new HashMap<String, String>();

		if (Boolean.parseBoolean(System.getProperty("useDevMOV"))) {

			config.put("rabbitmq-host", "host.docker.internal");
			config.put(MOV_URL_CONFIG_PROPERTY_NAME, "http://host.docker.internal:8084");

		} else {

			mongoContainer.start();
			final var mongoConnection = "mongodb://mov:password@" + mongoContainer.getHost() + ":"
					+ mongoContainer.getMappedPort(27017) + "/movDB";

			rabbitMQContainer.start();
			final var rabbitMQHost = rabbitMQContainer.getHost();
			config.put("rabbitmq-host", rabbitMQHost);
			final var rabbitMQPort = String.valueOf(rabbitMQContainer.getMappedPort(5672));
			config.put("rabbitmq-port", rabbitMQPort);

			movContainer = new GenericContainer<>(DockerImageName.parse(MOV_DOCKER_NAME)).withStartupAttempts(1)
					.withEnv("rabbitmq-host", rabbitMQHost).withEnv("rabbitmq-port", rabbitMQPort)
					.withEnv("quarkus.mongodb.connection-string", mongoConnection).withExposedPorts(8080)
					.waitingFor(Wait.forListeningPort());
			movContainer.start();
			config.put(MOV_URL_CONFIG_PROPERTY_NAME,
					"http://" + movContainer.getHost() + ":" + movContainer.getMappedPort(8080));

		}

		return config;

	}

	/**
	 * Stop the mocked server.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void stop() {

		if (mongoContainer.isRunning()) {

			mongoContainer.close();

		}

		if (rabbitMQContainer.isRunning()) {

			rabbitMQContainer.close();

		}

		if (movContainer != null && movContainer.isRunning()) {

			movContainer.close();
			movContainer = null;
		}

	}

}
