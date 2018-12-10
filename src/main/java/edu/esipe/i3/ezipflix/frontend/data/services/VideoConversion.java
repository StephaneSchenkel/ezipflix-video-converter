package edu.esipe.i3.ezipflix.frontend.data.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import edu.esipe.i3.ezipflix.frontend.ConversionRequest;
import edu.esipe.i3.ezipflix.frontend.ConversionResponse;
import edu.esipe.i3.ezipflix.frontend.data.entities.VideoConversions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Gilles GIRAUD gil on 11/4/17.
 */
@Service
public class VideoConversion {

    private static final Logger LOGGER = LoggerFactory.getLogger(VideoConversion.class);

    @Autowired GoogleDatastoreService googleDatastoreService;
    @Autowired GooglePubSubService googlePubSubService;

    public void save(
                final ConversionRequest request,
                final ConversionResponse response) throws JsonProcessingException {

        final VideoConversions conversion = new VideoConversions(
                                                    response.getUuid().toString(),
                                                    request.getFilename(),
                                                    "",
                                                    request.getBucket());


        if(!googleDatastoreService.isInitialized())
            googleDatastoreService.initialize();
        String id = String.valueOf(googleDatastoreService.save(conversion));

        LOGGER.info("VideoConversions saved with id : " + id);

        conversion.setUuid(id);
        if(!googlePubSubService.isInitialized())
            googlePubSubService.initialize();
        googlePubSubService.send(conversion.toJson());
    }

    public boolean fileExists(final String fileName, final String bucket){
        boolean result = false;

        final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();

        ObjectListing ol = s3.listObjects(bucket);
        List<S3ObjectSummary> objects = ol.getObjectSummaries();
        int i = 0;
        while(i < objects.size() && !objects.get(i).getKey().equals(fileName))
            i++;
        if(i < objects.size())
            result = true;

        return result;
    }

}
