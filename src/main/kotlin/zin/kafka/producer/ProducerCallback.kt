package zin.kafka.producer

import org.apache.kafka.clients.producer.Callback
import org.apache.kafka.clients.producer.RecordMetadata
import java.lang.Exception

class ProducerCallback : Callback {
    override fun onCompletion(
        p0: RecordMetadata?,
        p1: Exception?,
    ) {
        p1?.printStackTrace()
    }
}
