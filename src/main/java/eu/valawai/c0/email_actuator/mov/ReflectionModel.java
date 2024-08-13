/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator.mov;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A model that implements the basic operation by reflection.
 *
 * @author VALAWAI
 */
public class ReflectionModel {

	/**
	 * Reflections equals.
	 *
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 * @see EqualsBuilder#reflectionEquals(Object, Object,String...)
	 */
	@Override
	public boolean equals(final Object obj) {

		return EqualsBuilder.reflectionEquals(this, obj);

	}

	/**
	 * Reflection hash code.
	 *
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#hashCode()
	 * @see HashCodeBuilder#reflectionHashCode(Object, String...)
	 */
	@Override
	public int hashCode() {

		return HashCodeBuilder.reflectionHashCode(this);

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 * @see ToStringBuilder#reflectionToString(Object)
	 */
	@Override
	public String toString() {

		return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);

	}

}
