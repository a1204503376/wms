import { getDetail as getEnterpriseDetail } from "@/api/wms/basedata/enterprise";
import { getAsnBillNo } from "@/api/wms/instock/asnHeader";
import { dict } from "@/constant/dict";

/**
 * 获取默认联系人，没有默认联系人的情况下取集合第一个值
 * @param contactList 联系人集合
 * @returns {联系人对象}
 */
export const getDefaultContact = function (contactList) {
    if (!contactList) {
        return null;
    }
    let contacts = contactList.filter(item => {
        return item.defaultFlagBoolean === true;
    });
    let contact = null;
    if (contacts && contacts.length > 0) {
        contact = contacts[0];
    } else if (contactList.length > 0) {
        contact = contactList[0];
    }
    return contact;
}
/**
 * 获取默认地址，没有默认地址的情况下取集合第一个值
 * @param contactList 地址集合
 * @returns {地址对象}
 */
export const getDefaultAddress = function (addressList) {
    if (!addressList) {
        return null;
    }
    let addresses = addressList.filter(item => {
        return item.defaultFlagBoolean == true;
    });
    let address = null;
    if (addresses && addresses.length > 0) {
        address = addresses[0];
    } else if (addressList.length > 0) {
        address = addressList[0];
    }
    return address;
}
/**
 * 常规分组（顺序第一个）
 * @type {{column: ({readonly: boolean, prop: string, label: string, dicUrl: string, props: {value: string}}|{readonly: boolean, prop: string, label: string}|{prop: string, rules: [{trigger: string, message: string, required: boolean}], label: string, dicUrl: string, type: string, props: {label: string, value: string}}|{cascaderItem: [string, string], defaultIndex: number, prop: string, rules: [{trigger: string, message: string, required: boolean}], disabled: (function(*=): *|[]|boolean), label: string, dicUrl: string, type: string, props: {label: string, value: string}}|{prop: string, rules: [{trigger: string, message: string, required: boolean}], label: string, dicUrl: string, type: string, props: {label: string, value: string}})[], label: string}}
 */
export const group = {
    label: '常规',
    column: [
        {
            prop: "asnBillNo",
            label: "单据编码",
            disabled: true,
            default: function (data, type) {
                if (type === 'new') {
                    getAsnBillNo().then(res => {
                        data.asnBillNo = res.data.data;
                    });
                }
            }
        },
        {
            prop: "woId",
            label: "货主",
            type: "select",
            dicUrl: "/api/wms/basedata/owner/list",
            cascaderItem: ['sname', 'contact', 'saddress', 'phone'],
            defaultIndex: 0,
            props: {
                label: "ownerName",
                value: "woId"
            },
            clearable:false,
            rules: [{ required: true, message: "所属货主不能为空", trigger: "change" }],
            disabled: function (data) {
                return data && data.asnDetailList && data.asnDetailList.length > 0;
            }
        },
        {
            prop: "billTypeCd",
            label: "单据类型",
            type: "select",
            dicUrl: "/api/wms/basedata/billtype/list?ioType=I",
            props: {
                label: 'billTypeName',
                value: 'billTypeCd'
            },
            rules: [{ required: true, message: "单据类型不能为空", trigger: "change" }],
        },
        {
            prop: "instoreType",
            label: "入库方式",
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=" + dict.instoreType,
            props: {
                label: 'dictValue',
                value: 'dictKey'
            },
            rules: [{ required: true, message: "请选择入库方式", trigger: "change" }],
        },
        {
            prop: "whId",
            label: "收货库房",
            type: "select",
            dicUrl: "/api/wms/warehouse/warehouse/list",
            props: {
                label: "whName",
                value: "whId"
            },
            rules: [{ required: true, message: "收货库房不能为空", trigger: "change" }],
        },
        {
            prop: "deptId",
            label: "机构",
            type: "select-tree",
            dicUrl: "/api/blade-system/dept/tree",
            props: {
                label: "title",
                value: "id",
                children: "children"
            },
            clearable: true,
            rules: [{ required: true, message: "机构不能为空", trigger: "change" }],
        },
        {
            prop: "scheduledArrivalDate",
            label: "预期到货",
            type: "min-date",
            format: "yyyy-MM-dd HH:mm:ss",
            rules: [{ required: true, message: "请选择预期到货时间", trigger: "change" }],
        },
        {
            prop: "sId",
            label: "供应商",
            type: "selectEnterprise",
            props: {
                label: 'sname'
            },
            search: {
                woId: 'woId',
                enterpriseType: '101'
            },
            placeholder: '请选择供应商',
            cascaderItem: ['contact', 'saddress', 'phone'],
            row: true,
            change: (val, obj, col, data) => {
                if (obj) {
                    // 设置供应商编码
                    data.scode = obj.enterpriseCode;
                    data.sname = obj.enterpriseName;
                }
                if (val) {
                    // 获取供应商详细信息
                    getEnterpriseDetail(val).then(res => {
                        data.scode = res.data.data.enterpriseCode;
                        data.sname = res.data.data.enterpriseName;
                        // 绑定联系人数据源，并设置默认联系人
                        if (col.cascader) {
                            col.cascader.forEach(item => {
                                if (item.prop === 'contact') {
                                    item.dicData = res.data.data.contactsList;
                                } else if (item.prop === 'saddress') {
                                    item.dicData = res.data.data.addressList;
                                }
                            });
                        }
                        // 将默认联系人显示在组建上
                        if (res.data.data.contactsList && res.data.data.contactsList.length > 0) {
                            let contact = getDefaultContact(res.data.data.contactsList);
                            if (contact) {
                                data.contact = contact.contactsName;
                                data.phone = contact.contactsNumber;
                            }
                        }
                        // 将默认地址显示在组建上
                        if (res.data.data.addressList && res.data.data.addressList.length > 0) {
                            let address = getDefaultAddress(res.data.data.addressList);
                            if (address) {
                                data.saddress = address.address;
                                data.cxcoor = address.longitude;
                                data.cycoor = address.latitude;
                            }
                        }
                    });
                }
            }
        },
        {
            prop: "contact",
            label: "联系人",
            type: "select",
            allowCreate: true,
            props: {
                label: 'contactsName',
                value: 'pcId'
            },
        },
        {
            prop: "phone",
            label: "联系电话",
            maxlength: 50,
        },
        {
            prop: "saddress",
            label: "供应商地址",
            span: 24,
            type: "select",
            allowCreate: true,
            props: {
                label: 'address',
                value: 'paId'
            },
        },
        {
            prop: "asnBillRemark",
            label: "备注",
            span: 16,
            maxlength: 200,
            type: "textarea"
        }
    ]
}
