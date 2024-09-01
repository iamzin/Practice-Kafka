package zin.kafka.partitioner

import org.apache.kafka.clients.producer.Partitioner
import org.apache.kafka.common.Cluster
import org.apache.kafka.common.InvalidRecordException
import org.springframework.stereotype.Component
import kotlin.math.abs

@Component
class CustomPartitioner : Partitioner {
    companion object {
        const val ANONYMOUS_PARTITION_LOCATION = -1
        const val ANONYMOUS_NAME = "anonymous"
    }

    override fun configure(configs: MutableMap<String, *>?) {}

    override fun close() {}

    override fun partition(
        topic: String?,
        key: Any?,
        keyBytes: ByteArray?,
        value: Any?,
        valueBytes: ByteArray?,
        cluster: Cluster?,
    ): Int {
        val partitions = cluster?.partitionsForTopic(topic)
        val numPartitions = partitions?.size ?: 1

        if (keyBytes == null || key !is String) {
            throw InvalidRecordException("All messages must have a key")
        }

        return if (key == ANONYMOUS_NAME) {
            numPartitions + ANONYMOUS_PARTITION_LOCATION
        } else {
            abs(key.hashCode()) % (numPartitions - 1)
        }
    }
}
