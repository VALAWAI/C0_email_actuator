/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator.mov;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.quarkus.logging.Log;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.config.Priorities;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

/**
 * The class used to manage the component life cycle. Thus, this register the
 * component when started and unregister when shutdown.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@ApplicationScoped
public class ComponentLifeCycle {

	/**
	 * The queue to send the registration message.
	 */
	@Inject
	@Channel("send_register_component")
	protected Emitter<RegisterComponentPayload> register;

	/**
	 * The queue to send the registration message.
	 */
	@Inject
	@Channel("send_unregister_component")
	protected Emitter<UnregisterComponentPayload> unregister;

	/**
	 * The current version of the component.
	 */
	@ConfigProperty(name = "quarkus.application.version")
	protected String version;

	/**
	 * The status of the component.
	 */
	@Inject
	protected ComponentStatus status;

	/**
	 * Called when the component is started and it must to register it on the
	 * VALAWAI infrastructure.
	 *
	 * @param event that contains the start status.
	 */
	public void handle(@Observes @Priority(Priorities.APPLICATION) StartupEvent event) {

		final var payload = new RegisterComponentPayload();
		payload.version = this.version;
		this.register.send(payload).handle((success, error) -> {

			if (error == null) {

				Log.infov("Sent register {0}.", payload);

			} else {

				Log.errorv(error, "Cannot register the component.");
			}
			return null;
		});

	}

	/**
	 * Called when the component is finished and it has to unregister it from the
	 * VALAWAI infrastructure.
	 *
	 * @param event that contains the start status.
	 */
	public void handle(@Observes ShutdownEvent event) {

		if (this.status.isRegistered()) {

			final var payload = new UnregisterComponentPayload();
			payload.component_id = this.status.getRegisteredId();
			this.unregister.send(payload).handle((success, error) -> {

				if (error == null) {

					Log.infov("Sent unregister {0}.", payload);

				} else {

					Log.errorv(error, "Cannot unregister the component.");
				}
				return null;
			});

		}

	}

	/**
	 * Called when the component is registered.
	 *
	 * @param payload with the component information.
	 */
	@Incoming("registered")
	public void handleControlRegistered(JsonObject payload) {

		final var componentId = payload.getString("id");
		this.status.registered(componentId);
		Log.infov("Registered component {0}.", payload);

	}

}
