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

public class ProductExtractor implements ResultSetExtractor<List<Product>> {

	@Override
	public List<Product> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Long, Product> map = new HashMap<Long, Product>();
		while (rs.next()) {
			Long id = rs.getLong("prodId");
			Product product = map.get(id);
			if (product == null) {
				product = new Product();
				product.setProdId(id);
				product.setProduct(rs.getString("product"));
				map.put(id, product);
			}

			int type = rs.getInt("IS_SERVICE");
			if (type == 1) {
				List<Service> serviceList = product.getServices();
				if (serviceList == null) {
					serviceList = new ArrayList<Service>();
					product.setServices(serviceList);
				}
				if (rs.getLong("COL_A") != 0) {
					Service service = new Service();
					service.setServiceId(rs.getLong("COL_A"));
					service.setService(rs.getString("COL_B"));
					service.setProdId(rs.getLong("COL_C"));
					serviceList.add(service);
				}
			} else if (type == 0) {
				List<Brand> brandList = product.getBrands();
				if (brandList == null) {
					brandList = new ArrayList<Brand>();
					product.setBrands(brandList);
				}
				if (rs.getLong("COL_A") != 0) {
					Brand brand = new Brand();
					brand.setBrandId(rs.getLong("COL_A"));
					brand.setBrand(rs.getString("COL_B"));
					brand.setProdId(rs.getLong("COL_C"));
					brandList.add(brand);
				}
			}
		}
		return new ArrayList<Product>(map.values());
	}

	// @Override
	// public List<Product> extractData(ResultSet rs) throws SQLException,
	// DataAccessException {
	// Map<Long, Product> map = new HashMap<Long, Product>();
	// while (rs.next()) {
	// Long id = rs.getLong("prodId");
	// Product product = map.get(id);
	// if (product == null) {
	// product = new Product();
	// product.setProdId(id);
	// product.setProduct(rs.getString("product"));
	// map.put(id, product);
	// }
	//
	// String type = rs.getString("VALUE");
	// if (type.equals("serv")) {
	// List<Service> serviceList = product.getServices();
	// if (serviceList == null) {
	// serviceList = new ArrayList<Service>();
	// product.setServices(serviceList);
	// }
	// if (rs.getLong("COL_SERVID") != 0) {
	// Service service = new Service();
	// service.setServiceId(rs.getLong("COL_SERVID"));
	// service.setService(rs.getString("COL_A"));
	// service.setProdId(rs.getLong("COL_PRODID"));
	// serviceList.add(service);
	// }
	// } else if (type.equals("brand")) {
	// List<Brand> brandList = product.getBrands();
	// if (brandList == null) {
	// brandList = new ArrayList<Brand>();
	// product.setBrands(brandList);
	// }
	// if (rs.getLong("COL_BRANDID") != 0) {
	// Brand brand = new Brand();
	// brand.setBrandId(rs.getLong("COL_BRANDID"));
	// brand.setBrand(rs.getString("COL_A"));
	// brand.setProdId(rs.getLong("COL_PRODID"));
	// brandList.add(brand);
	// }
	// } else if (type.equals("servplan")) {
	// List<ServicePlan> servicePlanList = product.getServicePlans();
	// if (servicePlanList == null) {
	// servicePlanList = new ArrayList<ServicePlan>();
	// product.setServicePlans(servicePlanList);
	// }
	// if (rs.getLong("COL_C") != 0) {
	// ServicePlan servicePlan = new ServicePlan();
	// servicePlan.setServicePlanId(rs.getLong("COL_C"));
	// servicePlan.setServicePlan(rs.getString("COL_A"));
	// servicePlan.setServicePlanLink(rs.getString("COL_D"));
	// servicePlan.setProdId(rs.getLong("COL_PRODID"));
	// servicePlan.setServiceId(rs.getLong("COL_SERVID"));
	// servicePlan.setBrandId(rs.getLong("COL_BRANDID"));
	// servicePlanList.add(servicePlan);
	// }
	// }
	//
	// }
	// return new ArrayList<Product>(map.values());
	// }

}
