package h.uniview.smarthouse.form;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
