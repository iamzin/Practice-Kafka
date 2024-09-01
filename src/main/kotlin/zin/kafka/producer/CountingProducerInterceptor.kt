package zin.kafka.producer

import org.apache.kafka.clients.producer.ProducerInterceptor
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.clients.producer.RecordMetadata
import org.springframework.stereotype.Component
import java.lang.Exception
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicLong

@Component
class CountingProducerInterceptor : ProducerInterceptor<Any, Any> {
    private val executorService = Executors.newSingleThreadScheduledExecutor()
    private val numSent = AtomicLong(0)
    private val numAcked = AtomicLong(0)

    override fun configure(configs: MutableMap<String, *>?) {
        val windowSize = configs?.get("counting.interceptor.window.size.ms") as Long
        executorService.scheduleAtFixedRate(
            { run() },
            windowSize,
            windowSize,
            java.util.concurrent.TimeUnit.MILLISECONDS,
        )
    }

    override fun close() {
        executorService.shutdown()
    }

    override fun onAcknowledgement(
        metadata: RecordMetadata?,
        exception: Exception?,
    ) {
        numAcked.incrementAndGet()
    }

    override fun onSend(record: ProducerRecord<Any, Any>?): ProducerRecord<Any, Any> {
        numSent.incrementAndGet()
        return record!!
    }

    private fun run() {
        val sent = numSent.getAndSet(0)
        val acked = numAcked.getAndSet(0)

        println("Sent: $sent Acked: $acked")
    }
}
