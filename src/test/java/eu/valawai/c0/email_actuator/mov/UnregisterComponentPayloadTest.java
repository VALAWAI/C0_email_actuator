/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator.mov;

/**
 * Test the {@link UnregisterComponentPayload}.
 *
 * @see UnregisterComponentPayload
 *
 * @author VALAWAI
 */
public class UnregisterComponentPayloadTest extends PayloadTestCase<UnregisterComponentPayload> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UnregisterComponentPayload createEmptyModel() {

		return new UnregisterComponentPayload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(UnregisterComponentPayload payload) {

		payload.component_id = "component_" + rnd.nextLong();
	}

}
