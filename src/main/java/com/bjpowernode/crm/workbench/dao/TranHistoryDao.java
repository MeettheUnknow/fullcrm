package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int save(TranHistory th);

    List<TranHistory> getHistoryListByTranId(String tranId);

    int selectTranHistoryByTranId(String id);

    int deleteTranHistoryByTranId(String id);

    int getHistoryCountByTids(String[] ids);

    int deleteHistoryCountByTids(String[] ids);

}
