package h.uniview.smarthouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import h.uniview.smarthouse.data.CameraInfo;
import h.uniview.smarthouse.data.EnvCfgCenter;
import h.uniview.smarthouse.data.ServerNodeInfo;
import h.uniview.smarthouse.form.CameraForm;
import h.uniview.smarthouse.form.ServerForm;
import h.uniview.smarthouse.utils.Constant;
import h.uniview.smarthouse.utils.PageUtils;
import h.uniview.smarthouse.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class DeviceController extends BaseController {
	
	@Autowired
	private EnvCfgCenter envCenter;
	
	@RequestMapping(value = "/device/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R list() {
        try {
        	List<CameraInfo> result = new ArrayList<CameraInfo>();
        	result.addAll(envCenter.getCameraInfoList());
        	result.addAll(envCenter.getNvrInfoList());
        	result.addAll(envCenter.getVideoInfoList());
        	
            PageUtils pg = new PageUtils(result, result.size(), 10, 1);
            return R.ok(pg);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
	
	@RequestMapping(value = "/device/show", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
	public R show() {
		try {
        	Map<String, Object> result = new HashMap<String, Object>();
        	result.put("workStation", envCenter.getWorkstationMsg());
        	result.put("cfgInfo", envCenter.getConfigMsg());
            return R.ok(result);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
	}

    @RequestMapping(value = "/device/network", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
	public R listNetwork() {
        try {
            R r = R.ok();
            r.put("server", envCenter.getServerNodeList());
            r.put("camera", envCenter.getCameraInfoList());

            List<CameraInfo> result = new ArrayList<CameraInfo>();
            result.addAll(envCenter.getNvrInfoList());
            result.addAll(envCenter.getVideoInfoList());

            r.put("video", result);
            return r;
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/device/server/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R createServerNode(@RequestBody @Valid ServerForm serverForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServerNodeInfo serverNode = objectMapper.convertValue(serverForm, ServerNodeInfo.class);
            envCenter.crateNode(serverNode, Constant.ConfigEnvType.SERVERNODE.getValue());
            
            R r = R.ok();
            r.put("server", envCenter.getServerNodeList());
            return r;
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    
    @RequestMapping(value = "/device/server/del")
    @ResponseBody
    public R deleteServerNode(String cursor) {
        try {
            envCenter.deleteNode(Constant.ConfigEnvType.SERVERNODE.getValue(), Integer.parseInt(cursor));
            R r = R.ok();
            r.put("server", envCenter.getServerNodeList());
            return r;
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/device/camera/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R createCamerNode(@RequestBody @Valid CameraForm cameraForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CameraInfo cameraNode = objectMapper.convertValue(cameraForm, CameraInfo.class);
            envCenter.crateNode(cameraNode, Constant.ConfigEnvType.CAMERA.getValue());
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    
    @RequestMapping(value = "/device/camera/del")
    @ResponseBody
    public R deleteCameraNode(String cursor) {
        try {
            envCenter.deleteNode(Constant.ConfigEnvType.SERVERNODE.getValue(), Integer.parseInt(cursor));
            R r = R.ok();
            r.put("server", envCenter.getServerNodeList());
            return r;
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/device/video/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R createVideo(@RequestBody @Valid CameraForm cameraForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CameraInfo cameraNode = objectMapper.convertValue(cameraForm, CameraInfo.class);
            envCenter.crateNode(cameraNode, Constant.ConfigEnvType.CAMERA.getValue());
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/video/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R listVideo() {
        try {
            return R.ok(envCenter.getVideoInfoList());
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

}
