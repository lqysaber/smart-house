package h.uniview.smarthouse.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

import h.uniview.smarthouse.data.CameraInfo;
import h.uniview.smarthouse.data.ConfigMsg;
import h.uniview.smarthouse.data.EnvCfgCenter;
import h.uniview.smarthouse.data.VideoNodeInfo;
import h.uniview.smarthouse.data.WorkstationMsg;
import h.uniview.smarthouse.form.CameraForm;
import h.uniview.smarthouse.form.ConfigDataForm;
import h.uniview.smarthouse.form.VideoForm;
import h.uniview.smarthouse.form.WorkStationForm;
import h.uniview.smarthouse.utils.Constant.ConfigEnvType;
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
	
}

