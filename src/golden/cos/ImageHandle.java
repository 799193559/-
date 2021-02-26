package golden.cos;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.exception.MultiObjectDeleteException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.region.Region;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ImageHandle {

    private static final Logger logger = LoggerFactory.getLogger(ImageHandle.class);

    /**
     * 上传图片
     * @param file
     * @return
     * @throws IOException
     */
    public static String uploadImageToCos(MultipartFile file) throws IOException {
        if (file != null) {
            // 获取文件内容类型
            String contentType = file.getContentType();
            // 获取上传文件的文件流
            InputStream inputStream = file.getInputStream();
            // 获取文件的字节数
            long size = file.getSize();
            // 判断文件是否为空
            if (size != 0) {
                // 初始化用户身份信息(secretId, secretKey)
                // secretId，secretKey 是腾讯 COS 的一个账号和密码
                COSCredentials cred = new BasicCOSCredentials(COSConfig.SECRETID, COSConfig.SECRETKEY);
                // 设置bucket的区域, COS地域的简称，请参照：
                // https://www.qcloud.com/document/product/436/6224
                ClientConfig clientConfig = new ClientConfig(new Region(COSConfig.REGION));
                // 生成cos客户端
                COSClient cosclient = new COSClient(cred, clientConfig);
                // bucket名需包含appid
                String bucketName = COSConfig.BUCKETNAME;

                //获取后缀名
                String[] split = contentType.split("/");
                //文件名
                String uuid = UUID.randomUUID().toString().replaceAll("-", "");

                //当前日期
                Date date = new Date();
                DateConvert util = new DateConvert();
                String dd = util.toString(date);

                //目标文件名
                String key = "/hanshi" + "/" + dd + "/" + uuid + "." + split[1];
                String imgName = key.substring(1);

                ObjectMetadata objectMetadata = new ObjectMetadata();
                // 从输入流上传必须制定content length
                // 否则http客户端可能会缓存所有数据，存在内存OOM的情况
                objectMetadata.setContentLength(size);
                // 默认下载时根据cos路径key的后缀返回响应的contenttype
                // 上传时设置contenttype会覆盖默认值
                objectMetadata.setContentType(contentType);

                PutObjectRequest putObjectRequest =
                        new PutObjectRequest(bucketName, key, inputStream, objectMetadata);
                // 设置存储类型, 默认是标准(Standard), 低频(standard_ia), 近线(nearline)
                putObjectRequest.setStorageClass(StorageClass.Standard);
                try {
                    //将图片上传到 COS
                    cosclient.putObject(putObjectRequest);
                } catch (CosServiceException e) {
                    e.printStackTrace();
                } catch (CosClientException e) {
                    e.printStackTrace();
                }
                // 关闭客户端
                cosclient.shutdown();
                // 返回的只是图片在COS中存储的路径，在线返回COS的链接需要自己拼接上
                //比如："https://hstc-image-1254334144.cos.ap-guangzhou.myqcloud.com/"+imgName
                return imgName;
            }
        }
        return "";
    }

    /**
     * 删除图片(不带版本号, 即bucket未开启多版本)
     * @param imgName
     * @return
     */
    public static Boolean deleteImageFromCos(String imgName){
        // 初始化用户身份信息(secretId, secretKey)
        COSCredentials cred = new BasicCOSCredentials(COSConfig.SECRETID, COSConfig.SECRETKEY);
        // 设置bucket的区域, COS地域的简称请参照 https://www.qcloud.com/document/product/436/6224
        ClientConfig clientConfig = new ClientConfig(new Region(COSConfig.REGION));
        // 生成cos客户端
        COSClient cosclient = new COSClient(cred, clientConfig);
        // bucket名需包含appid
        String bucketName = COSConfig.BUCKETNAME;

        DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketName);
        // 设置要删除的key列表, 最多一次删除1000个
        ArrayList<DeleteObjectsRequest.KeyVersion> keyList = new ArrayList<>();

        // 传入要删除的文件名
        keyList.add(new DeleteObjectsRequest.KeyVersion(imgName));
        //============================================
        //如果一次性删除多张图片的话，可以把传入的参数改为 List，再循环赋值给 keyList
		//keyList.add(new KeyVersion("2018-04-19/99440ec2800e4a3baef9e84c662a546d.plain"));
		//keyList.add(new KeyVersion("2018-04-18/4b566aef3b5d4b28bd12d6e32a72dad9.jpeg"));
		//keyList.add(new KeyVersion("2018-04-18/tttt"));
        //============================================
        deleteObjectsRequest.setKeys(keyList);
        // 批量删除文件
        try {
            DeleteObjectsResult deleteObjectsResult = cosclient.deleteObjects(deleteObjectsRequest);
            List<DeleteObjectsResult.DeletedObject> deleteObjectResultArray = deleteObjectsResult.getDeletedObjects();
        } catch (MultiObjectDeleteException mde) { // 如果部分产出成功部分失败,
            // 返回MultiObjectDeleteException
            List<DeleteObjectsResult.DeletedObject> deleteObjects = mde.getDeletedObjects();
            List<MultiObjectDeleteException.DeleteError> deleteErrors = mde.getErrors();
            // 关闭客户端
            cosclient.shutdown();
            return false;
        } catch (CosServiceException e) { // 如果是其他错误, 比如参数错误，
            // 身份验证不过等会抛出CosServiceException
            logger.error("参数错误");
            e.printStackTrace();
            // 关闭客户端
            cosclient.shutdown();
            return false;
        } catch (CosClientException e) { // 如果是客户端错误，比如连接不上COS
            logger.error("客户端错误，比如连接不上COS");
            e.printStackTrace();
            // 关闭客户端
            cosclient.shutdown();
            return false;
        }
        // 关闭客户端
        cosclient.shutdown();
        return true;
    }
}
