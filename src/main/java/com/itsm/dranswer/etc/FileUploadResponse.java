package com.itsm.dranswer.etc;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResponse {

    private Integer totFileCnt;

    private Long totFileSize;

}
