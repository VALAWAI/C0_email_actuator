/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator.mov;

/**
 * Test the {@link RegisterComponentPayload}.
 *
 * @see RegisterComponentPayload
 *
 * @author VALAWAI
 */
public class RegisterComponentPayloadTest extends PayloadTestCase<RegisterComponentPayload> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RegisterComponentPayload createEmptyModel() {

		return new RegisterComponentPayload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(RegisterComponentPayload payload) {

		payload.version = rnd.nextInt(0, 10) + "." + rnd.nextInt(0, 100) + "." + rnd.nextInt(0, 100);
	}

}
