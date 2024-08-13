/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0.email_actuator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import eu.valawai.c0.email_actuator.mov.MOVTestResource;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * Test the {@link EMailActuator}.
 *
 * @see EMailActuator
 *
 * @author UDT-IA, IIIA-CSIC
 */
@QuarkusTest
@WithTestResource(value = MOVTestResource.class)
public class EMailActuatorTest {

	/**
	 * The amilbox where are send the e-mails.
	 */
	@Inject
	MockMailbox mailbox;

	/**
	 * Clear the mailbox before run a test.
	 */
	@BeforeEach
	public void init() {

		this.mailbox.clear();
	}

	/**
	 * The component used to send the e-mail thought the rabbitMQ.
	 */
	@Channel("send_email")
	@Inject
	Emitter<EMailPayload> emailEmitter;

	/**
	 * Check not send e-mail if it is not valid.
	 */
	@Test
	public void shouldNotSendInvalidEMailMessage() {

		final var eMail = new EMailPayload();
		this.assertSendEMail(eMail);

		for (var i = 0; i < 10; i++) {

			assertEquals(0, this.mailbox.getTotalMessagesSent());

			try {
				Thread.sleep(Duration.ofSeconds(1));
			} catch (final InterruptedException ignored) {
			}
		}

	}

	/**
	 * Check that an email has been sent.
	 *
	 * @param eMail that has to be sent.
	 */
	protected void assertSendEMail(EMailPayload eMail) {

		final var semaphore = new Semaphore(0);
		final var errors = new ArrayList<Throwable>();
		this.emailEmitter.send(eMail).handle((success, error) -> {

			if (error != null) {

				errors.add(error);
			}
			semaphore.release();
			return null;
		});

		try {
			semaphore.tryAcquire(30, TimeUnit.SECONDS);
		} catch (final InterruptedException ignored) {
		}

		assertTrue(errors.isEmpty());

	}

	/**
	 * Check send -email.
	 */
	@Test
	public void shouldSendEMail() {

		final var eMail = new EMailPayloadTest().nextModel();
		this.assertSendEMail(eMail);

		final var max = System.currentTimeMillis() + Duration.ofSeconds(30).toMillis();
		while (this.mailbox.getTotalMessagesSent() == 0 && System.currentTimeMillis() < max) {

			assertEquals(0, this.mailbox.getTotalMessagesSent());

			try {
				Thread.sleep(Duration.ofSeconds(1));
			} catch (final InterruptedException ignored) {
			}
		}

		assertTrue(this.mailbox.getTotalMessagesSent() > 0);
		final var receiverAddress = eMail.addresses.get(0).encode();
		final var mails = this.mailbox.getMailMessagesSentTo(receiverAddress);
		assertEquals(1, mails.size());
		final var mail = mails.get(0);
		assertEquals(eMail.content, mail.getText());
		for (final var address : eMail.addresses) {

			final List<String> expectedAddress = switch (address.type) {
			case TO -> mail.getTo();
			case BCC -> mail.getBcc();
			default -> mail.getCc();
			};

			assertTrue(expectedAddress.contains(address.encode()));
		}

	}

}
