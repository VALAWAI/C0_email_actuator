/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
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
