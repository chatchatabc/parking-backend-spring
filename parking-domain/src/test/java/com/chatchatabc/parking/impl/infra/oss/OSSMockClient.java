package com.chatchatabc.parking.impl.infra.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class OSSMockClient extends OSSClient {
    private final Map<String, OssObjectMock> fileStore = new HashMap<>();

    record OssObjectMock(String bucketName, String key, byte[] content) {
    }


    public OSSMockClient() {
        super("http://localhost", null, new ClientConfiguration());
    }

    @Override
    public PutObjectResult putObject(String bucketName, String key, InputStream input) throws OSSException, ClientException {
        try {
            fileStore.put(key, new OssObjectMock(bucketName, key, IOUtils.readStreamAsByteArray(input)));
        } catch (Exception e) {
            throw new ClientException(e);
        }
        return new PutObjectResult();
    }

    @Override
    public OSSObject getObject(String bucketName, String key) throws OSSException, ClientException {
        if (Objects.equals(key, "health.txt")) {
            final OSSObject ossObject = new OSSObject();
            ossObject.setBucketName(bucketName);
            ossObject.setKey(key);
            return ossObject;
        }
        if (fileStore.containsKey(key)) {
            var item = fileStore.get(key);
            OSSObject ossObject = new OSSObject();
            ossObject.setKey(key);
            ossObject.setBucketName(item.bucketName());
            ossObject.setObjectContent(new ByteArrayInputStream(item.content()));
            return ossObject;
        }
        return null;
    }
}
