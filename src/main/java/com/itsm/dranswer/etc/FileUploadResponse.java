package com.itsm.dranswer.etc;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {

    private Integer totFileCnt;

    private Long totFileSize;

    private List<FileObject> listObject = new ArrayList<>();

    @Getter
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FileObject{
        private String keyName;
        private String orgFileName;
    }

}
