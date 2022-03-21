package cn.afterturn.easypoi.wps.controller;

import cn.afterturn.easypoi.wps.entity.WpsToken;
import cn.afterturn.easypoi.wps.entity.resreq.*;
import cn.afterturn.easypoi.wps.service.IEasyPoiWpsViewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * @author jueyue on 20-5-7.
 */
@RestController
@RequestMapping("easypoi/wps/v1/3rd/file")
public class EasyPoiFileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyPoiFileController.class);

    @Autowired(required = false)
    private IEasyPoiWpsViewService easyPoiWpsService;

    /**
     * 获取网络文件预览URL
     *
     * @param filePath
     * @return t
     */
    @GetMapping("getViewUrl")
    public WpsToken getViewUrl(String filePath) {
        LOGGER.info("getViewUrlWebPath：filePath={}", filePath);
        WpsToken token = easyPoiWpsService.getViewUrl(decode(filePath));
        return token;
    }

    /**
     * 获取文件元数据
     */
    @GetMapping("info")
    public WpsFileResponse getFileInfo(HttpServletRequest request, @RequestParam(value = "_w_userid", required = false) String userId,
                                       @RequestParam("_w_filepath") String filePath) {
        LOGGER.info("获取文件元数据userId:{},path:{}", userId, filePath);
        String          fileId       = request.getHeader("x-weboffice-file-id");
        WpsFileResponse fileResponse = easyPoiWpsService.getFileInfo(userId, decode(filePath), fileId);
        return fileResponse;
    }

    /**
     * 通知此文件目前有哪些人正在协作
     */
    @PostMapping("online")
    public WpsResponse fileOnline(HttpServletRequest request, @RequestBody WpsUserRequest list) {
        LOGGER.info("通知此文件目前有哪些人正在协作param:{}", list);
        String fileId = request.getHeader("x-weboffice-file-id");
        easyPoiWpsService.fileOnline(fileId, list);
        return WpsResponse.success();
    }

    /**
     * 上传文件新版本
     */
    @PostMapping("save")
    public WpsFileSaveResponse fileSave(HttpServletRequest request,
                                        @RequestParam(value = "_w_userid", required = false) String userId,
                                        @RequestBody MultipartFile file) {
        LOGGER.info("上传文件新版本");
        String fileId = request.getHeader("x-weboffice-file-id");
        return new WpsFileSaveResponse(easyPoiWpsService.fileSave(fileId, userId, file));
    }

    /**
     * 获取特定版本的文件信息
     */
    @GetMapping("version/{version}")
    public WpsFileSaveResponse fileVersion(HttpServletRequest request, @PathVariable Integer version,
                                           @RequestParam("_w_filepath") String filePath) {
        LOGGER.info("获取特定版本的文件信息version:{}", version);
        String fileId = request.getHeader("x-weboffice-file-id");
        return new WpsFileSaveResponse(easyPoiWpsService.getFileInfoOfVersion(fileId, decode(filePath), version));
    }

    /**
     * 文件重命名
     */
    @PutMapping("rename")
    public WpsResponse fileRename(HttpServletRequest request, @RequestBody WpsRenameRequest req,
                                  @RequestParam(value = "_w_userid", required = false) String userId) {
        LOGGER.info("文件重命名param:{},userId:{}", req, userId);
        String fileId = request.getHeader("x-weboffice-file-id");
        easyPoiWpsService.rename(fileId, userId, req.getName());
        return WpsResponse.success();
    }

    /**
     * 获取所有历史版本文件信息
     */
    @PostMapping("history")
    public WpsFileHistoryResponse fileHistory(@RequestBody WpsFileHistoryRequest req) {
        LOGGER.info("获取所有历史版本文件信息param:{}", req);
        return easyPoiWpsService.getHistory(req);
    }

    /**
     * 新建文件
     */
    @PostMapping("new")
    public WpsResponse fileNew(@RequestBody MultipartFile file, @RequestParam(value = "_w_userid", required = false) String userId) {
        LOGGER.info("新建文件,只打印不实现,userid:{}", userId);
        return WpsResponse.success();
    }

    /**
     * 回调通知
     */
    @PostMapping("onnotify")
    public WpsResponse onNotify(@RequestBody Map obj) {
        LOGGER.info("回调通知,只打印不实现,param:{}", obj);
        return WpsResponse.success();
    }

    public String decode(String filePath) {
        try {
            filePath = URLDecoder.decode(filePath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return filePath;
    }

}
