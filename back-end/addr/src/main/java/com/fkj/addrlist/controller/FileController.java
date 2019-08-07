package com.fkj.addrlist.controller;

import com.alibaba.fastjson.JSON;
import com.fkj.addrlist.entities.Equipment;
import com.fkj.addrlist.mapper.UnitMapper;
import com.fkj.addrlist.util.LoggerUtils;
import com.fkj.addrlist.util.Result;
import com.fkj.addrlist.util.ResultCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.xml.crypto.Data;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Api(tags = {"文件操作"})
@RestController
public class FileController {

    @Autowired
    UnitMapper unitMapper;

    @Value("${file.groupPath}")
    private String groupPath;

    @GetMapping("/data")
    public Date testData(){
        return new Date();
    }
    @GetMapping("/exist")
    public Result isExist(){
        File file = new File(groupPath, "group.txl");
        if (!file.exists()) {
            return new Result(5001, "文件不存在");
        }
        return Result.success();
    }
    @ApiOperation(value = "下载通信录")
    @GetMapping("/download")
    public void download(HttpServletResponse response) {

        // 设置文件名，根据业务需要替换成要下载的文件名
        File file = new File(groupPath, "group.txl");
        if (file.exists()) {
            response.setContentType("application/octet-stream");
            //application/octet-stream设置返回数据为以流的形式下载文件，这样可以实现任意格式的文件下载
            /*设置强制下载不打开
             * Content-Disposition中指定的类型是下载后文件的扩展名，并且弹出的下载对话框中的文件类型
             * 图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，保存类型以Content中设置的为准。
             * 注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。
             */
            response.addHeader("Content-Disposition", "attachment;fileName=" + "group.txl");
            // 设置文件名attachment附件
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            //FileInputStream可以从文件系统中的某个文件中获得输入字节
            BufferedInputStream bis = null;
            //BufferedInputStream和BufferedOutputStream这两个类分别是FilterInputStream和FilterOutputStream的子类，
            // 作为装饰器子类，使用它们可以防止每次读取/发送数据时进行实际的写操作，代表着使用缓冲区。
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoggerUtils.info(simpleDateFormat.format(new Date()), "wz", "系统控制管理席", "通信录服务软件", "导出通信录");
    }

    @ApiOperation(value = "打开本地通信录")
    @PostMapping(value = "/upload")
    public Result upload(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new Result(1001, "文件内容为空！");
            }
//            if (file.getSize() > 10240) {
//                return Result.error(ResultCode.DATA_IS_WRONG);
//            }
//            System.out.println(file.getOriginalFilename());
            if(!file.getOriginalFilename().endsWith("txl")){
                return new Result(1001, "文件名格式错误，请选择.txl文件");
            }
            File dest = new File(groupPath, "temp.txl");
            file.transferTo(dest);// 文件写入

            String json = FileUtils.readFileToString(new File(groupPath, "temp.txl"), "UTF-8");
            dest.delete();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            LoggerUtils.info(simpleDateFormat.format(new Date()), "wz", "系统控制管理席", "通信录服务软件", "打开本地通信录");
            return Result.success(JSON.parseArray(json, Equipment.class));
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        LoggerUtils.info(simpleDateFormat.format(new Date()), "wz", "系统控制管理席", "通信录服务软件", "打开本地通信录");
        return Result.error(ResultCode.DATA_IS_WRONG);
    }
}
