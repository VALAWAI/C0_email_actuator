/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator.mov;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.Random;

import org.junit.jupiter.api.Test;

import io.vertx.core.json.Json;

/**
 * Generic test over the classes that extends the {@link ReflectionModel}.
 *
 * @see ReflectionModel
 *
 * @param <T> type of model to test.
 *
 * @author VALAWAI
 */
public abstract class ReflectionModelTestCase<T extends ReflectionModel> {

	/**
	 * The component to generate random values.
	 */
	public static final Random rnd = new Random(0);

	/**
	 * Create a new empty model.
	 *
	 * @return the created empty model.
	 */
	public abstract T createEmptyModel();

	/**
	 * Create a new model.
	 *
	 * @return the created a random next model.
	 */
	public final T nextModel() {

		final var model = this.createEmptyModel();
		this.fillIn(model);
		return model;
	}

	/**
	 * Fill in a model.
	 *
	 * @param model to fill in.
	 */
	public abstract void fillIn(T model);

	/**
	 * Check that can encode decode form Json.
	 */
	@Test
	public void shouldEncodeDecodeJson() {

		final var source = this.nextModel();
		final var encoded = Json.encode(source);
		final var target = Json.decodeValue(encoded, source.getClass());
		assertEquals(target, source);

		assertEquals(target.toString(), source.toString());

		assertEquals(target.hashCode(), source.hashCode());
	}

	/**
	 * Check that to models are equals.
	 */
	@Test
	public void shouldBeEquals() {

		final var source = this.nextModel();
		assertEquals(source, source);

	}

	/**
	 * Check that the string of two different models are not equals.
	 */
	@Test
	public void shouldStringNotBeEquals() {

		final var source = this.nextModel();
		final var target = this.createEmptyModel();
		assertNotEquals(source, target);

	}

	/**
	 * Check that to models are not equals.
	 */
	@Test
	public void shouldNotBeEquals() {

		final var source = this.nextModel();
		final var target = this.createEmptyModel();
		assertNotEquals(source, target);

	}

	/**
	 * Check that to models are equals.
	 */
	@Test
	public void shouldToStringNotBeEquals() {

		final var source = this.nextModel();
		final var target = this.createEmptyModel();
		assertNotEquals(target.toString(), source.toString());

	}

	/**
	 * Check that to models are equals.
	 */
	@Test
	public void shouldToHashCodeNotBeEquals() {

		final var source = this.nextModel();
		final var target = this.createEmptyModel();
		assertNotEquals(target.hashCode(), source.hashCode());

	}

}
