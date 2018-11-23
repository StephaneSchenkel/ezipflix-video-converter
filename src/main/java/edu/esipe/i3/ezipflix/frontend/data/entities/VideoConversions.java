package edu.esipe.i3.ezipflix.frontend.data.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Gilles GIRAUD gil on 11/4/17.
 */
@Document(collection = "video_conversions")
public class VideoConversions {
    @Id
    private String uuid;
    private String originFilename;
    private String targetFilename;
    private String bucket;

    public VideoConversions() {
    }

    public VideoConversions(String uuid, String originFilename, String targetFilename, String bucket) {
        this.uuid = uuid;
        this.originFilename = originFilename;
        this.targetFilename = targetFilename;
        this.bucket = bucket;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOriginFilename() {
        return originFilename;
    }

    public void setOriginFilename(String originFilename) {
        this.originFilename = originFilename;
    }

    public String getTargetFilename() {
        return targetFilename;
    }

    public void setTargetFilename(String targetFilename) {
        this.targetFilename = targetFilename;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String toJson() throws JsonProcessingException {
        final ObjectMapper _mapper = new ObjectMapper();
        final Map<String, String> _map = new HashMap<String, String>();
        _map.put("id", uuid);
        _map.put("originFilename", originFilename);
        _map.put("targetFilename", targetFilename);
        _map.put("bucket", bucket);
        byte [] _bytes = _mapper.writeValueAsBytes(_map);
        return new String(_bytes);
    }
}
