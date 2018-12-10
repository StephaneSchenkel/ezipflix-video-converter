package edu.esipe.i3.ezipflix.frontend.data.services;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.api.gax.rpc.ApiException;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import edu.esipe.i3.ezipflix.frontend.VideoDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
public class GooglePubSubService {

    @Value("${conversion.messaging.googlepubsub.project-id}")
    public String projectId;

    @Value("${conversion.messaging.googlepubsub.topic-id}")
    public String topicId;

    private static final Logger LOGGER = LoggerFactory.getLogger(GooglePubSubService.class);
    private Publisher publisher = null;

    private boolean initialized = false;

    public boolean isInitialized(){
        return initialized;
    }

    public void initialize(){
        ProjectTopicName topicName = ProjectTopicName.of(projectId, topicId);
        try {
            publisher = Publisher.newBuilder(topicName).build();
            initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(final String message){
        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

        // Once published, returns a server-assigned message id (unique within the topic)
        ApiFuture<String> future = publisher.publish(pubsubMessage);

        // Add an asynchronous callback to handle success / failure
        ApiFutures.addCallback(future, new ApiFutureCallback<String>() {

            public void onFailure(Throwable throwable) {
                if (throwable instanceof ApiException) {
                    ApiException apiException = ((ApiException) throwable);
                    // details on the API exception
                    LOGGER.error(apiException.getStatusCode().getCode().toString());
                }
                LOGGER.error("Error publishing message : " + message);
            }

            public void onSuccess(String messageId) {
                // Once published, returns server-assigned message ids (unique within the topic)
                LOGGER.info(messageId);
            }
        });
    }

    @PreDestroy
    public void cleanup(){
        if(publisher != null){
            try {
                publisher.shutdown();
                publisher.awaitTermination(1, TimeUnit.MINUTES);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
