package com.bjpowernode.crm.workbench.service;

import com.bjpowernode.crm.workbench.domain.Clue;
import com.bjpowernode.crm.workbench.domain.ClueRemark;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface ClueService {

    boolean save(Clue c);

    Clue detail(String id);

    boolean unbound(String id);

    boolean bound(String cid, String[] aids);

    boolean convert(String clueId, Tran t, String createBy);

    PaginationVO<Clue> pageList(Map<String, Object> map);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndClue(String id);

    boolean update(Clue c);

    List<ClueRemark> getRemarkListByCid(String clueId);

    boolean saveRemark(ClueRemark cr);

    boolean deleteRemark(String id);

    boolean updateRemark(ClueRemark cr);

    Clue updateDetail(Clue c);


}
