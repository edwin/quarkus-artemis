package com.edw;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import io.smallrye.common.annotation.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.jms.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <pre>
 *     com.edw.PriceConsumer
 * </pre>
 *
 * @author Muhammad Edwin < edwin at redhat dot com >
 * 18 Jul 2022 21:38
 */
public class PriceConsumer implements Runnable {

    @Inject
    ConnectionFactory connectionFactory_brokerURLService_00;

    @Inject
    ConnectionFactory connectionFactory_brokerURLService_01;

    private Logger logger = LoggerFactory.getLogger(PriceConsumer.class);

    public PriceConsumer(@Identifier("brokerURLService_00") ConnectionFactory connectionFactory_brokerURLService_00,
                         @Identifier("brokerURLService_01") ConnectionFactory connectionFactory_brokerURLService_01) {
        this.connectionFactory_brokerURLService_00 = connectionFactory_brokerURLService_00;
        this.connectionFactory_brokerURLService_01 = connectionFactory_brokerURLService_01;
    }

    private final ExecutorService scheduler = Executors.newSingleThreadExecutor();

    void onStart(@Observes StartupEvent ev) {
        scheduler.submit(this);
    }

    void onStop(@Observes ShutdownEvent ev) {
        scheduler.shutdown();
    }

    @Override
    public void run() {
        new Thread(() -> {
            try (JMSContext context = connectionFactory_brokerURLService_00.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
                JMSConsumer consumer = context.createConsumer(context.createQueue("prices"));
                while (true) {
                    Message message = consumer.receive();
                    if (message == null) return;
                    String uuid = message.getBody(String.class);

                    logger.info(">>>>>>>> msg from broker 0 >>>>> {}", uuid);
                }
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }).start();

        new Thread(() -> {
            try (JMSContext context = connectionFactory_brokerURLService_01.createContext(JMSContext.AUTO_ACKNOWLEDGE)) {
                JMSConsumer consumer = context.createConsumer(context.createQueue("prices"));
                while (true) {
                    Message message = consumer.receive();
                    if (message == null) return;
                    String uuid = message.getBody(String.class);

                    logger.info(">>>>>>>> msg from broker 1 >>>>> {}", uuid);
                }
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
}
