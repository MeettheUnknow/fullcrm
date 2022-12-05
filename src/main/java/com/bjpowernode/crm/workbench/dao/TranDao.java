package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

public interface TranDao {

    int save(Tran t);

    Tran detail(String id);

    int changeStage(Tran t);

    int getTotal();

    List<Map<String, Object>> getCharts();

    List<Tran> getTranListByCid(String customerId);

    int deleteTranByTranId(String id);

    List<Tran> getTranListByContactsId(String contactsId);

    int getTotalByCondition(Map<String, Object> map);

    List<Tran> getTranListByCondition(Map<String, Object> map);

    int delete(String[] ids);

    Tran getTranListByTid(String id);

    int update(Tran t);

    String[] selectTranIds(String id);

}
