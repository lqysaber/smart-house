package h.uniview.smarthouse.controller;

import javax.validation.Valid;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import h.uniview.smarthouse.form.PhotoForm;
import h.uniview.smarthouse.utils.R;

@RestController(value = "photo")
public class PhotoController extends BaseController {
	
	@RequestMapping(value = "queryInventory", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public R list(@RequestBody @Valid PhotoForm photoForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
            return R.error(handleError(bindingResult));
        }
        try {
            return R.ok();
        } catch (UnknownAccountException | IncorrectCredentialsException | LockedAccountException e) {
            return R.error(e.getMessage());
        } catch (AuthenticationException e) {
            return R.error("认证失败！");
        }
    }

}
