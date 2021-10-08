package com.itsm.dranswer.storage;

/*
 * @package : com.itsm.dranswer.storage
 * @name : S3ObjectDto.java
 * @date : 2021-10-08 오후 2:15
 * @author : xeroman.k
 * @version : 1.0.0
 * @modifyed :
 */

import com.amazonaws.services.s3.model.S3ObjectSummary;
import lombok.*;

import java.text.SimpleDateFormat;

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
    private String lastModified;

    public S3ObjectDto(S3ObjectSummary os, String folderName) {
        this.key = os.getKey();
        this.size = os.getSize();
        this.eTag = os.getETag();
        this.storageClass = os.getStorageClass();
        this.name = this.key.replace(folderName, "");
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.lastModified = fm.format(os.getLastModified());
    }

    public S3ObjectDto(String folder, String folderName) {
        this.key = folder;
        this.size = 0L;
        this.eTag = "";
        this.storageClass = "";
        this.name = this.key.replaceFirst(folderName, "");
        this.lastModified = null;
    }
}
