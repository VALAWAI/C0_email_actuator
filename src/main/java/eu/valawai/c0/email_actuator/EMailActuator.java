/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator;

import java.util.concurrent.CompletionStage;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;

import eu.valawai.c0.email_actuator.mov.LogService;
import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

/**
 * The component that is used to send the e-mails.
 *
 * @author UDT-IA, IIIA-CSIC
 */
@ApplicationScoped
public class EMailActuator {

	/**
	 * The service to send e-mails.
	 */
	@Inject
	ReactiveMailer mailer;

	/**
	 * The component to send log messages.
	 */
	@Inject
	LogService log;

	/**
	 * The component to validate a {@code Payload}.
	 */
	@Inject
	Validator validator;

	/**
	 * Called when a has to send an e-mail.
	 *
	 * @see EMailPayload
	 *
	 * @param msg with the e-mail to send.
	 *
	 * @return the result if the message process.
	 */
	@Incoming("receive_email")
	public CompletionStage<Void> sendEMail(Message<JsonObject> msg) {

		try {

			final var payload = msg.getPayload();
			final var emailToSend = payload.mapTo(EMailPayload.class);
			final var violations = this.validator.validate(emailToSend);
			if (violations.isEmpty()) {

				final var mail = new Mail();
				for (final var address : emailToSend.addresses) {

					final var encoded = address.encode();
					switch (address.type) {

					default -> mail.addTo(encoded);
					case BCC -> mail.addBcc(encoded);
					case CC -> mail.addCc(encoded);
					}
				}
				mail.setSubject(emailToSend.subject);
				if (emailToSend.is_html) {

					mail.setHtml(emailToSend.content);

				} else {

					mail.setText(emailToSend.content);
				}

				return this.mailer.send(mail).onItemOrFailure().transform((any, error) -> {

					return error;

				}).subscribeAsCompletionStage().thenCompose(error -> {

					if (error != null) {

						this.log.errorWithPayload(emailToSend, "Cannot send the e-mail, because {0}", error);
						return msg.nack(error);

					} else {

						this.log.infoWithPayload(emailToSend, "Sent the email.");
						return msg.ack();
					}
				});

			} else {

				this.log.errorWithPayload(payload, "Bad change parameters message, because {0}", violations);
				return msg.nack(new ConstraintViolationException(violations));

			}

		} catch (final Throwable error) {

			Log.errorv(error, "Unexpected change parameters message {0}.", msg.getPayload());
			this.log.errorWithPayload(msg.getPayload(), "Bad change parameters message, because {0}",
					error.getMessage());
			return msg.nack(error);

		}
	}

}
