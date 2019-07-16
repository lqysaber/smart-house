package h.uniview.smarthouse.form;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class PhotoForm implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@NotNull
	private Integer pageSize;
	
	@NotNull
	private Integer currPage;
	
	@NotNull
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private String queryDate;

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurrPage() {
		return currPage;
	}

	public void setCurrPage(Integer currPage) {
		this.currPage = currPage;
	}

	public String getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(String queryDate) {
		this.queryDate = queryDate;
	}

}
