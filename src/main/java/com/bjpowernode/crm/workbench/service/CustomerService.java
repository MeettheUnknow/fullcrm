package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.CustomerRemark;
import com.bjpowernode.crm.workbench.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    List<String> getCustomerName(String name);

    Customer detail(String id);

    PaginationVO<Customer> pageList(Map<String, Object> map);

    boolean save(Customer c);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndCustomer(String id);

    boolean update(Customer c);

    List<CustomerRemark> getRemarkListByCid(String customerId);

    boolean saveRemark(CustomerRemark cr);

    boolean deleteRemark(String id);

    boolean updateRemark(CustomerRemark cr);

    String getCustomerNameByCid(String customerId);

    String getContactsNameByCid(String contactsId);

    Customer updateDetail(Customer c);

}
