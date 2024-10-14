/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator;

import java.util.List;

import eu.valawai.c0.email_actuator.mov.Payload;
import jakarta.validation.constraints.NotEmpty;

/**
 * The payload that contains the information of an e-mail.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public class EMailPayload extends Payload {

	/**
	 * The address of the people that has send or receive the e-mail.
	 */
	@NotEmpty
	public List<EMailAddressPayload> addresses;

	/**
	 * The subject of the e-mail.
	 */
	public String subject;

	/**
	 * This is {@code true} if the content is HTML.
	 */
	public boolean is_html = false;

	/**
	 * The content of the e-mail.
	 */
	@NotEmpty
	public String content;

}
