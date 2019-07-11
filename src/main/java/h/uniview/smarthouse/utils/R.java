package h.uniview.smarthouse.utils;

import java.util.HashMap;
import java.util.Map;

public class R extends HashMap<String, Object> {

	private static final long serialVersionUID = -8713837118340960775L;

	// 成功
	private static final Integer SUCCESS = 0;
	// 警告
	private static final Integer WARN = 1;
	// 异常 失败
	private static final Integer FAIL = 500;

	public R() {
		put("code", SUCCESS);
		put("msg", "操作成功");
	}
	
	public static R warn(String msg) {
		R r = new R();
		r.put("code", WARN);
		r.put("msg", msg);
		return r;
	}

	public static R ok(Object msg) {
		R r = new R();
		r.put("code", SUCCESS);
		r.put("msg", msg);
		return r;
	}

	public static R ok() {
		return new R();
	}

	@Override
	public R put(String key, Object value) {
		super.put(key, value);
		return this;
	}
	
	public static R error() {
		return error(FAIL, "未知异常，请联系管理员");
	}
	
	public static R error(String msg) {
		return error(FAIL, msg);
	}
	
	public static R error(int code, String msg) {
		R r = new R();
		r.put("code", code);
		r.put("msg", msg);
		return r;
	}

	public static R ok(String msg) {
		R r = new R();
		r.put("msg", msg);
		return r;
	}
	
	public static R ok(Map<String, Object> map) {
		R r = new R();
		r.putAll(map);
		return r;
	}

}
