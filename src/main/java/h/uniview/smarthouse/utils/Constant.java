package h.uniview.smarthouse.utils;

public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;
	
	public enum PhotoType {
//		BMP、JPG、JPEG、PNG、GIF
		BMP("。bmp"),
		JPG("。jpg"),
		JPEG("。jpeg"),
		PNG("。png"),
		GIF("。gif");
		
		private String value;

		PhotoType(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		public static boolean isMember(String photoType) {
			for(PhotoType pt : values()) {
				if(pt.getValue().equals(photoType)) {
					return true;
				}
			}
			return false;
		}
	}

	public enum MenuType {
		/**
		 * 目录
		 */
		CATALOG(0),
		/**
		 * 菜单
		 */
		MENU(1),
		/**
		 * 按钮
		 */
		BUTTON(2);

		private int value;

		MenuType(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}

}
