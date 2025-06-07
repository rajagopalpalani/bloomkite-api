package com.sowisetech.advisor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.sowisetech.advisor.model.City;
import com.sowisetech.advisor.model.CityPincode;
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.State;
import com.sowisetech.advisor.model.StateCity;

//public class StateExtractor implements ResultSetExtractor<List<StateCity>> {
//
//	@Override
//	public List<StateCity> extractData(ResultSet rs) throws SQLException, DataAccessException {
//		Map<Long, StateCity> map = new HashMap<Long, StateCity>();
//		while (rs.next()) {
//			Long id = rs.getLong("stateId");
//			StateCity stateCity = map.get(id);
//			if (stateCity == null) {
//				stateCity = new StateCity();
//				stateCity.setStateId(id);
//				stateCity.setState(rs.getString("state"));
//
//				map.put(id, stateCity);
//			}
//
//			int type = rs.getInt("IS_CITY");
//			if (type == 1) {
//				List<CityPincode> cityPincodeList = stateCity.getCities();
//				if (cityPincodeList == null || cityPincodeList.size() == 0) {
//					cityPincodeList = new ArrayList<CityPincode>();
//					stateCity.setCities(cityPincodeList);
//				}
//				// set city into CityPincode//
//				CityPincode cityPincode = new CityPincode();
//				String city = rs.getString("COL_C");
//				cityPincode.setCity(city);
//				cityPincodeList.add(cityPincode);
//
//				List<String> pincodeList = cityPincode.getPincodes();
//				if (pincodeList == null || cityPincodeList.size() == 0) {
//					pincodeList = new ArrayList<String>();
//					cityPincode.setPincodes(pincodeList);
//				}
//				String pincode = rs.getString("COL_D");
//				pincodeList.add(pincode);
//				cityPincode.setPincodes(pincodeList);
//			}
//
//		}
//		return new ArrayList<StateCity>(map.values());
//	}
//}

public class StateExtractor implements ResultSetExtractor<List<State>> {

	@Override
	public List<State> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Long, State> map = new HashMap<Long, State>();
		while (rs.next()) {
			Long id = rs.getLong("stateId"); 
			State state = map.get(id); 
			if (state == null) {
				state = new State();
				state.setStateId(id);
				state.setState(rs.getString("state"));
				map.put(id, state); 
			}

			int type = rs.getInt("IS_CITY");
			if (type == 1) {
				List<City> cityList = state.getCities(); 
				if (cityList == null) {
					cityList = new ArrayList<City>();
					state.setCities(cityList);
				}
				if(rs.getLong("COL_A") != 0) {
				City city = new City();
				city.setCity(rs.getString("COL_C"));
				city.setCityId(rs.getLong("COL_A"));
				city.setPincode(rs.getString("COL_D"));
				city.setStateId(rs.getLong("COL_B"));
				cityList.add(city);
				}
			}
		}
		return new ArrayList<State>(map.values());
	}
}

