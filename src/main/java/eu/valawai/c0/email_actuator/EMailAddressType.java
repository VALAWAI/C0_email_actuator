/*
  Copyright 2024 UDT-IA, IIIA-CSIC

  Use of this source code is governed by an MIT-style
  license that can be found in the LICENSE file or at
  https://opensource.org/licenses/MIT.
*/

package eu.valawai.c0.email_actuator;

/**
 * The possible types of e-mail addresses.
 *
 * @author UDT-IA, IIIA-CSIC
 */
public enum EMailAddressType {

	/**
	 * The address refers to someone that has to receive the e-mail.
	 */
	TO,

	/**
	 * The address refers to someone that has to receive a copy of the e-mail.
	 */
	CC,

	/**
	 * The address refers to someone that has to receive a blind copy of the e-mail.
	 */
	BCC;

}
