package h.uniview.smarthouse.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import h.uniview.smarthouse.data.CameraInfo;
import h.uniview.smarthouse.data.ConfigMsg;
import h.uniview.smarthouse.data.EnvCfgCenter;
import h.uniview.smarthouse.data.NVRInfo;
import h.uniview.smarthouse.data.ServerNodeInfo;
import h.uniview.smarthouse.data.VideoNodeInfo;
import h.uniview.smarthouse.data.WorkstationMsg;
import h.uniview.smarthouse.exception.RRException;
import h.uniview.smarthouse.form.CameraForm;
import h.uniview.smarthouse.form.ConfigDataForm;
import h.uniview.smarthouse.form.DeviceForm;
import h.uniview.smarthouse.form.ServerForm;
import h.uniview.smarthouse.form.VideoForm;
import h.uniview.smarthouse.form.WorkStationForm;
import h.uniview.smarthouse.utils.Constant.ConfigEnvType;
import h.uniview.smarthouse.utils.Constant.DeviceType;
import h.uniview.smarthouse.utils.Constant;
import h.uniview.smarthouse.utils.R;

@Controller
public class PageController extends BaseController {
	
	@Autowired
	private EnvCfgCenter envCenter;

	@RequestMapping("pages/{url}.html")
	public String page(@PathVariable("url") String url){
		return "pages/" + url + ".html";
	}
	
	@RequestMapping(value = "/device/workstation/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R updateWorkstation(@RequestBody @Valid WorkStationForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WorkstationMsg object = objectMapper.convertValue(form, WorkstationMsg.class);
            envCenter.upadteNode(ConfigEnvType.WROKSTATION.getValue(), form.getCursor(), object);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/device/config/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R updateConfigData(@RequestBody @Valid ConfigDataForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ConfigMsg object = objectMapper.convertValue(form, ConfigMsg.class);
            envCenter.upadteNode(ConfigEnvType.CONFIGDATA.getValue(), form.getCursor(), object);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/device/camera/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R updateCamera(@RequestBody @Valid CameraForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CameraInfo object = objectMapper.convertValue(form, CameraInfo.class);
            envCenter.upadteNode(ConfigEnvType.CAMERA.getValue(), form.getCursor(), object);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    
    @RequestMapping(value = "/device/video/update", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R updateVideo(@RequestBody @Valid VideoForm form, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            VideoNodeInfo object = objectMapper.convertValue(form, VideoNodeInfo.class);
            envCenter.upadteNode(ConfigEnvType.VIDEO.getValue(), form.getCursor(), object);
            return R.ok();
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }
    
    @RequestMapping("/device/show/detail")
    @ResponseBody
	public R showDevice(@RequestBody @Valid DeviceForm form, BindingResult bindingResult){
    	if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
    	
    	try {
	    	if(DeviceType.CAMERA.getValue().equals(form.getType())) {
	    		return R.ok(retrieveDevice(form.getCursor(), CameraInfo.class));
			} 
	    	if(DeviceType.NVR.getValue().equals(form.getType())) {
	    		return R.ok(retrieveDevice(form.getCursor(), NVRInfo.class));
			} 
	    	if(DeviceType.VIDEO_BAL.getValue().equals(form.getType()) || DeviceType.VIDEO_GUN.getValue().equals(form.getType())) {
	    		return R.ok(retrieveDevice(form.getCursor(), VideoNodeInfo.class));
			} 
	    	return R.ok(retrieveDevice(form.getCursor(), ServerNodeInfo.class));
    	} catch (Exception e) {
    		return R.error(e.getMessage());
    	}
	}
    
    private <T> T retrieveDevice(String cursor, Class<T> clazz) throws Exception {
    	int idx = Integer.parseInt(cursor);
    	if(clazz.equals(CameraInfo.class)) {
    		if(envCenter.getCameraInfoList().size() < idx+1) {
    			throw new RRException("System Error");
    		}
    		return (T) envCenter.getCameraInfoList().get(idx);
    	} 
    	if(clazz.equals(NVRInfo.class)) {
    		if(envCenter.getNvrInfoList().size() < idx+1) {
    			throw new RRException("System Error");
    		}
    		return (T) envCenter.getNvrInfoList().get(idx);
    	} 
    	if(clazz.equals(VideoNodeInfo.class)) {
    		if(envCenter.getVideoInfoList().size() < idx+1) {
    			throw new RRException("System Error");
    		}
    		return (T) envCenter.getVideoInfoList().get(idx);
    	} 
		if(envCenter.getServerNodeList().size() < idx+1) {
			throw new RRException("System Error");
		}
		return (T) envCenter.getServerNodeList().get(idx);
    }
	
}

