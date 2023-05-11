package com.chatchatabc.parking.impl.infra.oss;

import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.utils.IOUtils;
import com.aliyun.oss.model.PutObjectResult;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class OSSMockClient extends OSSClient {
    private Map<String, byte[]> fileStore = new HashMap<>();

    public OSSMockClient() {
        super("http://localhost", null, new ClientConfiguration());
    }

    @Override
    public PutObjectResult putObject(String bucketName, String key, InputStream input) throws OSSException, ClientException {
        try {
            fileStore.put(key, IOUtils.readStreamAsByteArray(input));
        } catch (Exception e) {
            throw new ClientException(e);
        }
        return new PutObjectResult();
    }
}
