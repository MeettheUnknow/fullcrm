package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkDao {

    int save(CustomerRemark customerRemark);

    int getCountByCids(String[] ids);

    int deleteByCids(String[] ids);

    List<CustomerRemark> getRemarkListByCid(String customerId);

    int saveRemark(CustomerRemark cr);

    int deleteById(String id);

    int updateRemark(CustomerRemark cr);


}
