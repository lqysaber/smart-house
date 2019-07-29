package h.uniview.smarthouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import h.uniview.smarthouse.data.*;
import h.uniview.smarthouse.form.CameraForm;
import h.uniview.smarthouse.form.NVRForm;
import h.uniview.smarthouse.form.ServerForm;
import h.uniview.smarthouse.form.VideoForm;
import h.uniview.smarthouse.utils.Constant;
import h.uniview.smarthouse.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
        	
//            PageUtils pg = new PageUtils(result, result.size(), 10, 1);
            return R.ok(result);
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
//            result.addAll(envCenter.getNvrInfoList());
            result.addAll(envCenter.getVideoInfoList());

            r.put("video", result);
            return r;
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/device/server/add", method = RequestMethod.POST)
    @ResponseBody
    public R createServerNode(@Validated @RequestBody ServerForm serverForm, BindingResult bindingResult) {
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
    public R createCamerNode(@Validated @RequestBody CameraForm cameraForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CameraInfo cameraNode = objectMapper.convertValue(cameraForm, CameraInfo.class);
            envCenter.crateNode(cameraNode, Constant.ConfigEnvType.CAMERA.getValue());

            R r = R.ok();
            r.put("camera", envCenter.getCameraInfoList());
            return r;
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    
    @RequestMapping(value = "/device/camera/del")
    @ResponseBody
    public R deleteCameraNode(String cursor) {
        try {
            envCenter.deleteNode(Constant.ConfigEnvType.CAMERA.getValue(), Integer.parseInt(cursor));
            R r = R.ok();
            r.put("camera", envCenter.getCameraInfoList());
            return r;
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/device/video/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R createVideo(@Validated @RequestBody VideoForm videoForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            VideoNodeInfo object = objectMapper.convertValue(videoForm, VideoNodeInfo.class);
            envCenter.crateNode(object, Constant.ConfigEnvType.VIDEO.getValue());
            R r = R.ok();
            r.put("video", envCenter.getVideoInfoList());
            return r;
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/device/nvr/add", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R createNVR(@Validated @RequestBody NVRForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            NVRInfo object = objectMapper.convertValue(form, NVRInfo.class);
            envCenter.crateNode(object, Constant.ConfigEnvType.NVR.getValue());
            R r = R.ok();
            r.put("video", envCenter.getVideoInfoList());
            return r;
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    
    @RequestMapping(value = "/device/video/del")
    @ResponseBody
    public R deleteVideoNode(String cursor) {
        try {
            envCenter.deleteNode(Constant.ConfigEnvType.VIDEO.getValue(), Integer.parseInt(cursor));
            R r = R.ok();
            r.put("video", envCenter.getVideoInfoList());
            return r;
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

    @RequestMapping(value = "/nvr/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R loginNVR() {
        try {
            return R.ok(envCenter.getNvrInfoList().get(0));
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

}
