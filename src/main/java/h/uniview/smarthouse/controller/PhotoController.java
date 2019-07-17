package h.uniview.smarthouse.controller;

import h.uniview.smarthouse.form.PhotoForm;
import h.uniview.smarthouse.utils.FileUtils;
import h.uniview.smarthouse.utils.PageUtils;
import h.uniview.smarthouse.utils.R;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.List;

@RestController
public class PhotoController extends BaseController {
	
	String path = "C:\\Users\\HugoLi\\Downloads\\子骏\\入册\\";
	@RequestMapping(value = "/photo/list", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R list(@RequestBody @Valid PhotoForm photoForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
		
        try {
            List<String> result = FileUtils.listFile(path);
            PageUtils pg = new PageUtils(result, photoForm.getPageSize(), photoForm.getCurrPage());
            return R.ok(pg);
        } catch (Exception e) {
            return R.error(e.getMessage());
        }
    }

	 /**
     * 处理图片显示请求
     * @param fileName
     */
    @RequestMapping("/showPic/{fileName}")
    public void showPicture(@PathVariable("fileName") String fileName,  HttpServletResponse response){
        File imgFile = new File(path+fileName);
        responseFile(response, imgFile);
    }

    /**
     * 处理图片下载请求
     * @param fileName
     * @param response
     */
    @RequestMapping("/downloadPic/{fileName}")
    public void downloadPicture(@PathVariable("fileName") String fileName, HttpServletResponse response){
        // 设置下载的响应头信息
        response.setHeader("Content-Disposition", "attachment;fileName=" + "headPic.jpg");
        File imgFile = new File(path+fileName);
        responseFile(response, imgFile);
    }

    /**
     * 响应输出图片文件
     * @param response
     * @param imgFile
     */
    private void responseFile(HttpServletResponse response, File imgFile) {
        try(InputStream is = new FileInputStream(imgFile);
            OutputStream os = response.getOutputStream();){
            byte [] buffer = new byte[1024]; // 图片文件流缓存池
            while(is.read(buffer) != -1){
                os.write(buffer);
            }
            os.flush();
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

}
