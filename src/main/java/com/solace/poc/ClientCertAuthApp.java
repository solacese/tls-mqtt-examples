package com.solace.poc;

import org.eclipse.paho.client.mqttv3.*;

import javax.net.SocketFactory;

/**
 * Example application that connects with a TLS 1.2 session, a provided CA-certificate file, and a
 * java Keystore containing the client certificate and private key required to authenticate to
 * the server.
 *
 * Should fail in the following cases:
 * - If basic authentication and client-certificate authentication are disabled on the broker;
 * - If the provided CA-certificate was not the signer of the broker's server-certificate;
 * - If the client-certificate is not signed by a CA known by the server;
 * - If the provided keystore password does not match the provided keystore file.
 *
 * This sample program was verified by testing against a Solace Event Broker ver. 9.1.0.77.
 */
public class ClientCertAuthApp
{
    final private static String PUBTOPIC = "T/GettingStarted/pubsub";
    final private static String SUBSCRIPTION = "T/GettingStarted/#";
    final private static int    MAX = 10;

    private static MqttClient createClient(String serverURI,
                                           String username,
                                           String password,
                                           String cacertFile,
                                           String keystoreFile,
                                           String keystorePassword) throws Exception {
        MqttClient mqttClient = new MqttClient( serverURI, cacertFile );

        final MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession( true );
        options.setUserName( username );
        options.setPassword( password.toCharArray() );

        SocketFactory sf = SslUtil.getSocketFactory( cacertFile, keystoreFile, keystorePassword );
        options.setSocketFactory( sf );
        mqttClient.connect( options );

        return mqttClient;
    }

    public static void main( String[] args ) throws Exception
    {
        if (args.length < 6) {
            System.out.println( "\n\tClientCertAuthApp: <connection-string> <username> <password> <ca-cert-file> <keystore-file> <keystore-password>\n" );
            System.out.println( "\tFor example: ssl://localhost:8883 mqttclient $up3r$3cr3t resources/sampleCA.pem resources/mqttclient.keystore mq77rul35\n" );
            System.exit( 0 );
        }

        MqttClient mqttClient = createClient( args[0], args[1], args[2], args[3], args[4], args[5] );

        PrintingCallback callback = new PrintingCallback( MAX );
        mqttClient.setCallback( callback );
        mqttClient.subscribe( SUBSCRIPTION, 0 );

        for(int i = 0; i < MAX; i++) {
            MqttMessage message = new MqttMessage( "Hello world from MQTT!".getBytes() );
            message.setQos( 0 );
            mqttClient.publish( PUBTOPIC, message );

            Thread.sleep( 1000 );
        }

        while( callback.getCounter().intValue() < MAX ) {
            Thread.sleep( 1000 );
        }

        mqttClient.disconnect();
        mqttClient.close();
    }
}
