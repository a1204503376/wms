package org.nodes.wms.core.common.wrapper;

import org.nodes.wms.core.common.entity.Contacts;
import org.nodes.wms.core.common.vo.ContactsVO;
import org.springblade.core.mp.support.BaseEntityWrapper;
import org.springblade.core.tool.utils.BeanUtil;
import org.springblade.core.tool.utils.Func;

import java.util.Objects;

/**
 * @author pengwei
 * @program WmsCore
 * @description 联系人封装类
 * @create 20191206
 */
public class ContactsWrapper extends BaseEntityWrapper<Contacts, ContactsVO> {

	public static ContactsWrapper build() {
		return new ContactsWrapper();
	}

	@Override
	public ContactsVO entityVO(Contacts entity) {
		ContactsVO contactsVO = Objects.requireNonNull(BeanUtil.copy(entity, ContactsVO.class));
		if(Func.isNotEmpty(contactsVO.getDefaultFlag())){
			contactsVO.setDefaultFlagBoolean(contactsVO.getDefaultFlag() == 0 ? false : true);
			contactsVO.setDefaultFlagBooleanDesc(contactsVO.getDefaultFlag() == 0 ? "否" : "是");
		}else{
			contactsVO.setDefaultFlagBoolean(false);
			contactsVO.setDefaultFlagBooleanDesc(contactsVO.getDefaultFlag() == 0 ? "否" : "是");
		}
		return contactsVO;
	}
}
