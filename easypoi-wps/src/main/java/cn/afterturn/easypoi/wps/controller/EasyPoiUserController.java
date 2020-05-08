package cn.afterturn.easypoi.wps.controller;

import cn.afterturn.easypoi.wps.entity.WpsUserEntity;
import cn.afterturn.easypoi.wps.entity.resreq.WpsUserRequest;
import cn.afterturn.easypoi.wps.entity.resreq.WpsUserResponse;
import cn.afterturn.easypoi.wps.service.IEasyPoiWpsUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jueyue on 20-5-8.
 */
@RestController
@RequestMapping("v1/3rd/file")
public class EasyPoiUserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EasyPoiUserController.class);

    @Autowired(required = false)
    private IEasyPoiWpsUserService userService;

    /**
     * 获取用户信息
     */
    @PostMapping("info")
    public WpsUserResponse userInfo(@RequestBody WpsUserRequest list) {
        LOGGER.info("获取用户信息param:{}", list);
        WpsUserResponse response = new WpsUserResponse();
        for (int i = 0; i < list.getIds().size(); i++) {
            if (userService != null) {
                response.getUsers().add(userService.getUser(list.getIds().get(i)));
            } else {
                response.getUsers().add(new WpsUserEntity());
            }
        }
        return response;
    }
}
