package com.solace.poc;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicInteger;

class PrintingCallback implements MqttCallback {
    final private AtomicInteger counter;
    final int max;

    PrintingCallback(int expectedCount) {
        max =expectedCount;
        counter = new AtomicInteger(0);
    }

    AtomicInteger getCounter() {
        return counter;
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("\nReceived a Message!" +
                "\n\tTime:    " + new Timestamp(System.currentTimeMillis()) +
                "\n\tTopic:   " + topic +
                "\n\tMessage: " + new String(message.getPayload()) +
                "\n\tQoS:     " + message.getQos());
        counter.incrementAndGet();
    }

    @Override
    public void connectionLost(Throwable cause) {
        System.out.println("\tConnection to Solace broker lost!" + cause.getMessage());
        counter.addAndGet(max);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("\tDONE: "+token.getMessageId()+"\n");
    }

}
