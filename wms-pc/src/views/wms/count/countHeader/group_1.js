import {getCNO, getDefaltMode} from "@/api/wms/count/countheader";
import {getSelectList} from "@/api/wms/warehouse/zone.js";
import {dict} from '@/api/core/dict';

export const group = {
    label: '基本信息',
    column: [
        {
            label: '盘点单编码',
            prop: 'countBillNo',
            disabled: true,
            default: function (data, type) {
                if (type === 'new') {
                    getCNO().then(res => {
                        data.countBillNo = res.data.data;
                    });
                }
            }
        },
        {
            label: '盘点类型',
            prop: 'countTag',
            type: 'select',
            clearable: false,
            dicUrl: '/api/blade-system/dict/dictionary?code=stockcount_type',
            props: {
                label: 'dictValue',
                value: 'dictKey'
            },
            rules: [
                {required: true, message: "请选择盘点类型！", trigger: "blur"}
            ]
        },
        {
            label: '库房',
            prop: 'whId',
            type: 'select',
            clearable: false,
            dicUrl: '/api/wms/warehouse/warehouse/list',
            props: {
                label: 'whName',
                value: 'whId'
            },
            rules: [
                {required: true, message: "请选择库房！", trigger: "blur"}
            ],
            cascaderItem: ['zoneId'],
            change: function (val, obj, col, data) {
                data.countDetailList = undefined;
                if (col.cascader) {
                    col.cascader.forEach(item => {
                        if (item.prop === 'zoneId') {
                            getSelectList(val).then(res => {
                                item.dicData = res.data.data;
                            });
                        }
                    });
                }
            }
        },
        {
            label: '盘点方式',
            prop: 'countBy',
            type: 'select',
            default: 0,
            clearable: false,
            dicUrl: '/api/blade-system/dict/dictionary?code=count_by',
            props: {
                label: 'dictValue',
                value: 'dictKey'
            },
            rules: [
                {required: true, message: "请选择盘点方式！", trigger: "blur"}
            ]
        },
        // {
        //     label: '执行人',
        //     prop: 'userName',
        //     type: 'select',
        //     dicUrl: '/blade-user/list',
        //     props: {
        //         label: 'realName',
        //         value: 'id'
        //     }
        // },
        {
            label: '备注',
            prop: 'countRemark',
            type: 'textarea',
            span: 24,
            maxlength: 100
        }
    ]
}
