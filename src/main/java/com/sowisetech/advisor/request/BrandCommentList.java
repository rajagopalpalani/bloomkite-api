package com.sowisetech.advisor.request;
import java.util.List;
import com.sowisetech.advisor.model.BrandsComment;

public class BrandCommentList {
	   long totalRecords;
	   List<BrandsComment> brandCommentList;

	   public long getTotalRecords() {
	          return totalRecords;
	   }

	   public void setTotalRecords(long totalRecords) {
	          this.totalRecords = totalRecords;
	   }

	   public List<BrandsComment> getBrandCommentList() {
	          return brandCommentList;
	   }

	   public void setBrandCommentList(List<BrandsComment> brandCommentList) {
	          this.brandCommentList = brandCommentList;
	   }
}

