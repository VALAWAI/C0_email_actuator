/*
  Copyright 2022-2026 VALAWAI

  Use of this source code is governed by GNU General Public License version 3
  license that can be found in the LICENSE file or at
  https://opensource.org/license/gpl-3-0/
*/

package eu.valawai.c0.email_actuator.mov;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.annotation.JsonRootName;

import io.quarkus.logging.Log;
import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * The information necessary to register a component.
 *
 * @author VALAWAI
 */
@RegisterForReflection
@JsonRootName("register_component_payload")
public class RegisterComponentPayload extends Payload {

	/**
	 * The type of the component to register.
	 */
	public String type = "C0";

	/**
	 * The name of the component to register.
	 */
	public String name = "c0_email_actuator";

	/**
	 * The version of the component.
	 */
	public String version;

	/**
	 * The asyncapi specification in yaml.
	 */
	public String asyncapi_yaml = loadAsyncAPI();

	/**
	 * Load the resource with the asyncaoi.
	 *
	 * @return the asyncapi of the component or {@code null} if it is not defined.
	 */
	private static final String loadAsyncAPI() {

		try {

			final var loader = RegisterComponentPayload.class.getClassLoader();
			final var resource = loader.getResourceAsStream("asyncapi.yaml");
			final var bytes = resource.readAllBytes();
			return new String(bytes, StandardCharsets.UTF_8);

		} catch (final Throwable tryAgain) {

			try {

				return Files.readString(Path.of("asyncapi.yaml"));

			} catch (final Throwable error) {

				Log.errorv(error, "Cannot obtain the asyncapi.yaml");
				return null;
			}
		}
	}

}
