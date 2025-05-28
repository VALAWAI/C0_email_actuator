# C0_email_actuator

The E-mail actuator (C0) is designed to send e-mails outside the VALAWAI infrastructure. It converts messages
received from other VALAWAI components into outgoing e-mails.

## Summary

 - **Type**: [C0](https://valawai.github.io/docs/components/C0/)
 - **Name**: E-mail actuator
 - **Documentation**: [https://valawai.github.io/docs/components/C0/email_actuator](https://valawai.github.io/docs/components/C0/email_actuator)
 - **Versions**:
    - **Stable version**: [1.2.0 (May 21, 2025)](https://github.com/VALAWAI/C0_email_actuator/tree/1.2.0)
    - **API**: [1.0.0 (August 16, 2024)](https://raw.githubusercontent.com/VALAWAI/C0_email_actuator/ASYNCAPI_1.0.0/asyncapi.yml)
    - **Required MOV API**: [1.2.0 (March 9, 2024)](https://raw.githubusercontent.com/valawai/MOV/ASYNCAPI_1.2.0/asyncapi.yml)
 - **Developed By**: [IIIA-CSIC](https://www.iiia.csic.es)
 - **License**: [GPL v3](LICENSE)
 - **Technology Readiness Level (TLR)**: [3](https://valawai.github.io/docs/components/C0/email_actuator/tlr)

## Usage

This component can be used to send e-mails outside the value-aware infrastructure.

## Deployment

This component is engineered for deployment as a Docker container, primarily fitting into the ecosystem of
the [Master Of VALAWAI (MOV)](https://valawai.github.io/docs/architecture/implementations/mov) which serves
as a centralized implementation for the VALAWAI architecture. While a comprehensive and in-depth deployment
guide, covering all intricacies and advanced configurations, is readily available in the
[component's dedicated deployment documentation](https://valawai.github.io/docs/components/C0/email_actuator/deploy),
the subsequent step-by-step guide is designed to provide you with the fundamental, essential steps required
to quickly get this component operational.

 1. **Building the Docker Image**

    First, you need to create the Docker image for the component. Navigate to the project's root directory
    and execute the following script:

    ```bash
    ./buildDockerImages.sh -t latest
    ```

    This command generates the `valawai/c0_email_actuator:latest` Docker image, which is referenced
    in the project's [](docker-compose.yml) file.

 2. **Starting the Component**

    You have two primary options for starting the component:

       A. **With Master Of VALAWAI(MOV) and a Mail Catcher**  To start this component
        alongside the MOV and a local email catcher for testing, use the following
        Docker Compose command:
  
       ```bash
       COMPOSE_PROFILES=all docker compose up -d
       ```
  
       After these services are up, you can access:

       - The MOV at [http://localhost:8081](http://localhost:8081)
       - The RabbitMQ user interface at [http://localhost:8082](http://localhost:8082) (credentials: `mov:password`)
       - The mail catcher user interface at [http://localhost:8083](http://localhost:8083)

       B. **As a Standalone Component (Connecting to an Existing MOV/RabbitMQ)** If your
        MOV instance is already running or you wish to connect to a remote RabbitMQ broker, you'll need to define a
        [.env](https://docs.docker.com/compose/environment-variables/env-file/) file
        to provide the necessary connection details. Create a file named `.env`
        in the same directory as your `docker-compose.yml` with content similar to this:
  
       ```properties
       MQ_HOST=host.docker.internal
       MQ_USERNAME=mov
       MQ_PASSWORD=password
       C0_EMAIL_ACTUATOR_PORT=9080
       MAIL_WEB=9083
       ```
  
       The specific meanings and possible values for these and other related variables
       are explained in detail in the
       [component's dedicated deployment documentation](https://valawai.github.io/docs/components/C0/email_actuator/deploy).
  
       Once your `.env` file is configured, you can start only the email actuator
       component and the mail catcher (without MOV) using this command:
  
       ```bash
       COMPOSE_PROFILES=mail,component docker compose up -d
       ```

 3. **Stopping All Containers**

    To stop all containers started with the mov,mail profiles, execute the following command:

    ```bash
    COMPOSE_PROFILES=all docker compose down
    ```

    This will stop the MOV, RabbitMQ and the mail catcher containers.

## Development environment

To ensure a consistent and isolated development experience, this component is configured
to use Docker. This approach creates a self-contained environment with all the necessary
software and tools for building and testing, minimizing conflicts with your local system
and ensuring reproducible results.

You can launch the development environment by running this script:

```bash
./startDevelopmentEnvironment.sh
```

Once the environment starts, you'll find yourself in a bash shell, ready to interact with
the Quarkus development environment. You'll also have access to the following integrated tools:

- **Master of VALAWAI**: The central component managing topology connections between services.
 Its web interface is accessible at [http://localhost:8081](http://localhost:8081).
- **RabbitMQ** The message broker for inter-component communication. The management web interface
 is at [http://localhost:8082](http://localhost:8082), with credentials `mov**:**password`.
- **MongoDB**: The database used by the MOV, named `movDB`, with user credentials `mov:password`.
- **Mongo express**: A web interface for interacting with MongoDB, available at
 [http://localhost:8084](http://localhost:8084), also with credentials `mov**:**password`.
- **Mail catcher**A tool to capture and inspect sent emails. Its web interface is at
  [http://localhost:8083](http://localhost:8083).

Within this console, you can use the official [`quarkus` client](https://quarkus.io/guides/cli-tooling#using-the-cli)
or any of these convenient commands:

- `startServer`: To initiate the development server.
- `mvn clean`: To clean the project (compiled and generated code).
- `mvn test`: To run all project tests.
- `mvn -DuseDevMOV=true test`: To execute tests using the already started Master of VALAWAI instance,
 rather than an independent container.
  
To exit the development environment, simply type `exit` in the bash shell or run the following script:

```bash
./stopDevelopmentEnvironment.sh
```

In either case, the development environment will gracefully shut down, including all activated services
like MOV, RabbitMQ, MongoDB, Mongo Express, and the Mail Catcher.

## Helpful Links

Here's a collection of useful links related to this component and the VALAWAI ecosystem:

- **C0 E-mail Actuator Documentation**: [https://valawai.github.io/docs/components/C0/email_actuator](https://valawai.github.io/docs/components/C0/email_actuator)
- **Master Of VALAWAI (MOV)**: [https://valawai.github.io/docs/architecture/implementations/mov/](https://valawai.github.io/docs/architecture/implementations/mov/)
- **VALAWAI Main Documentation**: [https://valawai.github.io/docs/](https://valawai.github.io/docs/)
- **VALAWAI on GitHub**: [https://github.com/VALAWAI](https://github.com/VALAWAI)
- **VALAWAI Official Website**: [https://valawai.eu/](https://valawai.eu/)
- **VALAWAI on X (formerly Twitter)**: [https://x.com/ValawaiEU](https://x.com/ValawaiEU)
