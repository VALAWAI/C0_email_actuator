/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0.email_actuator;

import static eu.valawai.c0.email_actuator.mov.ReflectionModelTestCase.rnd;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
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
import io.vertx.core.json.JsonObject;
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
	 * The component used to send the e-mail thought the rabbitMQ.
	 */
	@Channel("send_email")
	@Inject
	Emitter<JsonObject> messageEmitter;

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
	 * Check not process bad e-mail message.
	 */
	@Test
	public void shouldNotProcessInvalidMessage() {

		this.assertEmit(new JsonObject().put("content", Collections.emptyList()), this.messageEmitter);
		;
		for (var i = 0; i < 10; i++) {

			assertEquals(0, this.mailbox.getTotalMessagesSent());

			try {
				Thread.sleep(Duration.ofSeconds(1));
			} catch (final InterruptedException ignored) {
			}
		}

	}

	/**
	 * Check that something is emitted.
	 */
	protected <T> void assertEmit(T value, Emitter<T> emitter) {

		final var semaphore = new Semaphore(0);
		final var errors = new ArrayList<Throwable>();
		emitter.send(value).handle((success, error) -> {

			if (error != null) {

				errors.add(error);
			}
			semaphore.release();
			return null;
		});

		try {

			assertTrue(semaphore.tryAcquire(30, TimeUnit.SECONDS), "not emitted an event in 30 seconds");

		} catch (final InterruptedException ignored) {
		}

		assertTrue(errors.isEmpty());

	}

	/**
	 * Check that an email has been sent.
	 *
	 * @param eMail that has to be sent.
	 */
	protected void assertSendEMail(EMailPayload eMail) {

		this.assertEmit(eMail, this.emailEmitter);

	}

	/**
	 * Wait until a mail is received.
	 */
	protected void waitUntilSentAtLeastOneMessage() {

		final var max = System.currentTimeMillis() + Duration.ofSeconds(30).toMillis();
		while (this.mailbox.getTotalMessagesSent() == 0 && System.currentTimeMillis() < max) {

			assertEquals(0, this.mailbox.getTotalMessagesSent());

			try {
				Thread.sleep(Duration.ofSeconds(1));
			} catch (final InterruptedException ignored) {
			}
		}

		assertTrue(this.mailbox.getTotalMessagesSent() > 0, "Not sent a message in the last 30 seconds");

	}

	/**
	 * Check send email to someone.
	 */
	@Test
	public void shouldSendEMailToSomeone() {

		final var eMail = new EMailPayload();
		eMail.addresses = new ArrayList<>();
		final var to = new EMailAddressPayloadTest().nextModel();
		to.type = EMailAddressType.TO;
		eMail.addresses.add(to);
		eMail.is_html = false;
		eMail.subject = "Subject " + rnd.nextInt(0, 1000000);
		eMail.content = "Content " + rnd.nextInt(0, 1000000);

		this.assertSendEMail(eMail);
		this.waitUntilSentAtLeastOneMessage();

		final var mails = this.mailbox.getMailMessagesSentTo(to.encode());
		assertEquals(1, mails.size());
		final var mail = mails.get(0);
		assertEquals(eMail.subject, mail.getSubject());
		assertEquals(eMail.content, mail.getText());
		assertEquals(Collections.emptyList(), mail.getBcc());
		assertEquals(Collections.emptyList(), mail.getCc());

	}

	/**
	 * Check send email to someone and a copy to others.
	 */
	@Test
	public void shouldSendEMailToSomeoneAndCopyToOthers() {

		final var eMail = new EMailPayload();
		eMail.addresses = new ArrayList<>();
		final var to = new EMailAddressPayloadTest().nextModel();
		to.type = EMailAddressType.TO;
		eMail.addresses.add(to);
		final var cc = new EMailAddressPayloadTest().nextModel();
		cc.type = EMailAddressType.CC;
		eMail.addresses.add(cc);
		final var bcc = new EMailAddressPayloadTest().nextModel();
		bcc.type = EMailAddressType.BCC;
		eMail.addresses.add(bcc);
		eMail.is_html = true;
		eMail.subject = "Subject " + rnd.nextInt(0, 1000000);
		eMail.content = "<h1>Content " + rnd.nextInt(0, 1000000) + "</h1>";

		this.assertSendEMail(eMail);
		this.waitUntilSentAtLeastOneMessage();

		final var mails = this.mailbox.getMailMessagesSentTo(bcc.encode());
		assertEquals(1, mails.size());
		final var mail = mails.get(0);
		assertEquals(eMail.subject, mail.getSubject());
		assertEquals(eMail.content, mail.getHtml());
		assertNotNull(mail.getCc());
		assertTrue(mail.getCc().contains(cc.encode()));
		assertNotNull(mail.getBcc());
		assertTrue(mail.getBcc().contains(bcc.encode()));

	}

}
