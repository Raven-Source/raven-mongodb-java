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
import org.raven.commons.data.SerializableTypeUtils;
import org.raven.commons.data.StringType;


/**
 * @author yi.liang
 * @since JDK11
 */
public final class StringTypePropertyCodecProvider implements PropertyCodecProvider, CodecProvider {

    private final CodecRegistry codecRegistry;
    private final static Class<StringType> stringTypeClass = StringType.class;

    /**
     * @param codecRegistry
     */
    public StringTypePropertyCodecProvider(final CodecRegistry codecRegistry) {
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

        if (stringTypeClass.isAssignableFrom(clazz)) {
            try {
                return codecRegistry.get(clazz);
            } catch (CodecConfigurationException e) {
                return (Codec<T>) new StringTypeCodec(clazz);
            }
        }
        return null;
    }

    /**
     * @param <T>
     */
    private static class StringTypeCodec<T extends StringType> implements Codec<T> {

        private final Class<T> clazz;

        /**
         * @param clazz
         */
        StringTypeCodec(final Class<T> clazz) {
            this.clazz = clazz;
        }

        /**
         * @param writer
         * @param value
         * @param encoderContext
         */
        @Override
        public void encode(BsonWriter writer, T value, EncoderContext encoderContext) {
            writer.writeString(value.getValue());
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
            return SerializableTypeUtils.stringValueOf(clazz, reader.readString());
        }
    }

}