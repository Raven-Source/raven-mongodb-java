package org.raven.mongodb.repository.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;
import org.bson.types.Decimal128;
import org.raven.commons.data.SerializableTypeUtils;
import org.raven.commons.data.ValueType;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @author yi.liang
 * @since JDK11
 */
public final class ValueTypePropertyCodecProvider implements PropertyCodecProvider, CodecProvider {

    private final CodecRegistry codecRegistry;
    private final static Class<ValueType> valueTypeClass = ValueType.class;

    /**
     * @param codecRegistry
     */
    public ValueTypePropertyCodecProvider(final CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }


    /**
     * @param type
     * @param propertyCodecRegistry
     * @param <T>
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <T> Codec<T> get(final TypeWithTypeParameters<T> type, final PropertyCodecRegistry propertyCodecRegistry) {
        Class<T> clazz = type.getType();
        return this.get(clazz);
    }

    @Override
    public <T> Codec<T> get(Class<T> clazz, CodecRegistry registry) {
        return this.get(clazz);
    }

    private <T> Codec<T> get(Class<T> clazz) {

        if (valueTypeClass.isAssignableFrom(clazz)) {
            try {
                return codecRegistry.get(clazz);
            } catch (CodecConfigurationException e) {
                return (Codec<T>) new ValueTypeCodec(clazz);
            }
        }
        return null;
    }

    /**
     * @param <T>
     */
    private static class ValueTypeCodec<T extends ValueType> implements Codec<T> {

        private final Class<T> clazz;
        private final Class genericType;

        /**
         * @param clazz
         */
        ValueTypeCodec(final Class<T> clazz) {
            this.clazz = clazz;
            genericType = SerializableTypeUtils.getGenericType(clazz);
        }

        /**
         * @param writer
         * @param value
         * @param encoderContext
         */
        @Override
        public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {

            Number writeVal = value.getValue();
            if (genericType.equals(Integer.class)) {
                writer.writeInt32(writeVal.intValue());

            } else if (genericType.equals(Long.class)) {
                writer.writeInt64(writeVal.longValue());

            } else if (genericType.equals(BigInteger.class)) {
                writer.writeInt64(writeVal.longValue());

            } else if (genericType.equals(Double.class)) {
                writer.writeDouble(writeVal.doubleValue());

            } else if (genericType.equals(Float.class)) {
                writer.writeDouble(writeVal.doubleValue());

            } else if (genericType.equals(BigDecimal.class)) {
                writer.writeDecimal128(new Decimal128((BigDecimal) writeVal));

            } else {
                writer.writeInt32(writeVal.intValue());
            }

        }

        /**
         * @return
         */
        @Override
        public Class<T> getEncoderClass() {
            return clazz;
        }

        /**
         * @param reader
         * @param decoderContext
         * @return
         */
        @Override
        public T decode(final BsonReader reader, final DecoderContext decoderContext) {

            if (genericType.equals(Integer.class)) {
                return SerializableTypeUtils.valueOf(clazz, reader.readInt32());

            } else if (genericType.equals(Long.class)) {
                return SerializableTypeUtils.valueOf(clazz, reader.readInt64());

            } else if (genericType.equals(BigInteger.class)) {
                return SerializableTypeUtils.valueOf(clazz, reader.readInt64());

            } else if (genericType.equals(Double.class)) {
                return SerializableTypeUtils.valueOf(clazz, reader.readDouble());

            } else if (genericType.equals(Float.class)) {
                return SerializableTypeUtils.valueOf(clazz, (float) reader.readDouble());

            } else if (genericType.equals(BigDecimal.class)) {
                return SerializableTypeUtils.valueOf(clazz, reader.readDecimal128().bigDecimalValue());

            } else {
                return SerializableTypeUtils.valueOf(clazz, reader.readInt32());
            }

        }
    }

}