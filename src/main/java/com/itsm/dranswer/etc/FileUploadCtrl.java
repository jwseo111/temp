package com.itsm.dranswer.etc;

import com.itsm.dranswer.ncp.storage.CustomObjectStorage;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.itsm.dranswer.utils.ApiUtils.ApiResult;
import static com.itsm.dranswer.utils.ApiUtils.success;

@RestController
public class FileUploadCtrl {

    @Autowired
    private CustomObjectStorage customObjectStorage;

    @PostMapping(path = "/etc/fileUpload")
    public ApiResult<FileUploadResponse> fileUpload(
            MultipartHttpServletRequest request,
            HttpServletResponse response
    ) throws Exception {

        final String endPoint = "https://kr.object.ncloudstorage.com";
        final String regionName = "kr-standard";
        String accessKey = "KCbWL7rOSXloPrSx7RLF";
        String secretKey = "2HC4LpXz6CiPWBWG0FeDmYAulQzgVxHtWcjmiWgq";

        String bucketName = "bucket-xeroman-test-01";
        String folderName = "test1334/asd/123123/";

        File tempDir = new File("/drAnswer/test");
        if(!tempDir.exists()){
            tempDir.mkdirs();
        }

        MultipartFile multipartFile = request.getFile("multipartFile");

        File targetFile = new File("/drAnswer/test/" + multipartFile.getOriginalFilename());
        try {
            InputStream fileStream = multipartFile.getInputStream();
            FileUtils.copyInputStreamToFile(fileStream, targetFile);
        } catch (IOException e) {
            FileUtils.deleteQuietly(targetFile);
            e.printStackTrace();
        }

        String objectName = multipartFile.getOriginalFilename();

        customObjectStorage.uploadObject(endPoint, regionName, accessKey, secretKey, bucketName, folderName, objectName, targetFile);

        return success(new FileUploadResponse());
    }

}