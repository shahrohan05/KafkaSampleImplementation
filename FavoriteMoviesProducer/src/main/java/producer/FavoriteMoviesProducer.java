package producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.joran.action.LoggerAction;


public class FavoriteMoviesProducer {

    private static final String TOPIC = "favorite-movies";
    private static final String BOOTSTRAP_SERVER = "localhost:9092";

    private static Properties props;
    private static Producer<String, String> producer;

    static {
        props = new Properties();
        props.put("bootstrap.servers", BOOTSTRAP_SERVER);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        

    }

    private static void sendFavoriteMovie(String movie) {
        Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.OFF);

        /**
         * topic - The topic the record will be appended to
         * 
         * partition - The partition to which the record should be sent
         * 
         * timestamp - The timestamp of the record
         * 
         * key - The key that will be included in the record
         * 
         * value - The record contents
         */
        System.out.println("Sending - " + movie);
        producer = new KafkaProducer<>(props);
        producer.send(new ProducerRecord<String, String>(TOPIC, 0, System.currentTimeMillis(), "User123", movie));
        producer.close();
        System.out.println("sent...");
    }

    public static void main(String[] args) {
        while (true) {
            String favoriteMovie = System.console().readLine("Favorite Movie (type 'exit/bye' to quit):");

            if (favoriteMovie.equals("exit") || favoriteMovie.equals("bye")) {
                break;
            } else if (favoriteMovie != null && favoriteMovie.trim().length() > 0) {
                sendFavoriteMovie(favoriteMovie);
            }
        }
    }
}