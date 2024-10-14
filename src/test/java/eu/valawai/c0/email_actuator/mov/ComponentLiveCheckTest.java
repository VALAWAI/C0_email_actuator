/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator.mov;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.microprofile.health.HealthCheckResponse;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * Test the {@link ComponentLiveCheck}.
 *
 * @see ComponentLiveCheck
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@WithTestResource(value = MOVTestResource.class)
public class ComponentLiveCheckTest {

	/**
	 * The status of the component.
	 */
	@Inject
	protected ComponentStatus status;

	/**
	 * The component to manage JSON objects.
	 */
	@Inject
	protected ObjectMapper mapper;

	/**
	 * Check that the health is down.
	 */
	@Test
	public void shouldHealthBeDown() {

		final var check = new ComponentLiveCheck();
		check.status = new ComponentStatus();
		final var result = check.call();
		assertEquals(HealthCheckResponse.Status.DOWN, result.getStatus());

	}

	/**
	 * Check that the health is up.
	 */
	@Test
	public void shouldHealthBeUp() {

		final var check = new ComponentLiveCheck();
		check.status = new ComponentStatus();
		check.status.registered("id");
		final var result = check.call();
		assertEquals(HealthCheckResponse.Status.UP, result.getStatus());

	}

	/**
	 * Check can get health status from the URL.
	 *
	 * @throws JsonProcessingException If can not obtain the status json result.
	 * @throws JsonMappingException    If can not obtain the status json result.
	 */
	@Test()
	public void shouldCheckHealth() throws JsonMappingException, JsonProcessingException {

		boolean found = false;
		final var content = given().when().get("/q/health").then().statusCode(200).extract().asString();
		final var body = this.mapper.readTree(content);
		final var checks = body.get("checks");
		final var max = checks.size();
		for (var i = 0; i < max; i++) {

			final var healthCheck = checks.get(i);
			if (ComponentLiveCheck.NAME.equals(healthCheck.get("name").asText())) {

				final var isUp = HealthCheckResponse.Status
						.valueOf(healthCheck.get("status").asText()) == HealthCheckResponse.Status.UP;
				assertEquals(this.status.isRegistered(), isUp);
				found = true;
				break;
			}

		}

		assertTrue(found, "Not found component live check");
	}

}
