package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.TranRemark;

import java.util.List;

public interface TranRemarkDao {

    int getRemarkCountByTids(String[] ids);

    int deleteRemarkByTids(String[] ids);

    List<TranRemark> getRemarkListByTranId(String tranId);

    int saveRemark(TranRemark tr);

    int deleteById(String id);

    int updateRemark(TranRemark tr);

    int selectCountRemarkByTranId(String id);

    int deleteCountRemarkByTranId(String id);

}
