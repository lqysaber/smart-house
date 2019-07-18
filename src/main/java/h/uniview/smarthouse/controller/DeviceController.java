package h.uniview.smarthouse.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import h.uniview.smarthouse.data.CameraInfo;
import h.uniview.smarthouse.data.EnvCfgCenter;
import h.uniview.smarthouse.utils.PageUtils;
import h.uniview.smarthouse.utils.R;

@RestController
public class DeviceController {
	
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
	
}
