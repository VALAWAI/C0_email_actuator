/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0.email_actuator.mov;

import jakarta.enterprise.context.ApplicationScoped;

/**
 * Maintains the status of the component.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@ApplicationScoped
public class ComponentStatus {

	/**
	 * The identifier of the registered component.
	 */
	protected String componentId;

	/**
	 * Check if the component is registered.
	 *
	 * @return {@code true} if the component is registered.
	 */
	public boolean isRegistered() {

		return this.componentId != null;
	}

	/**
	 * Obtain the identifier of associated to the component when it has been
	 * registered.
	 *
	 * @return the registered identifier, or {@code null} if it is not registered.
	 */
	public String getRegisteredId() {

		return this.componentId;
	}

	/**
	 * Called when the component has been registered.
	 *
	 * @param id identifier of the component.
	 */
	public void registered(String id) {

		this.componentId = id;
	}
}
