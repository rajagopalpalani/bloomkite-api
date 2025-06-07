package com.sowisetech.advisor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.sowisetech.advisor.model.CityList;
import com.sowisetech.advisor.model.Pincode;
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.Service;

public class CityExtractor implements ResultSetExtractor<List<CityList>> {

	@Override
	public List<CityList> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<String, CityList> map = new LinkedHashMap<String, CityList>();
		while (rs.next()) {
			String city = rs.getString("COL_C");
			CityList cityList = map.get(city);
			if (cityList == null) {
				cityList = new CityList();
				cityList.setCity(rs.getString("COL_C"));
				cityList.setStateId(rs.getString("COL_B"));
				cityList.setState(rs.getString("state"));
				map.put(city, cityList);
			}
			int type = rs.getInt("IS_CITY");
			if (type == 1) {
				List<String> pincodeList = cityList.getPincodes();
				if (pincodeList == null) {
					pincodeList = new ArrayList<String>();
					cityList.setPincodes(pincodeList);
				}
				if (rs.getLong("COL_A") != 0) {
					String pincode = rs.getString("COL_D");
					pincodeList.add(pincode);
				}
			}
		}
		return new ArrayList<CityList>(map.values());
	}

}
