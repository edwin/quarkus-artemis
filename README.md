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
|    +-----------pod------------+      +--------pod---------+      +------------pod-----------+  |
|    | Quarkus Artemis Producer | ---> | Red Hat AMQ Broker | ---> | Quarkus Artemis Consumer |  |
|    +--------------------------+      +--------------------+      +--------------------------+  |
+ -----------------------------------------------------------------------------------------------+
```

## Result
```
Starting the Java application using /opt/jboss/container/java/run/run-java.sh ...
INFO exec  java -Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager -XX:+UseParallelGC -XX:MinHeapFreeRatio=10 -XX:MaxHeapFreeRatio=20 -XX:GCTimeRatio=4 -XX:AdaptiveSizePolicyWeight=90 -XX:+ExitOnOutOfMemoryError -cp "." -jar /deployments/quarkus-run.jar
__  ____  __  _____   ___  __ ____  ______
 --/ __ \/ / / / _ | / _ \/ //_/ / / / __/
 -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \
--\___\_\____/_/ |_/_/|_/_/|_|\____/___/
2022-07-18 15:15:13,321 WARN  [io.qua.config] (main) Unrecognized configuration key "quarkus.http.host" was provided; it will be ignored; verify that the dependency extension for this configuration is set or that you did not make a typo
2022-07-18 15:15:14,036 INFO  [io.quarkus] (main) quarkus-artemis 1.0.0-SNAPSHOT on JVM (powered by Quarkus 2.10.2.Final) started in 1.960s.
2022-07-18 15:15:14,037 INFO  [io.quarkus] (main) Profile prod activated.
2022-07-18 15:15:14,038 INFO  [io.quarkus] (main) Installed features: [artemis-jms, cdi]
>>>>>>>>>>>>> 48
>>>>>>>>>>>>> 91
>>>>>>>>>>>>> 90
>>>>>>>>>>>>> 32
>>>>>>>>>>>>> 1
>>>>>>>>>>>>> 43
>>>>>>>>>>>>> 61
>>>>>>>>>>>>> 29
>>>>>>>>>>>>> 93
>>>>>>>>>>>>> 23
>>>>>>>>>>>>> 60
```

## Related Guides

- Artemis JMS ([guide](https://quarkiverse.github.io/quarkiverse-docs/quarkus-artemis/dev/index.html)): Use JMS APIs to connect to ActiveMQ Artemis via its native protocol
