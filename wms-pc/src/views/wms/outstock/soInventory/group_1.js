import { getDetail as getEnterpriseDetail } from "@/api/wms/basedata/enterprise";
import { getSoBillNo } from "@/api/wms/outstock/soHeader";

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
            prop: "soBillNo",
            label: "单据编码",
            disabled: true,
            default: function (data, type) {
                if (type === 'new') {
                    getSoBillNo().then(res => {
                        data.soBillNo = res.data.data;
                    });
                }
            }
        },
        {
            prop: "orderNo",
            label: "外部单号",
            disabled: true
        },
        {
            prop: "billTypeCd",
            label: "单据类型",
            type: "select",
            dicUrl: "/api/wms/basedata/billtype/list?ioType=O",
            props: {
                label: 'billTypeName',
                value: 'billTypeCd'
            },
            rules: [{ required: true, message: "单据类型不能为空", trigger: "change" }],
        },
        {
            prop: "woId",
            label: "所属货主",
            type: "select",
            dicUrl: "/api/wms/basedata/owner/list",
            cascaderItem: ['cName', 'expressName'],
            defaultIndex: 0,
            props: {
                label: "ownerName",
                value: "woId"
            },
            rules: [{ required: true, message: "所属货主不能为空", trigger: "change" }],
            disabled: function (data) {
                return data && data.soDetailList && data.soDetailList.length > 0;
            }
        },
        {
            prop: "whId",
            label: "发货库房",
            type: "select",
            dicUrl: "/api/wms/warehouse/warehouse/list",
            props: {
                label: "whName",
                value: "whId"
            },
            rules: [{ required: true, message: "发货库房不能为空", trigger: "change" }],
        },
        // {
        //     prop: "zoneType",
        //     label: "拣货库区",
        //     type: "select",
        //     dicUrl: "/api/blade-system/dict/dictionary?code=zone_type",
        //     props: {
        //         label: "dictValue",
        //         value: "dictKey"
        //     },
        //     rules: [{required: true, message: "拣货库区不能为空", trigger: "change"}],
        // },
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
            prop: "outStockDate",
            label: "发货日期",
            type: "min-date",
            format: "yyyy-MM-dd HH:mm:ss",
        },
        {
            prop: "cId",
            label: "客户",
            type: "selectEnterprise",
            row: true,
            props: {
                label: 'cName'
            },
            search: {
                woId: 'woId',
                enterpriseType: '102'
            },
            placeholder: '请选择客户',
            cascaderItem: ['contact', 'address', 'phone'],
            rules: [{ required: true, message: "客户不能为空", trigger: "change, blur" }],
            change: (val, obj, col, data) => {
                if (obj) {
                    // 设置客户编码
                    data.cCode = obj.enterpriseCode;
                    data.cName = obj.enterpriseName;
                }
                if (val) {
                    // 获取客户详细信息
                    getEnterpriseDetail(val).then(res => {
                        data.cCode = res.data.data.enterpriseCode;
                        data.cName = res.data.data.enterpriseName;
                        // 绑定联系人数据源，并设置默认联系人
                        if (col.cascader) {
                            col.cascader.forEach(item => {
                                if (item.prop === 'contact') {
                                    item.dicData = res.data.data.contactsList;
                                } else if (item.prop === 'address') {
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
                                data.address = address.address;
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
            rules: [{ required: true, message: "联系人不能为空", trigger: "change" }],
        },
        {
            prop: "phone",
            label: "联系电话",
            maxlength: 100,
            rules: [{ required: true, message: "联系电话不能为空", trigger: "change" }],
        },
        {
            prop: "address",
            label: "客户地址",
            span: 24,
            type: "select",
            allowCreate: true,
            props: {
                label: 'address',
                value: 'paId'
            },
            rules: [{ required: true, message: "客户地址不能为空", trigger: "change" }],
        },
        {
            prop: "expressId",
            label: "承运商",
            type: "selectEnterprise",
            props: {
                label: 'expressName'
            },
            search: {
                woId: 'woId',
                enterpriseType: '103'
            },
            placeholder: '请选择承运商',
            cascaderItem: ['expressAddress', 'expressPhone'],
            change: (val, obj, col, data) => {
                if (obj) {
                    // 设置承运商编码
                    data.expressCode = obj.enterpriseCode;
                    data.expressName = obj.enterpriseName;
                }
                // 获取承运商详细信息
                if (val) {
                    getEnterpriseDetail(val).then(res => {
                        // 绑定联系人数据源，并设置默认联系人
                        if (col.cascader) {
                            col.cascader.forEach(item => {
                                if (item.prop === 'expressAddress') {
                                    item.dicData = res.data.data.addressList;
                                }
                            });
                        }
                        // 将默认联系人显示在组建上
                        if (res.data.data.contactsList && res.data.data.contactsList.length > 0) {
                            let contact = getDefaultContact(res.data.data.contactsList);
                            if (contact) {
                                data.expressPhone = contact.contactsNumber;
                            }
                        }
                        // 将默认地址显示在组建上
                        if (res.data.data.addressList && res.data.data.addressList.length > 0) {
                            let address = getDefaultAddress(res.data.addressList);
                            if (address) {
                                data.expressAddress = address.address;
                                data.expressX = address.longitude;
                                data.expressY = address.latitude;
                            }
                        }
                    });
                }
            }
        },
        {
            prop: "expressPhone",
            label: "承运商电话",
            maxlength: 50,
        },
        {
            prop: "expressAddress",
            label: "承运商地址",
            span: 24,
            type: "select",
            allowCreate: true,
            props: {
                label: 'address',
                value: 'paId'
            }
        },
        {
            prop: "soBillRemark",
            label: "备注",
            type: 'textarea',
            span: 24,
            maxlength: 200
        }
    ]
}
