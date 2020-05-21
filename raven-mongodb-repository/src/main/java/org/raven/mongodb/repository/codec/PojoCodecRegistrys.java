package org.raven.mongodb.repository.codec;

import com.mongodb.MongoClient;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.PropertyCodecProvider;
import org.raven.mongodb.repository.conventions.CustomConventions;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;


/**
 * @author yi.liang
 * @since JDK1.8
 */
public class PojoCodecRegistrys {

    public final static CodecRegistry CODEC_REGISTRY = registry();

    /**
     * @return
     */
    private static CodecRegistry registry() {

        CodecRegistry pojoCodecRegistry = MongoClient.getDefaultCodecRegistry();
        //registry ValueEnum CodecProvider
        PropertyCodecProvider propertyCodecProvider = new ValueTypePropertyCodecProvider(pojoCodecRegistry);

        CodecRegistry res = fromRegistries(MongoClient.getDefaultCodecRegistry(),
                                        fromProviders(PojoCodecProvider.builder().conventions(CustomConventions.DEFAULT_CONVENTIONS).automatic(true)
                                                .register(propertyCodecProvider).build()
                                        )
        );

        return res;
    }
}
