package com.sowisetech.forum.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.sowisetech.forum.model.ForumCategory;
import com.sowisetech.forum.model.ForumSubCategory;

public class ForumCategoryListExtractor implements ResultSetExtractor<List<ForumCategory>> {

	@Override
	public List<ForumCategory> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Integer, ForumCategory> map = new HashMap<Integer, ForumCategory>();
		while (rs.next()) {
			int id = rs.getInt("forumCategoryId");
			ForumCategory forumCategory = map.get(id);
			if (forumCategory == null) {
				forumCategory = new ForumCategory();
				forumCategory.setForumCategoryId(id);
				forumCategory.setName(rs.getString("name"));
				map.put(id, forumCategory);
			}

			int forumCategoryId = rs.getInt("COL_A");
			if (forumCategoryId != 0) {
				List<ForumSubCategory> subCategory = forumCategory.getForumSubCategory();
				if (subCategory == null) {
					subCategory = new ArrayList<ForumSubCategory>();
					forumCategory.setForumSubCategory(subCategory);
				}
				ForumSubCategory forumSubCategory = new ForumSubCategory();
				forumSubCategory.setForumSubCategoryId(rs.getInt("COL_A"));
				forumSubCategory.setName(rs.getString("COL_B"));
				forumSubCategory.setForumCategoryId(rs.getInt("COL_C"));
				subCategory.add(forumSubCategory);
			}
		}
		return new ArrayList<ForumCategory>(map.values());
	}

}
