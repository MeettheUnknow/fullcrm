package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.domain.TranRemark;
import com.bjpowernode.crm.workbench.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface TranService {

    boolean save(Tran t, String customerName);

    Tran detail(String id);

    List<TranHistory> getHistoryListByTranId(String tranId);

    boolean changeStage(Tran t);

    Map<String, Object> getCharts();

    boolean saveTran(Tran t);

    List<Tran> getTranListByCid(String customerId);

    boolean deleteTranByTranId(String id);

    List<Tran> getTranListByContactsId(String contactsId);

    PaginationVO<Tran> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Tran getTranListByTid(String id);

    boolean update(Tran t);

    List<TranRemark> getRemarkListByTranId(String tranId);

    boolean saveRemark(TranRemark tr);

    boolean deleteRemark(String id);

    boolean updateRemark(TranRemark tr);

}
