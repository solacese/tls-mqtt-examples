#!/usr/bin/env python
import paho.mqtt.client as mqtt
import time

broker_address="localhost"
topic="T/GettingStarted/pubsub"

def on_message(client, userdata, message):
    print "message received "   , str(message.payload.decode("utf-8"))
    print "message topic="      , message.topic
    print "message qos="        , message.qos
    print "message retain flag=", message.retain


print( "creating new instance" )
client = mqtt.Client( "P1" )

print( "connecting to broker" )
client.connect( broker_address )

client.loop_start()

print( "Subscribing to topic", topic )
client.on_message=on_message
client.subscribe( topic )

for i in range( 1, 10 ):
    print( "Publishing message to topic" , topic )
    client.publish( topic, "Hello world from MQTT "+str(i) )
    time.sleep( 1 )

client.loop_stop()

print( "Goodbye!" )
