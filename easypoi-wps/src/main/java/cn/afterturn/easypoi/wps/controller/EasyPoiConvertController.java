package cn.afterturn.easypoi.wps.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import cn.afterturn.easypoi.wps.service.IEasyPoiWpsConvertService;
/**
 * @author JueYue
 * @date 2021-06-21-6-1
 * @since 1.0
 */
@RestController
@RequestMapping("easypoi/wps/v1/file/convert")
public class EasyPoiConvertController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyPoiConvertController.class);

    @GetMapping("version/{version}")
    public byte[] fileVersion(IEasyPoiWpsConvertService service, String taskId , String srcUri , String fileName , String exportType) throws Exception {
        boolean isOk = service.fileConvert(taskId,srcUri,fileName,exportType,null);
        int timeout = 0;
        byte[] bytes = null;
        while (isOk) {
            try {
                bytes = service.getConvertFile(taskId);
            } catch (Exception e) {}
            if (bytes != null || timeout > 120) {
                isOk = false;
            }
            timeout ++;
            Thread.sleep(1000);
        }
        return bytes;
    }
}
