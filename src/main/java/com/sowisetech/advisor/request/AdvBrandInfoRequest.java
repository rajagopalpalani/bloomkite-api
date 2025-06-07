package com.sowisetech.advisor.request;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class AdvBrandInfoRequest {

	int screenId;
	String advId;
	List<AdvBrandInfoReq> brandInfoReqList;

	public int getScreenId() {
		return screenId;
	}

	public void setScreenId(int screenId) {
		this.screenId = screenId;
	}

	public String getAdvId() {
		return advId;
	}

	public void setAdvId(String advId) {
		this.advId = advId;
	}

	public List<AdvBrandInfoReq> getBrandInfoReqList() {
		return brandInfoReqList;
	}

	public void setBrandInfoReqList(List<AdvBrandInfoReq> brandInfoReqList) {
		this.brandInfoReqList = brandInfoReqList;
	}

	// long advBrandId;
	// String advId;
	// List<ProductBrand> productBrand;
	//
	// public List<ProductBrand> getProductBrand() {
	// return productBrand;
	// }
	//
	// public void setProductBrand(List<ProductBrand> productBrand) {
	// this.productBrand = productBrand;
	// }
	//
	// public long getAdvBrandId() {
	// return advBrandId;
	// }
	//
	// public void setAdvBrandId(long advBrandId) {
	// this.advBrandId = advBrandId;
	// }
	//
	// public String getAdvId() {
	// return advId;
	// }
	//
	// public void setAdvId(String advId) {
	// this.advId = advId;
	// }

}
