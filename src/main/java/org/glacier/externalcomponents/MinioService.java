package org.glacier.externalcomponents;

import io.minio.MinioClient;
import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.PutObjectArgs;
import io.minio.GetObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.messages.Item;

/**
 * @author Mr-Glacier
 * @version 1.0
 * @apiNote minio组件测试
 * @since 2024/12/26 16:33
 */
public class MinioService {

    public String minioUrl = "http://39.107.231.234:9000";
    public String minioAccessKey = "MrGlacier";
    public String minioSecretKey = "";

    /**
     * 创建一个MinioClient实例。
     *
     * @param minioUrl       Minio服务地址;
     * @param minioAccessKey 用户名.username;
     * @param minioSecretKey 密钥.password ;
     */
    public MinioClient createMinioClient(String minioUrl, String minioAccessKey, String minioSecretKey) {
        try {
            // 创建一个 MinioClient 实例。
            return MinioClient.builder()
                    .endpoint(minioUrl)
                    .credentials(minioAccessKey, minioSecretKey)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 关闭一个MinioClient实例。
     */
    public void closeMinioClient(MinioClient minioClient) {
        try {
            minioClient.disableAccelerateEndpoint();
        }catch (Exception e){
            e.printStackTrace();

        }
    }


    /**
     * 检查桶是否存在。
     *
     * @param minioClient Client实例;
     * @param bucketName  桶名称;
     * @return true if the bucket exists, false otherwise.
     */
    public boolean checkBucketExists(MinioClient minioClient, String bucketName) {
        try {
            return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建一个桶,如果不存在,则创建。
     *
     * @param minioClient MinioClient实例 ;
     * @param bucketName  桶名称 ;
     */
    public boolean createBucket(MinioClient minioClient, String bucketName) {
        try {
            boolean isExist = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!isExist) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    public static void main(String[] args) {
        MinioService minioService = new MinioService();

        String bucketName = "glacierdiary";

        // step 1 : 创建一个 MinioClient 实例。
        MinioClient minioClient = minioService.createMinioClient(minioService.minioUrl, minioService.minioAccessKey, minioService.minioSecretKey);
        // step 2 : 检查桶是否存在。
        if (minioService.checkBucketExists(minioClient, bucketName)) {
            System.out.println("Bucket exists.");
        } else {
            System.out.println("Bucket does not exist.");
        }

        // step 3 : 创建一个桶,如果不存在,则创建。
        if (minioService.createBucket(minioClient, bucketName)) {
            System.out.println("Bucket created.");
        }else {
            System.out.println("Bucket creation failed.");
        }
        minioService.closeMinioClient(minioClient);


    }


}
