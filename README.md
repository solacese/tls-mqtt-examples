# tls-mqtt-examples

Basic secure MQTT examples for TLS and certificate authentication 
using the Paho mqttv3 client library. I created this because a 
saw many Paho MQTT samples but very few addressed TLS and secure 
links.

These samples require an MQTT Event Broker that supports TLS and
client certificate authentication like a [Solace PubSub+ Event
Broker](http://solace.com). You should have a server certificate 
installed and access to the CA-Certificate that signed your 
server certificate.

Samples are provided in the `resources` directory but these are 
just for unit-testing. They will not work in your environment 
because they lack a server signed by the sampleCA and matching 
that server's FQDN.

## Java examples

The java code is built via maven, recommend producing an all-in-one
jar with dependencies for easier commandline execution:

```bash
bash% mvn clean compile assembly:single
...
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
bash% java -jar target/tls-mqtt-examples-1.0-SNAPSHOT-jar-with-dependencies.jar

	ARGS: <connection-string> <username> <password> <ca-cert-file> <keystore-file> <keystore-password>

	For example: ssl://localhost:8883 mqttclient $up3r$3cr3t resources/sampleCA.pem resources/mqttclient.keystore mq77rul35
```

### `com/solace/poc/TlsClientApp.java`

A basic TLS test that uses a CA-cert to connect to a server and establish 
a valid secure TLS session.

### `com/solace/poc/ClientCertAuthApp.java`

A  TLS test that uses a CA-cert to connect to a server and establish 
a valid secure TLS session, and a client certificate to authenticate 
to that server. The client certificate must be signed by a valid 
Certificate Authority known by the server.

For details about how to install a CA certificate to a Solace broker 
[see this documentation](https://docs.solace.com/Configuring-and-Managing/Managing-Certificate-Authorities.htm#Configur2).

## Python examples

The python code was tested in Linux environments, so the scripts
aren't tested on Windows.  All scripts are in `src/main/python/`.
They all execute the sample pub/sub loop of 10 messages, but ramp
up in setup complexity.

All examples hardcode variables and arguments within the scripts.
Change them to your environment and configurations.

### `src/main/python/simple_pubsub.py`

A basic connectivity, no TLS or certificates required. Good for validating your basic setup.

### `src/main/python/simple_tls_pubsub.py`

TLS connectivity with server-checks, so you need to have a MQTT Event Broker that supports TLS with a valid server-certificate installed.

### `src/main/python/certauth_tls_pubsub.py`

Full TLS connectivity with client certificate authentication. Requires an MQTT Event Broker that supports client certificate authentication.

## References

[Note there's also a blog post on solace.com](https://solace.com/blog/mqtt-client-cert-auth/) that walks through creating 
a CA and client certificate, then using it to connect to Solace.