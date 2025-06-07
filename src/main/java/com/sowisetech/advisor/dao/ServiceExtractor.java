package com.sowisetech.advisor.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.sowisetech.advisor.model.Brand;
import com.sowisetech.advisor.model.Product;
import com.sowisetech.advisor.model.Service;
import com.sowisetech.advisor.model.ServicePlan;

public class ServiceExtractor implements ResultSetExtractor<List<Service>> {

	@Override
	public List<Service> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Long, Service> map = new HashMap<Long, Service>();
		while (rs.next()) {
			Long id = rs.getLong("serviceId");
			Service service = map.get(id);
			if (service == null) {
				service = new Service();
				service.setServiceId(id);
				service.setService(rs.getString("service"));
				service.setProdId(rs.getLong("prodId"));
				map.put(id, service);
			}

			int type = rs.getInt("IS_SERVICEPLAN");
			if (type == 1) {
				List<ServicePlan> servicePlanList = service.getServicePlans();
				if (servicePlanList == null) {
					servicePlanList = new ArrayList<ServicePlan>();
					service.setServicePlans(servicePlanList);
				}
				if (rs.getLong("COL_A") != 0) {
					ServicePlan servicePlan = new ServicePlan();
					servicePlan.setServicePlanId(rs.getLong("COL_A"));
					servicePlan.setServicePlan(rs.getString("COL_B"));
					servicePlan.setServiceId(rs.getLong("COL_C"));
					servicePlanList.add(servicePlan);
				}
			}
		}
		return new ArrayList<Service>(map.values());
	}

}
