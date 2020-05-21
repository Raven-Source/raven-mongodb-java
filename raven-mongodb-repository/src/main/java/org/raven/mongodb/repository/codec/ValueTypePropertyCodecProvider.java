package org.raven.mongodb.repository.codec;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecConfigurationException;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.bson.codecs.pojo.PropertyCodecRegistry;
import org.bson.codecs.pojo.TypeWithTypeParameters;
import org.bson.types.Decimal128;
import org.raven.commons.data.ValueType;
import org.raven.commons.data.ValueTypeUtils;

import java.math.BigDecimal;
import java.math.BigInteger;


/**
 * @author yi.liang
 * @since JDK1.8
 */
public final class ValueTypePropertyCodecProvider implements PropertyCodecProvider {

    private final CodecRegistry codecRegistry;
    private final static Class<ValueType> valueEnumTypeClass = ValueType.class;

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
        if (valueEnumTypeClass.isAssignableFrom(clazz)) {
            try {
                return codecRegistry.get(clazz);
            } catch (CodecConfigurationException e) {
                return (Codec<T>) new ValueTypePropertyCodecProvider.ValueTypeCodec(clazz);
            }
        }
        return null;
    }

    /**
     * @param <T>
     */
    private static class ValueTypeCodec<T extends ValueType> implements Codec<T> {

        private final Class<T> clazz;
        private final Class<? extends Number> numberClazz;
//        private HashMap<Integer, ValueType> valueMap;

        /**
         * @param clazz
         */
        ValueTypeCodec(final Class<T> clazz) {
            this.clazz = clazz;
            numberClazz = ValueTypeUtils.getGenericType(clazz);
//            valueMap = ValueTypeUtils.getValueMap(clazz);
        }

        /**
         * @param writer
         * @param value
         * @param encoderContext
         */
        @Override
        public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {

            Number writeVal = value.getValue();
            if (writeVal.equals(Integer.class)) {
                writer.writeInt32(writeVal.intValue());

            } else if (writeVal.equals(Long.class)) {
                writer.writeInt64(writeVal.longValue());

            } else if (writeVal.equals(BigInteger.class)) {
                writer.writeInt64(writeVal.longValue());

            } else if (writeVal.equals(Double.class)) {
                writer.writeDouble(writeVal.doubleValue());

            } else if (writeVal.equals(Float.class)) {
                writer.writeDouble(writeVal.doubleValue());

            } else if (writeVal.equals(BigDecimal.class)) {
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

            if (numberClazz.equals(Integer.class)) {
                return ValueTypeUtils.valueOf(clazz, reader.readInt32());

            } else if (numberClazz.equals(Long.class)) {
                return ValueTypeUtils.valueOf(clazz, reader.readInt64());

            } else if (numberClazz.equals(BigInteger.class)) {
                return ValueTypeUtils.valueOf(clazz, reader.readInt64());

            } else if (numberClazz.equals(Double.class)) {
                return ValueTypeUtils.valueOf(clazz, reader.readDouble());

            } else if (numberClazz.equals(Float.class)) {
                return ValueTypeUtils.valueOf(clazz, (float) reader.readDouble());

            } else if (numberClazz.equals(BigDecimal.class)) {
                return ValueTypeUtils.valueOf(clazz, reader.readDecimal128().bigDecimalValue());

            } else {
                return ValueTypeUtils.valueOf(clazz, reader.readInt32());
            }

        }
    }

}