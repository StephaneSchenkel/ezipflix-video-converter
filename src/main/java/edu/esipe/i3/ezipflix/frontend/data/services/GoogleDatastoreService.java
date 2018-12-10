package edu.esipe.i3.ezipflix.frontend.data.services;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.*;
import edu.esipe.i3.ezipflix.frontend.data.entities.VideoConversions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleDatastoreService {

    @Value("${conversion.messaging.googlepubsub.project-id}")
    public String projectId;

    @Value("${conversion.data.google.entity-name}")
    public String entityName;

    private static final Logger LOGGER = LoggerFactory.getLogger(GooglePubSubService.class);
    private Datastore datastore = null;
    private KeyFactory keyFactory = null;

    private boolean initialized = false;

    public boolean isInitialized(){
        return initialized;
    }

    public void initialize(){
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setKind(entityName);
    }

    public Long save(final VideoConversions conversion){
        Key key = datastore.allocateId(keyFactory.newKey());
        Entity videoConversionEntity = Entity.newBuilder(key)
                .set("originFilename", conversion.getOriginFilename())
                .set("bucket", conversion.getBucket())
                .set("tstamp", Timestamp.now())
                .build();
        Entity entity = datastore.put(videoConversionEntity);
        return entity.getKey().getId();
    }

}
