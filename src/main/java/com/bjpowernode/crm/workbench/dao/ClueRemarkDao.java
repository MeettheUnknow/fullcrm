package com.bjpowernode.crm.workbench.dao;

import com.bjpowernode.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getListByClueId(String clueId);

    int delete(ClueRemark clueRemark);

    int getCountByCids(String[] ids);

    int deleteByCids(String[] ids);

    List<ClueRemark> getRemarkListByCid(String clueId);

    int saveRemark(ClueRemark cr);

    int deleteById(String id);

    int updateRemark(ClueRemark cr);

    int getCountRemarkByCid(String id);

    int deleteCountRemarkByCid(String id);

}
