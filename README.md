# C0_email_actuator

The C0 e-mail actuator component send e-mails that it has received from
the VALAWAI infrastructure. Thus, this component fetches the e-mails of a user
account in an email server, normalizes them and publishes them in the channel
**valawai/c0/email_actuator/data/e_mail**. Also, it provides the possibility to change
the component parameters if you send a message to the queue 
**valawai/c0/email_actuator/control/change_parameters**. You can read more about
this service and the payload of the message on the [aysncapi](asyncapi.yaml)
or on the [component documentation](https://valawai.github.io/docs/components/C0/email_actuator).


## Summary

 - Type: C0
 - Name: E-mail actuator
 - API: [1.0.0 (August 14, 2024)](https://raw.githubusercontent.com/VALAWAI/C0_email_actuator/ASYNCAPI_1.0.0/asyncapi.yml)
 - VALAWAI API: [1.2.0 (March 9, 2024)](https://raw.githubusercontent.com/valawai/MOV/ASYNCAPI_1.2.0/asyncapi.yml)
 - Developed By: [IIIA-CSIC](https://www.iiia.csic.es)
 - License: [GPL 3](LICENSE)
 
 
## Generate Docker image

The easy way to create the docker image of this component is to execute
the next script.
 
 ```
./buildDockerImages.sh
```

At the end you must have the docker image **valawai/c0_email_actuator:Z.Y.Z**
where **X.Y.Z** will be the version of the component. If you want to have
the image with another tag, for example **latest**, you must call the script
with this tag as a parameter, for example:

```
./buildDockerImages.sh latest
```

And you will obtain the container **valawai/c0_email_actuator:latest**.

The most useful environment variables on the docker image are:

 - **RABBITMQ_HOST** is the host where the RabbitMQ is available.
  The default value is ___mov-mq___.
 - **RABBITMQ_PORT** defines the port of the RabbitMQ.
  The default value is ___5672___.
 - **RABBITMQ_USERNAME** contains the name of the user that can access the RabbitMQ.
  The default value is ___mov___.
 - **RABBITMQ_PASSWORD** is the password to authenticate the user that can access the RabbitMQ.
  The default value is ___password___.
 - **MAIL_PROTOCOL** defines the protocol to connect to the e-mail server to fetch the e-mails.
  It can be **pop3**, **pop3s**, **imap** or **imap2**. The default value is ___imaps___.
 - **MAIL_HOST** is the host to the e-mail server. The default value is ___mail___.
 - **MAIL_PORT** defines the port of the e-mail server. The default value is ___993___.
 - **MAIL_SECURED** this is **true** is the connection uses the secured TLS.
  The default value is ___true___.
 - **MAIL_USERNAME** contains the name of the user that can access the e-mail server.
  The default value is ___user___.
 - **MAIL_PASSWORD** defines the credential to authenticate the user that can access the e-mail server.
  The default value is ___password__.
 - **C0_EMAIL_ACTUATOR_FETCHING_INTERVAL** contains the seconds that have to pass between fetching e-mail
  intervals. The default value is ___60__.
 - **LOG_LEVEL** defines the level of the log messages to be stored.
  The default value is ___INFO__.

The component is developed using [Quarkus](https://quarkus.io/), so you can change any environment
variable [defined on it](https://quarkus.io/guides/all-config).

## Deploy

On the file [docker-compose.yml](docker-compose.yml), you can see how the docker image
of this component can be deployed on a valawai environment. On this file are defined
the profiles **mov** and **mail**. The first one is to launch
the [Master Of Valawai (MOV)](https://github.com/VALAWAI/MOV) and the second one is to start
a [mocked e-mail server](https://github.com/dbck/docker-mailtrap). You can use the next
command to start this component with the MOV and the mail server.

```
COMPOSE_PROFILES=mov,mail docker compose up -d
```

After that, if you open a browser and go to [http://localhost:8080](http://localhost:8080)
you can view the MOV user interface. Also, you can access the RabbitMQ user interface
at [http://localhost:8081](http://localhost:8081). Finally, you can access the mail server
user interface at [http://localhost:8082](http://localhost:8082). 
one are **mov:password**.

The docker compose defines some variables that can be modified by creating a file named
[**.env**](https://docs.docker.com/compose/environment-variables/env-file/) where 
you write the name of the variable plus equals plus the value.  As you can see in
the next example.

```
MQ_HOST=rabbitmq.valawai.eu
MQ_USERNAME=c0_email_actuator
MQ_PASSWORD=lkjagb_ro82t¿134
```

The defined variables are:


 - **C0_EMAIL_ACTUATOR_TAG** is the tag of the C0 email actuator docker image to use.
  The default value is ___latest___.
 - **MQ_HOST** is the hostname of the message queue broker that is available.
  The default value is ___mq___.
 - **MQ_PORT** is the port of the message queue broker is available.
  The default value is ___5672___.
 - **MQ_UI_PORT** is the port of the message queue broker user interface is available.
  The default value is ___8081___.
 - **MQ_USER** is the name of the user that can access the message queue broker.
  The default value is ___mov___.
 - **MQ_PASSWORD** is the password to authenticate the user that can access the message queue broker.
  The default value is ___password___.
 - **MAIL_PROTOCOL** defines the protocol to connect to the e-mail server to fetch the e-mails.
  It can be **pop3**, **pop3s**, **imap** or **imap2**. The default value is ___imaps___.
 - **MAIL_HOST** is the host to the e-mail server. The default value is ___mail___.
 - **MAIL_PORT** defines the port of the e-mail server. The default value is ___993___.
 - **MAIL_SECURED** this is **true** is the connection uses the secured TLS.
  The default value is ___true___.
 - **MAIL_USERNAME** contains the name of the user that can access the e-mail server.
  The default value is ___user___.
 - **MAIL_PASSWORD** defines the credential to authenticate the user that can access the e-mail server.
  The default value is ___password__.
 - **MAILTRAP_TAG** is the tag of the [email server](https://github.com/dbck/docker-mailtrap) docker image to use.
  The default value is ___latest___.
 - **RABBITMQ_TAG** is the tag of the RabbitMQ docker image to use.
  The default value is ___management___.
 - **MONGODB_TAG** is the tag of the MongoDB docker image to use.
  The default value is ___latest___.
 - **MONGO_PORT** is the port where MongoDB is available.
  The default value is ___27017___.
 - **MONGO_ROOT_USER** is the name of the root user for the MongoDB.
  The default value is ___root___.
 - **MONGO_ROOT_PASSWORD** is the password of the root user for the MongoDB.
  The default value is ___password___.
 - **MONGO_LOCAL_DATA** is the local directory where the MongoDB will be stored.
  The default value is ___~/mongo_data/movDB___.
 - **DB_NAME** is the name of the database used by the MOV.
  The default value is ___movDB___.
 - **DB_USER_NAME** is the name of the user used by the MOV to access the database.
  The default value is ___mov___.
 - **DB_USER_PASSWORD** is the password of the user used by the MOV to access the database.
  The default value is ___password___.
 - **MOV_TAG** is the tag of the MOV docker image to use.
  The default value is ___latest___.
 - **MOV_UI_PORT** is the port where the MOV user interface is available.
  The default value is ___8080___.

The database is only created the first time where script is called. So, if you modify
any of the database parameters you must create again the database. For this, you must
remove the directory defined by the parameter **MONGO_LOCAL_DATA** and start again
the **docker compose**.

You can stop all the started containers with the command:

```
COMPOSE_PROFILES=mov,mail docker compose down
``` 
 
 
## Development

You can start the development environment with the script:

```shell script
./startDevelopmentEnvironment.sh
```

After that, you have a bash shell where you can interact with
the Quarkus development environment. You can start the development
server with the command:

```shell script
startServer
```

Alternatively, to run the test using the started Quarkus client, you can use Maven.

 * __mvn test__  to run all the tests
 * __mvnd test__  to run all the tests on debugging mode.
 * __mvn -DuseDevMOV=true test__  to run all the tests using the started Master of VALAWAI,
 	instead of an independent container.

Also, this starts the tools:

 * __RabbitMQ__  the server to manage the messages to interchange with the components.
 The management web interface can be opened at **http://localhost:8081** with the credential
 **mov**:**password**.
 * __MongoDB__  the database to store the data used by the MOV. The database is named as **movDB** and the user credentials **mov:password**.
 The management web interface can be opened at **http://localhost:8081** with the credential
 **mov**:**password**.
 * __Mongo express__  the web interface to interact with the MongoDB. The web interface
  can be opened at **http://localhost:8082**.
 * __Mail catcher__  the component that capture the sent mails.
  The web interface can be opened at **http://localhost:8083**.
 * __Master of VALAWAI__  the component that mantains the topology connections between components.
  The web interface can be opened at **http://localhost:8084**.


## Links

 - [C0 E-mail actuator documentation](https://valawai.github.io/docs/components/C0/email_actuator)
 - [Master Of VALAWAI tutorial](https://valawai.github.io/docs/tutorials/mov)
 - [VALWAI documentation](https://valawai.github.io/docs/)
 - [VALAWAI project web site](https://valawai.eu/)
 - [Twitter](https://twitter.com/ValawaiEU)
 - [GitHub](https://github.com/VALAWAI)