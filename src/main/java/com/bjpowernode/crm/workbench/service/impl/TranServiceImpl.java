package com.bjpowernode.crm.workbench.service.impl;

import com.bjpowernode.crm.utils.DateTimeUtil;
import com.bjpowernode.crm.utils.SqlSessionUtil;
import com.bjpowernode.crm.utils.UUIDUtil;
import com.bjpowernode.crm.workbench.dao.CustomerDao;
import com.bjpowernode.crm.workbench.dao.TranDao;
import com.bjpowernode.crm.workbench.dao.TranHistoryDao;
import com.bjpowernode.crm.workbench.dao.TranRemarkDao;
import com.bjpowernode.crm.workbench.domain.Customer;
import com.bjpowernode.crm.workbench.domain.Tran;
import com.bjpowernode.crm.workbench.domain.TranHistory;
import com.bjpowernode.crm.workbench.domain.TranRemark;
import com.bjpowernode.crm.workbench.service.TranService;
import com.bjpowernode.crm.workbench.vo.PaginationVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TranServiceImpl implements TranService {

    private TranDao tranDao = SqlSessionUtil.getSqlSession().getMapper(TranDao.class);
    private TranHistoryDao tranHistoryDao = SqlSessionUtil.getSqlSession().getMapper(TranHistoryDao.class);
    private TranRemarkDao tranRemarkDao = SqlSessionUtil.getSqlSession().getMapper(TranRemarkDao.class);
    private CustomerDao customerDao = SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);


    public boolean save(Tran t, String customerName) {

        /*

            交易添加业务：

                在做添加之前，参数t里面就少了一项信息，就是客户的主键：customerId

                先处理客户相关的需求

                （1）判断customerName，根据客户名字在客户表进行精确查询
                        如果有这个客户，则取出这个客户的id，封装到t对象中
                        如果没有这个客户，则在客户表新建一条客户信息，然后将新建的客户的id取出，封装到t对象中

                （2）经过以上操作后，t对象中的信息就全了，需要执行添加交易的操作

                （3）添加交易完成后，需要创建一条交易历史


         */

        boolean flag = true;

        Customer cus = customerDao.getCustomerByName(customerName);

        //如果cus为null，需要创建客户
        if(cus==null){

            cus = new Customer();
            cus.setId(UUIDUtil.getUUID());
            cus.setName(customerName);
            cus.setCreateBy(t.getCreateBy());
            cus.setCreateTime(DateTimeUtil.getSysTime());
            cus.setContactSummary(t.getContactSummary());
            cus.setNextContactTime(t.getNextContactTime());
            cus.setOwner(t.getOwner());
            //添加客户
            int count1 = customerDao.save(cus);
            if(count1!=1){
                flag = false;
            }

        }

        //通过以上对于客户的处理，不论是查询出来已有的客户，还是以前没有我们新增的客户，总之客户是已经有了，客户的id已有了
        //将客户id封装到t对象中
        t.setCustomerId(cus.getId());

        //添加交易
        int count2 = tranDao.save(t);
        if(count2!=1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count3 = tranHistoryDao.save(th);
        if(count3!=1){
            flag = false;
        }

        return flag;
    }

    public Tran detail(String id) {

        Tran t = tranDao.detail(id);

        return t;
    }

    public List<TranHistory> getHistoryListByTranId(String tranId) {

        List<TranHistory> thList= tranHistoryDao.getHistoryListByTranId(tranId);

        return thList;
    }

    public boolean changeStage(Tran t) {

        boolean flag = true;

        //改变交易阶段
        int count1 = tranDao.changeStage(t);
        if(count1!=1){

            flag = false;

        }

        //交易阶段改变后，生成一条交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setCreateBy(t.getEditBy());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setExpectedDate(t.getExpectedDate());
        th.setMoney(t.getMoney());
        th.setTranId(t.getId());
        //添加交易历史
        int count2 = tranHistoryDao.save(th);
        if(count2!=1){

            flag = false;

        }

        return flag;
    }

    public Map<String, Object> getCharts() {

        //取得total
        int total = tranDao.getTotal();

        //取得dataList
        List<Map<String,Object>> dataList = tranDao.getCharts();

        //将total和dataList保存到map中
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("total", total);
        map.put("dataList", dataList);

        //返回map
        return map;
    }

    public boolean saveTran(Tran t) {

        boolean flag = true;

        int count1 = tranDao.save(t);
        if(count1!=1){
            flag = false;
        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count2 = tranHistoryDao.save(th);
        if(count2!=1){
            flag = false;
        }

        return flag;
    }

    public List<Tran> getTranListByCid(String customerId) {

        List<Tran> tList = tranDao.getTranListByCid(customerId);

        return tList;
    }

    public boolean deleteTranByTranId(String id) {

        boolean flag = true;

        int count1 = tranRemarkDao.selectCountRemarkByTranId(id);

        int count2 = tranRemarkDao.deleteCountRemarkByTranId(id);

        if(count1!=count2){

            flag = false;

        }

        int count3 = tranHistoryDao.selectTranHistoryByTranId(id);

        int count4 = tranHistoryDao.deleteTranHistoryByTranId(id);

        if(count3!=count4){

            flag = false;

        }

        int count5 = tranDao.deleteTranByTranId(id);

        if(count5!=1){

            flag = false;

        }

        return flag;
    }

    public List<Tran> getTranListByContactsId(String contactsId) {

        List<Tran> tList = tranDao.getTranListByContactsId(contactsId);

        return tList;
    }

    public PaginationVO<Tran> pageList(Map<String, Object> map) {

        //取得total
        int total = tranDao.getTotalByCondition(map);

        //取得dataList
        List<Tran> dataList = tranDao.getTranListByCondition(map);

        //创建一个vo对象，将total和dataList封装到vo中
        PaginationVO<Tran> vo = new PaginationVO<Tran>();
        vo.setTotal(total);
        vo.setDataList(dataList);

        //将vo返回

        return vo;
    }

    public boolean delete(String[] ids) {

        boolean flag = true;

        //查询出需要删除的备注的数量
        int count1 = tranRemarkDao.getRemarkCountByTids(ids);

        //删除备注，返回受到影响的条数（实际删除的数量）
        int count2 = tranRemarkDao.deleteRemarkByTids(ids);

        if(count1!=count2){
            flag = false;
        }

        //查询出删除历史记录的数量
        int count3 = tranHistoryDao.getHistoryCountByTids(ids);

        //删除历史记录，返回受到影响的条数（实际删除的数量）
        int count4 = tranHistoryDao.deleteHistoryCountByTids(ids);

        if(count3!=count4){
            flag = false;
        }

        //删除交易
        int count5 = tranDao.delete(ids);
        if(count5!=ids.length){

            flag = false;

        }

        return flag;
    }

    public Tran getTranListByTid(String id) {

        Tran t = tranDao.getTranListByTid(id);

        return t;
    }

    public boolean update(Tran t) {

        boolean flag = true;

        //更新交易
        int count1 = tranDao.update(t);
        if(count1!=1){

            flag = false;

        }

        //添加交易历史
        TranHistory th = new TranHistory();
        th.setId(UUIDUtil.getUUID());
        th.setTranId(t.getId());
        th.setStage(t.getStage());
        th.setMoney(t.getMoney());
        th.setExpectedDate(t.getExpectedDate());
        th.setCreateTime(DateTimeUtil.getSysTime());
        th.setCreateBy(t.getCreateBy());
        int count2 = tranHistoryDao.save(th);
        if(count2!=1){
            flag = false;
        }

        return flag;
    }

    public List<TranRemark> getRemarkListByTranId(String tranId) {

        List<TranRemark> trList = tranRemarkDao.getRemarkListByTranId(tranId);

        return trList;
    }

    public boolean saveRemark(TranRemark tr) {

        boolean flag = true;

        int count = tranRemarkDao.saveRemark(tr);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public boolean deleteRemark(String id) {

        boolean flag = true;

        int count = tranRemarkDao.deleteById(id);

        if(count!=1){

            flag = false;

        }

        return flag;
    }

    public boolean updateRemark(TranRemark tr) {

        boolean flag = true;

        int count = tranRemarkDao.updateRemark(tr);

        if(count!=1){

            flag = false;

        }

        return flag;
    }


}
