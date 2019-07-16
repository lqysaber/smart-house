package h.uniview.smarthouse.controller;

import h.uniview.smarthouse.form.PhotoForm;
import h.uniview.smarthouse.utils.FileUtils;
import h.uniview.smarthouse.utils.PageUtils;
import h.uniview.smarthouse.utils.R;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PhotoController extends BaseController {
	
	@RequestMapping(value = "/photo/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R list(@RequestBody @Valid PhotoForm photoForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
		String path = "C:\\Users\\HugoLi\\Downloads\\子骏\\入册\\";
        try {
            List<String> result = FileUtils.listFile(path);
            PageUtils pg = new PageUtils(result, result.size(), photoForm.getPageSize(), photoForm.getCurrPage());
            return R.ok(pg);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

//    @RequestMapping(value = "/photo/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
//    public R list(int pageSize, int currPage, String queryDate) {
//        String path = "C:\\Users\\HugoLi\\Downloads\\子骏\\入册\\";
//        try {
//            List<String> result = FileUtils.listFile(path);
//            PageUtils pg = new PageUtils(result, result.size(), pageSize, currPage);
//            return R.ok(pg);
//        } catch (Exception e) {
//            return R.error(e.getMessage());
//        }
//    }

}
