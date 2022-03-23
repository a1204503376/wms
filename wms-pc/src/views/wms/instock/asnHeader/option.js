import {group as group_1} from "./group_1.js";
import {group as group_2} from "./group_2.js";
import {dict} from "@/constant/dict";

export const option = {
    newBtn: true,
    multiDelBtn: true,
    viewBtn: true,
    editBtn: true,
    delBtn: true,
    menu: true,
    custom: false,
    rowKey: "id",
    menuItem: [
        {
            label: '复制',
            command: 1,
            divided: true
        },
        {
            label: '到货登记',
            command: 2,
            divided: true
        }
    ],
    column: [
        {
            label: "单据编码",
            prop: "asnBillNo",
            search: true,
            width: 200,
            sortable: true,
            view: true
        },
        {
            label: "单据类型",
            prop: "billTypeCd",
            search: true,
            type: "select",
            dicUrl: "/api/wms/basedata/billtype/list?ioType=I",
            props: {
                label: "billTypeName",
                value: "billTypeCd"
            }
        },
        {
            label: "单据状态",
            prop: "asnBillState",
            search: true,
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=" + dict.asnBillState,
            props: {
                label: "dictValue",
                value: "dictKey"
            }
        },

        {
            label: "同步状态",
            prop: "syncState",
            search: true,
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=" + dict.syncState,
            props: {
                label: "dictValue",
                value: "dictKey"
            }
        },
        {
            label: "过账方式",
            prop: "postState",
            search: true,
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=" + dict.postState,
            props: {
                label: "dictValue",
                value: "dictKey"
            }
        },
        {
            label: "过账日期",
            prop: "postTime",
            width: 155,
            search: true,
            type: 'date-picker'
        },
        {
            label: "过账人",
            prop: "postUserCd",
            width: 120,
            search: true,
        },
        {
            label: '上位系统单号',
            prop: 'orderNo',
            search: true,
            width: 160
        },

        {
            label: "入库方式",
            prop: "instoreType",
            search: true,
            type: "select",
            dicUrl: "/api/blade-system/dict/dictionary?code=" + dict.instoreType,
            props: {
                label: "dictValue",
                value: "dictKey"
            }
        },
        {
            label: "所属货主",
            prop: "woId",
            search: true,
            type: "select",
            dicUrl: "/api/wms/basedata/owner/list",
            props: {
                label: "ownerName",
                value: "woId"
            }
        },
        {
            label: "所属库房",
            prop: "whId",
            search: true,
            type: "select",
            dicUrl: "/api/wms/warehouse/warehouse/list",
            props: {
                label: "whName",
                value: "whId"
            }
        },
        {
            label: "所属机构",
            prop: "deptId",
            search: true,
            type: "select-tree",
            dicUrl: "/api/blade-system/dept/tree",
            props: {
                label: "title",
                value: "id",
                children: "children"
            },
            clearable: true
        },
        {
            label: "供应商",
            prop: "sname",
            search: true,
            width: 150
        },
        {
            label: "供应商地址",
            prop: "saddress",
            width: 160
        },
        {
            label: "供应商电话",
            prop: "phone",
            width: 120
        },
        {
            label: "联系人",
            prop: "contact",
            width: 110
        },
        {
            label: "预计到货时间",
            prop: "scheduledArrivalDate",
            width: 160,
            sortable: true,
            type: 'date-picker'
        },
        {
            label: "实际到货时间",
            prop: "actualArrivalDate",
            width: 160,
            sortable: true,
            type: 'date-picker'
        },
        {
            label: "单据完成时间",
            prop: "finishDate",
            width: 160,
            sortable: true,
            type: 'date-picker'
        },
        {
            label: "单据创建时间",
            prop: "preCreateDate",
            width: 160,
            sortable: true,
            type: 'date-picker'
        },
        {
            label: "创建类型",
            prop: "createTypeName",
            width: 120,
            sortable: true,
            sortProp: 'createType'
        },
        {
            label: "单据创建人",
            prop: "createUserName",
            overHidden: true,
            width: 120,
            sortable: true,
        },
        {
            label: "备注",
            prop: "asnBillRemark"
        }
    ],
    group: [
        group_1,
        group_2
    ]
}
