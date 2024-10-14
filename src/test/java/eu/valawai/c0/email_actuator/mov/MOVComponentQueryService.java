/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator.mov;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Check if the component is registered.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@ApplicationScoped
public class MOVComponentQueryService {

	/**
	 * The component to send the query.
	 */
	@Inject
	@Channel("send_query")
	protected Emitter<QueryComponentsPayload> query;

	/**
	 * The information of the registered component.
	 */
	protected JsonObject component;

	/**
	 * The identifier of the last query.
	 */
	protected String queryId = "";

	/**
	 * The component that manage the
	 */
	protected Semaphore semaphore = new Semaphore(0);

	/**
	 * Ask to the MOV about the information of the component.
	 *
	 * @return the information of the component or {2code null} if cannot obtain it.
	 */
	public JsonObject queryComponentInformation() {

		final var query = new QueryComponentsPayload();
		this.queryId = query.id;
		final var semaphore = new Semaphore(0);
		this.component = null;
		this.query.send(query).handle((success, error) -> {

			if (error == null) {

				Log.infov("Sent query {0}.", query);

			} else {

				Log.errorv(error, "Cannot send query.");
			}
			semaphore.release();
			return null;
		});

		try {

			semaphore.tryAcquire(2, 30, TimeUnit.SECONDS);

		} catch (final InterruptedException ignored) {
		}

		return this.component;
	}

	/**
	 * Called when the query result is provided by the MOV.
	 *
	 * @param payload with the query result.
	 */
	@Incoming("found_page")
	public void handleFoundPage(JsonObject payload) {

		if (this.queryId.equals(payload.getString("query_id"))) {

			this.component = null;
			final var array = payload.getJsonArray("components");
			if (array != null && !array.isEmpty()) {

				this.component = array.getJsonObject(0);
			}
			this.semaphore.release();
		}

	}

}
