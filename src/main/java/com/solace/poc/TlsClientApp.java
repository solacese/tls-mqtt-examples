package com.solace.poc;

import org.eclipse.paho.client.mqttv3.*;

import javax.net.SocketFactory;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Example application that connects with a TLS 1.2 session, with a provided CA-certificate file.
 *
 * Should fail in the following cases:
 * - If basic authentication is disabled on the broker;
 * - If the provided CA-certificate was not the signer of the broker's server-certificate.
 *
 * This sample program was verified by testing against a Solace Event Broker ver. 9.1.0.77.
 */
public class TlsClientApp
{
    final private static String PUBTOPIC = "T/GettingStarted/pubsub";
    final private static String SUBSCRIPTION = "T/GettingStarted/#";
    final private static int    MAX = 10;

    private static MqttClient createClient(String serverURI,
                                           String username,
                                           String password,
                                           String cacertfile) throws Exception {
        MqttClient mqttClient = new MqttClient( serverURI, cacertfile );

        final MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession( true );
        options.setUserName( username );
        options.setPassword( password.toCharArray() );

        SocketFactory sf = SslUtil.getSocketFactory(cacertfile);
        options.setSocketFactory( sf );
        mqttClient.connect( options );

        return mqttClient;
    }

    public static void main( String[] args ) throws Exception
    {
        if (args.length < 4) {
            System.out.println( "\n\tTlsClientApp: <connection-string> <username> <password> <ca-certfile>\n" );
            System.out.println( "\tFor example: ssl://localhost:8883 mqttclient $up3r$3cr3t, resources/sampleCA.pem\n" );
            System.exit( 0 );
        }

        MqttClient mqttClient = createClient( args[0], args[1], args[2], args[3] );

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
