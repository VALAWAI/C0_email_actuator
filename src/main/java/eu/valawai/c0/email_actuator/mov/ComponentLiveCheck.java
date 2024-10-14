/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator.mov;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import jakarta.inject.Inject;

/**
 * Check if the component is alive. Thus, that is has been registered.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@Liveness
public class ComponentLiveCheck implements HealthCheck {

	/**
	 * The name of the check.
	 */
	public static final String NAME = "Registered C0 email actuator";

	/**
	 * The status of the component.
	 */
	@Inject
	ComponentStatus status;

	/**
	 * Check if the component is registered.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public HealthCheckResponse call() {

		if (this.status.isRegistered()) {

			return HealthCheckResponse.up(NAME);

		} else {

			return HealthCheckResponse.down(NAME);
		}
	}

}
