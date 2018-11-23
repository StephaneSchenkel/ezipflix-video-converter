package edu.esipe.i3.ezipflix.frontend;

import java.net.URI;

/**
 * Created by Gilles GIRAUD gil on 11/4/17.
 */
public class ConversionRequest {

    private String filename;
    private String bucket;

    public ConversionRequest() {
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }
}
