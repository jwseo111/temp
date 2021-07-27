package com.itsm.dranswer.storage;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class S3ObjectDto {

    private String key;
    private String name;
    private Long size;
    private String eTag;
    private String storageClass;

    public S3ObjectDto(S3ObjectSummary os, String folderName) {
        this.key = os.getKey();
        this.size = os.getSize();
        this.eTag = os.getETag();
        this.storageClass = os.getStorageClass();
        this.name = this.key.replace(folderName, "");
    }

    public S3ObjectDto(String folder, String folderName) {
        this.key = folder;
        this.size = 0L;
        this.eTag = "";
        this.storageClass = "";
        this.name = this.key.replaceFirst(folderName, "");
    }
}
