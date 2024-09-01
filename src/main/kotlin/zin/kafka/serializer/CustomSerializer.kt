package zin.kafka.serializer

import org.apache.kafka.common.serialization.Serializer
import zin.kafka.domain.Customer
import java.nio.ByteBuffer
import java.nio.charset.Charset

class CustomSerializer : Serializer<Customer> {
    /**
     * We are serializing Customer as:
     * 4 byte int representing id
     * 4 byte int representing length of the name in UTF-8 bytes (0 if name is null)
     * N bytes representing the UTF-8 bytes of the name
     */
    override fun serialize(
        topic: String?,
        data: Customer?,
    ): ByteArray {
        try {
            if (data == null) {
                return ByteArray(0)
            }

            val serializedName = data.name?.toByteArray(Charset.forName("UTF-8")) ?: ByteArray(0)
            val stringSize = serializedName.size

            val buffer = ByteBuffer.allocate(4 + 4 + stringSize)
            buffer.putInt(data.id)
            buffer.putInt(stringSize)
            buffer.put(serializedName)

            return buffer.array()
        } catch (e: Exception) {
            throw RuntimeException("Error serializing Customer to byte[]", e)
        }
    }
}
