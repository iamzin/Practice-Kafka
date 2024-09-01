package zin.kafka.producer

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord
import zin.kafka.domain.Customer
import java.nio.charset.StandardCharsets
import java.util.Properties

class Producer {
    private val producer: KafkaProducer<String, Customer>

    init {
        val props = Properties()
        props["bootstrap.servers"] = "localhost:9092"
        props["key.serializer"] = "io.confluent.kafka.serializers.KafkaAvroSerializer"
        props["value.serializer"] = "io.confluent.kafka.serializers.KafkaAvroSerializer"
        props["schema.registry.url"] = "http://localhost:8081"
        producer = KafkaProducer(props)
    }

    fun send(
        topic: String,
        customer: Customer,
        callback: ProducerCallback? = null,
    ) {
        val record: ProducerRecord<String, Customer> = ProducerRecord(topic, customer.name, customer)

        record.headers().add(DEFAULT_RECORD_HEADER_KEY, DEFAULT_RECORD_HEADER_VALUE)

        kotlin.runCatching {
            producer.send(record, callback ?: Callback { _, _ -> })
        }.onFailure {
            it.printStackTrace()
        }
    }

    fun close() {
        producer.close()
    }

    companion object {
        const val DEFAULT_RECORD_HEADER_KEY = "default-header"
        val DEFAULT_RECORD_HEADER_VALUE = "default-value".toByteArray(StandardCharsets.UTF_8)
    }
}
