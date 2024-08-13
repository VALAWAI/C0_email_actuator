/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import eu.valawai.c0.email_actuator.mov.PayloadTestCase;

/**
 * Test the {@link EMailAddressPayload}.
 *
 * @see EMailAddressPayload
 *
 * @author VALAWAI
 */
public class EMailAddressPayloadTest extends PayloadTestCase<EMailAddressPayload> {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public EMailAddressPayload createEmptyModel() {

		return new EMailAddressPayload();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void fillIn(EMailAddressPayload payload) {

		final var values = EMailAddressType.values();
		payload.type = values[rnd.nextInt(0, values.length)];
		final var userId = rnd.nextInt(0, 1000000);
		payload.name = "User name " + userId;
		payload.address = "user_" + userId + "@valawai.eu";
	}

	/**
	 * Should decode payload from an address.
	 *
	 * @param value of the address.
	 * @param name  expected payload name.
	 * @param email expected payload address.
	 */
	@ParameterizedTest(name = "Should obtain the payload from {0}")
	@CsvSource(textBlock = """
			bob smith <bob@example.com>, bob smith, bob@example.com
			bob smith <bob@example.com> (Bobby), bob smith (Bobby), bob@example.com
			<bob@example.com> (Bobby), (Bobby), bob@example.com
			bob@example.com, , bob@example.com
			<bob@example.com>, , bob@example.com
			     <bob@example.com>     , , bob@example.com
			         <bob@example.com>      (Bobby)    , (Bobby), bob@example.com
			             bob smith       <bob@example.com>      (Bobby)      , bob smith (Bobby), bob@example.com
			""")
	public void shouldDecode(String value, String name, String email) {

		final var payload = EMailAddressPayload.decode(value);
		assertNotNull(payload);
		assertEquals(name, payload.name);
		assertEquals(email, payload.address);

		final var encoded = payload.encode();
		assertNotNull(encoded);
		final var decoded = EMailAddressPayload.decode(encoded);
		assertEquals(payload, decoded);
	}

	/**
	 * Should not decode {@code null}.
	 */
	@Test
	public void shouldDecodeNull() {

		assertNull(EMailAddressPayload.decode(null));

	}

}
