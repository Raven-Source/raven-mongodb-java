package org.raven.mongodb.repository.codec;

import com.mongodb.MongoClientSettings;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.internal.ProvidersCodecRegistry;
import org.raven.mongodb.repository.conventions.CustomConventions;

import java.lang.reflect.Field;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

/**
 * PojoCodecRegistry
 *
 * @author yi.liang
 * date 2021.07.25 22:01
 */
@Slf4j
public class PojoCodecRegistry {

    public final static CodecRegistry CODEC_REGISTRY = registry();

    private static CodecRegistry registry() {

        CodecRegistry pojoCodecRegistry = MongoClientSettings.getDefaultCodecRegistry();
        //registry CodecProvider

        ValueTypePropertyCodecProvider valueTypePropertyCodecProvider = new ValueTypePropertyCodecProvider(pojoCodecRegistry);
        StringTypePropertyCodecProvider stringTypePropertyCodecProvider = new StringTypePropertyCodecProvider(pojoCodecRegistry);

        CodecRegistry customCodecRegistry = fromProviders(
                PojoCodecProvider.builder().conventions(CustomConventions.DEFAULT_CONVENTIONS).automatic(true)
                        .register(valueTypePropertyCodecProvider, stringTypePropertyCodecProvider)
                        .build()
                , valueTypePropertyCodecProvider, stringTypePropertyCodecProvider);

        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), customCodecRegistry);

        modifyBson(customCodecRegistry);

        return codecRegistry;
    }

    @SuppressWarnings({"unchecked"})
    private static void modifyBson(CodecProvider codecProvider) {

        try {

            Field field = ProvidersCodecRegistry.class.getDeclaredField("codecProviders");
            field.setAccessible(true);

            List<CodecProvider> codecProviders = (List<CodecProvider>) field.get(Bson.DEFAULT_CODEC_REGISTRY);
            codecProviders.add(codecProvider);

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
    }

}
