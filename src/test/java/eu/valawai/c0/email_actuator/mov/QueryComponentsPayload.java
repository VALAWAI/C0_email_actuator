/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator.mov;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonRootName;

/**
 * The information necessary to query for some components.
 *
 * @author VALAWAI
 */
@JsonRootName("query_components_payload")
public class QueryComponentsPayload extends Payload {

	/**
	 * The identifier of the query.
	 */
	public String id = UUID.randomUUID().toString();

	/**
	 * The pattern to match the name or description of the components to return. If
	 * it is defined between / it is considered a PCRE regular expression.
	 */
	public String pattern = "c0_email_actuator";

	/**
	 * The type to match the components to return. If it is defined between / it is
	 * considered a PCRE regular expression.
	 */
	public String type = "C0";

	/**
	 * The order in witch the components has to be returned. It is form by the field
	 * names, separated by a comma, and each of it with the - prefix for descending
	 * order or + for ascending.
	 */
	public String order = "-since";

	/**
	 * The index of the first component to return.
	 */
	public int offset = 0;

	/**
	 * The maximum number of components to return.
	 */
	public int limit = 1;

}
