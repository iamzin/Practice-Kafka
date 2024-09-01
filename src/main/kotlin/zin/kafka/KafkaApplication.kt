package zin.kafka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import zin.kafka.domain.Customer
import zin.kafka.producer.Producer
import zin.kafka.producer.ProducerCallback

@SpringBootApplication
class KafkaApplication

fun main(args: Array<String>) {
    runApplication<KafkaApplication>(*args)

    runProducer()
}

private fun runProducer() {
    val producer = Producer()

    while (true) {
        val customer =
            Customer(
                id = (0..100).random(),
                name = "Customer ${(0..100).random()}",
            )

        producer.send("customer", customer, ProducerCallback())
        Thread.sleep(1000)
    }
}
