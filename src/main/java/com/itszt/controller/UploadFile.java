package com.itszt.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传以及文件类型检查
 */
@Slf4j
@RestController
public class UploadFile {


    /**
     * 校验图片格式 JPG、PNG、JPEG
     */
    private static Map<String, String> FILE_TYPE_MAP = new HashMap<>();

    static {
        FILE_TYPE_MAP.put("jpg", "FFD8FF"); //JPG
        FILE_TYPE_MAP.put("png", "89504E47"); //PNG
        FILE_TYPE_MAP.put("jpeg", "FFD8FF"); //JPEG
    }

    /**
     * 获得文件头部字符串
     */
    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        log.info("当前文件头文件字节码为{}", stringBuilder.toString());
        return stringBuilder.toString();
    }

    /**
     * 比对图片类型
     * <p>
     * //@param headcode  图片头字节码
     *
     * @param imageType 图片类型
     * @param src       图片字节码
     * @return
     */
    public boolean comparePicType(String imageType, byte[] src) {
        String headcode = bytesToHexString(src);
        boolean b = FILE_TYPE_MAP.containsKey(imageType);
        if (b) {
            String head = FILE_TYPE_MAP.get(imageType).toLowerCase();
            String filehead = headcode.toLowerCase();
            if (filehead.contains(head)) {
                log.info("当前图片类型为指定类型需要,imageType:{},head：{},filehead:{}", imageType, head, filehead);
                return true;
            }
        }
        log.info("当前图片类型为不符合指定要求");
        return false;
    }

    /**
     * 附件上传
     *
     * @param files
     * @return
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
//    public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file, @RequestParam("attaType") String attaType, @RequestParam("headId") String headId) {
    public void upload(@RequestParam("files") MultipartFile[] files, HttpServletRequest request) throws IOException {
//        String separator = File.separator + "";
//        String realPath = request.getSession().getServletContext().getRealPath("");
//        realPath = realPath.substring(0, realPath.indexOf(separator));
//        log.info("java项目代码盘符为:{}", realPath);
        byte[] bytes = files[0].getBytes();
        long size = files[0].getSize();
        String name = files[0].getName();
        System.out.println("name = " + name);
        String fileName = files[0].getOriginalFilename();
        String type = fileName.substring(fileName.lastIndexOf(".") + 1);
        log.info("upload  image name:{},size:{}, type:{}", fileName, size, type);
        boolean m = checkFileSize(size, 1, "M");
        System.out.println("m = " + m);
        boolean b = comparePicType(type, bytes);
        System.out.println("b = " + b);
        String disk = getPath(UploadFile.class);
        log.info("file name:{}", fileName);
        String fileUrl = disk + "/data/k-credit/data/opinion/uploadfile/path/";
        String filela = fileName.substring(fileName.lastIndexOf("."), fileName.length());//文件后缀
        String filef = fileName.substring(0, fileName.lastIndexOf("."));//文件前缀
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String subname = simpleDateFormat.format(new Date());
        fileName = filef + subname + filela;//新的文件名
        String filePath = fileUrl + fileName;
        File dest = new File(filePath);
        try {
            if (!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
                dest.createNewFile();
            }
            files[0].transferTo(dest);
            log.info("上传成功");
        } catch (IOException e) {
            log.error(">>>>>>>>>>>>>>>>>附件上传失败：{}", e);
        }
    }

    /**
     * 得到某一个类的盘符
     *
     * @param name
     * @return
     */
    public String getPath(Class name) {
        String strResult = null;
//        String separator = File.separator + "";
        if (System.getProperty("os.name").toLowerCase().indexOf("window") > -1) {
            String window = name.getResource("/").toString().replace("file:/", "")
                    .replace("%20", " ");
            strResult = window.substring(0, window.indexOf("/"));
        } else {
//            String linux = name.getResource("/").toString().replace("file:", "")
//                    .replace("%20", " ");
            strResult = "";
        }

        return strResult;

    }


    /**
     * 判断文件大小
     *
     * @param len  文件长度
     * @param size 限制大小
     * @param unit 限制单位（B,K,M,G）
     * @return
     */
    public boolean checkFileSize(Long len, int size, String unit) {
        double fileSize = 0;
        if ("B".equals(unit.toUpperCase())) {
            fileSize = (double) len;
        } else if ("K".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1024;
        } else if ("M".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1048576;
        } else if ("G".equals(unit.toUpperCase())) {
            fileSize = (double) len / 1073741824;
        }
        if (fileSize > size) {
            log.info("当前文件大小超过1M!");
            return false;
        }
        return true;
    }


}
