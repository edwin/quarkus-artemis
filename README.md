# Quarkus, Artemis and Red Hat AMQ Broker on Openshift 4

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## About
A quarkus app to do a jms pub/sub request to a clustered Red Hat AMQ Broker in Openshift 4. Red Hat AMQ is being deployed by using an Operator, simplifying the whole management and deployment.

## Concept
```
+------------------------------------------------------------------------------------------------+
|  Openshift 4                                                                                   |
|                                                                                                |
|    +-------one-pod------------+      +---two--pod---------+      +--------one-pod-----------+  |
|    | Quarkus Artemis Producer | ---> | Red Hat AMQ Broker | ---> | Quarkus Artemis Consumer |  |
|    +--------------------------+      +--------------------+      +--------------------------+  |
+ -----------------------------------------------------------------------------------------------+
```

Quarkus Artemis producer will publish a round-robin messages to two AMQ Broker instances, where each instances is being subscribe by one consumer. On log we can see `msg from broker 0` and `msg from broker 1` multiple times, showing the capability of round robin requests.

## Result
```
Starting the Java application using /opt/jboss/container/java/run/run-java.sh ...
INFO exec  java -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -XX:+UseParallelGC -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -XX:+ExitOnOutOfMemoryError -cp "." -jar /deployments/quarkus-run.jar 
__  ____  __  _____   ___  __ ____  ______ 
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
2024-06-21 17:34:09,728 WARN  [io.qua.config] (main) Unrecognized configuration key "quarkus.http.host" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
2024-06-21 17:34:09,895 WARN  [io.qua.arc.ComponentsProvider] (main) Unable to load removed bean type [com.edw.JMSConfig]: java.lang.ClassNotFoundException: com.edw.JMSConfig
2024-06-21 17:34:11,627 INFO  [io.quarkus] (main) quarkus-artemis 1.0.0-SNAPSHOT on JVM (powered by Quarkus 2.10.2.Final) started in 7.712s. 
2024-06-21 17:34:11,632 INFO  [io.quarkus] (main) Profile prod activated. 
2024-06-21 17:34:11,635 INFO  [io.quarkus] (main) Installed features: [artemis-jms, cdi]
2024-06-21 17:34:13,330 INFO  [com.edw.PriceProducer] (pool-2-thread-1) sending message prices >>> 27733a27-9449-46d5-811d-62c4f9075b9a
2024-06-21 17:34:13,357 INFO  [com.edw.PriceConsumer] (Thread-2) >>>>>>>> msg from broker 1 >>>>> 27733a27-9449-46d5-811d-62c4f9075b9a
2024-06-21 17:34:19,410 INFO  [com.edw.PriceConsumer] (Thread-1) >>>>>>>> msg from broker 0 >>>>> f5887996-f912-450a-98ae-faedb430e4b4
2024-06-21 17:34:19,447 INFO  [com.edw.PriceProducer] (pool-2-thread-1) sending message prices >>> f5887996-f912-450a-98ae-faedb430e4b4
2024-06-21 17:34:24,685 INFO  [com.edw.PriceProducer] (pool-2-thread-1) sending message prices >>> c9b1c146-c3e0-4ded-8865-377e4d25b2cc
2024-06-21 17:34:24,686 INFO  [com.edw.PriceConsumer] (Thread-1) >>>>>>>> msg from broker 0 >>>>> c9b1c146-c3e0-4ded-8865-377e4d25b2cc
2024-06-21 17:34:29,736 INFO  [com.edw.PriceProducer] (pool-2-thread-1) sending message prices >>> f54ef576-faa3-422a-8af8-c573a25def22
2024-06-21 17:34:29,739 INFO  [com.edw.PriceConsumer] (Thread-2) >>>>>>>> msg from broker 1 >>>>> f54ef576-faa3-422a-8af8-c573a25def22
2024-06-21 17:34:35,110 INFO  [com.edw.PriceConsumer] (Thread-2) >>>>>>>> msg from broker 1 >>>>> a8634fe1-7848-447c-8645-1dfade4f94c2
2024-06-21 17:34:35,111 INFO  [com.edw.PriceProducer] (pool-2-thread-1) sending message prices >>> a8634fe1-7848-447c-8645-1dfade4f94c2
2024-06-21 17:34:40,420 INFO  [com.edw.PriceProducer] (pool-2-thread-1) sending message prices >>> 6457cc42-7bb9-453b-ac3c-5820fed455d7
2024-06-21 17:34:40,447 INFO  [com.edw.PriceConsumer] (Thread-1) >>>>>>>> msg from broker 0 >>>>> 6457cc42-7bb9-453b-ac3c-5820fed455d7
2024-06-21 17:34:45,614 INFO  [com.edw.PriceProducer] (pool-2-thread-1) sending message prices >>> bb38de67-b24a-4a66-ba75-edf43e77ccf5
2024-06-21 17:34:45,632 INFO  [com.edw.PriceConsumer] (Thread-1) >>>>>>>> msg from broker 0 >>>>> bb38de67-b24a-4a66-ba75-edf43e77ccf5
```

## Stat
Here is the result of `artemis queue stat` on both AMQ Broker instances. And on both instances we have one consumer each for `prices` queue.
```
sh-4.4$ ./artemis queue stat
NOTE: Picked up JDK_JAVA_OPTIONS: -Dbroker.properties=/amq/extra/secrets/my-broker-props/broker.properties
Connection brokerURL = tcp://my-broker-ss-0.my-broker-hdls-svc.broker.svc.cluster.local:61616
|NAME                     |ADDRESS                  |CONSUMER|MESSAGE|MESSAGES|DELIVERING|MESSAGES|SCHEDULED| ROUTING |INTERNAL|
|                         |                         | COUNT  | COUNT | ADDED  |  COUNT   | ACKED  |  COUNT  |  TYPE   |        |
|$.artemis.internal.sf.my-|$.artemis.internal.sf.my-|   1    |   0   |  3050  |    0     |  3050  |    0    |MULTICAST|  true  |
|  cluster.91afe236-2fdf-1|  cluster.91afe236-2fdf-1|        |       |        |          |        |         |         |        |
|  1ef-a096-0a580ad90024  |  1ef-a096-0a580ad90024  |        |       |        |          |        |         |         |        |
|$sys.mqtt.sessions       |$sys.mqtt.sessions       |   0    |   0   |   0    |    0     |   0    |    0    | ANYCAST |  true  |
|DLQ                      |DLQ                      |   0    |   0   |   0    |    0     |   0    |    0    | ANYCAST | false  |
|ExpiryQueue              |ExpiryQueue              |   0    |   0   |   0    |    0     |   0    |    0    | ANYCAST | false  |
|myQueue0                 |myAddress0               |   0    |   0   |   0    |    0     |   0    |    0    | ANYCAST | false  |
|notif.9505eb12-2fdf-11ef-|activemq.notifications   |   1    |   0   |  9128  |    0     |  9128  |    0    |MULTICAST|  true  |
|  a096-0a580ad90024.Activ|                         |        |       |        |          |        |         |         |        |
|  eMQServerImpl_name=amq-|                         |        |       |        |          |        |         |         |        |
|  broker                 |                         |        |       |        |          |        |         |         |        |
|prices                   |prices                   |   1    |   0   |  3691  |    0     |  3691  |    0    | ANYCAST | false  |
|prices-lele              |prices-lele              |   0    |   0   |   0    |    0     |   0    |    0    | ANYCAST | false  |

Note: Use --clustered to expand the report to other nodes in the topology.
```

```
sh-4.4$ ./artemis queue stat
NOTE: Picked up JDK_JAVA_OPTIONS: -Dbroker.properties=/amq/extra/secrets/my-broker-props/broker.properties
Connection brokerURL = tcp://my-broker-ss-1.my-broker-hdls-svc.broker.svc.cluster.local:61616
|NAME                     |ADDRESS                  |CONSUMER|MESSAGE|MESSAGES|DELIVERING|MESSAGES|SCHEDULED| ROUTING |INTERNAL|
|                         |                         | COUNT  | COUNT | ADDED  |  COUNT   | ACKED  |  COUNT  |  TYPE   |        |
|$.artemis.internal.sf.my-|$.artemis.internal.sf.my-|   1    |   0   |  1928  |    0     |  1928  |    0    |MULTICAST|  true  |
|  cluster.91afe24f-2fdf-1|  cluster.91afe24f-2fdf-1|        |       |        |          |        |         |         |        |
|  1ef-861b-0a580ad90023  |  1ef-861b-0a580ad90023  |        |       |        |          |        |         |         |        |
|$sys.mqtt.sessions       |$sys.mqtt.sessions       |   0    |   0   |   0    |    0     |   0    |    0    | ANYCAST |  true  |
|DLQ                      |DLQ                      |   0    |   0   |   0    |    0     |   0    |    0    | ANYCAST | false  |
|ExpiryQueue              |ExpiryQueue              |   0    |   0   |   0    |    0     |   0    |    0    | ANYCAST | false  |
|myQueue0                 |myAddress0               |   0    |   0   |   0    |    0     |   0    |    0    | ANYCAST | false  |
|notif.94fbb0fa-2fdf-11ef-|activemq.notifications   |   1    |   0   |  9141  |    0     |  9141  |    0    |MULTICAST|  true  |
|  861b-0a580ad90023.Activ|                         |        |       |        |          |        |         |         |        |
|  eMQServerImpl_name=amq-|                         |        |       |        |          |        |         |         |        |
|  broker                 |                         |        |       |        |          |        |         |         |        |
|prices                   |prices                   |   1    |   0   |  449   |    0     |  449   |    0    | ANYCAST | false  |
|prices-lele              |prices-lele              |   0    |   0   |  5584  |    0     |  5584  |    0    | ANYCAST | false  |

Note: Use --clustered to expand the report to other nodes in the topology.
```

## Testings

### First Cycle
using a Quarkus pod which consist of one Producer and one Consumer. Here we can see an uneven distribution of listeners.

| # Quarkus Replica | # Producers | # Consumer | # Brokers | # Listeners on Broker 0 | # Listeners on Broker 1 |
|-------------------|-------------|------------|-----------|-------------------------|-------------------------|
| 1                 | 1           | 1          | 2         | 1                       | 0                       |
| 2                 | 2           | 2          | 2         | 2                       | 0                       |
| 4                 | 4           | 4          | 2         | 3                       | 1                       |
| 6                 | 6           | 6          | 2         | 2                       | 4                       |

### Second Cycle
using a Quarkus pod which consist of one Producer and two Consumers with each consumer pointing to a different broker. We can see that listeners are much more evenly distributed. 

| # Quarkus Replica | # Producers | # Consumer | # Brokers | # Listeners on Broker 0 | # Listeners on Broker 1 |
|-------------------|-------------|------------|-----------|-------------------------|-------------------------|
| 1                 | 1           | 2          | 2         | 1                       | 1                       |
| 2                 | 2           | 4          | 2         | 2                       | 2                       |
| 4                 | 4           | 8          | 2         | 4                       | 4                       |
| 6                 | 6           | 12         | 2         | 6                       | 6                       |

## Related Guides

- Artemis JMS ([guide](https://quarkiverse.github.io/quarkiverse-docs/quarkus-artemis/dev/index.html)): Use JMS APIs to connect to ActiveMQ Artemis via its native protocol
