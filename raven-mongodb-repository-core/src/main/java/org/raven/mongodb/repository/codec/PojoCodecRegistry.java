package org.raven.mongodb.repository.codec;

import com.mongodb.MongoClientSettings;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.raven.mongodb.repository.conventions.CustomConventions;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

        PropertyCodecProvider valueTypePropertyCodecProvider = new ValueTypePropertyCodecProvider(pojoCodecRegistry);
        PropertyCodecProvider stringTypePropertyCodecProvider = new StringTypePropertyCodecProvider(pojoCodecRegistry);

        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().conventions(CustomConventions.DEFAULT_CONVENTIONS).automatic(true)
                        .register(valueTypePropertyCodecProvider, stringTypePropertyCodecProvider).build()
                )
        );

        return codecRegistry;
    }

}
