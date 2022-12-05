package com.bjpowernode.crm.workbench.service;


import com.bjpowernode.crm.workbench.domain.Contacts;
import com.bjpowernode.crm.workbench.domain.ContactsRemark;
import com.bjpowernode.crm.workbench.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface ContactsService {

    List<Contacts> getContactsListByName(String cname);

    PaginationVO<Contacts> pageList(Map<String, Object> map);

    boolean save(Contacts c, String customerName);

    boolean delete(String[] ids);

    Map<String, Object> getUserListAndContacts(String id);

    boolean update(Contacts c);

    Contacts detail(String id);

    List<Contacts> getContactsListByCid(String customerId);

    boolean saveContact(Contacts c);

    boolean deleteDetailContactByCid(String id);

    List<ContactsRemark> getRemarkListByCid(String contactsId);

    boolean saveRemark(ContactsRemark cr);

    boolean deleteRemark(String id);

    boolean updateRemark(ContactsRemark cr);

    boolean bound(String contactsId, String[] aids);

    boolean unbound(String id);

    Contacts updateDetail(Contacts c);

}
