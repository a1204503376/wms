import {selectLocation} from "@/api/wms/warehouse/location";
import {searchSku} from "@/api/wms/count/countheader.js";

// let dialogType = 'new';

// export const setDialogType = (val) => {
//     dialogType = val;
// };

// function show() {
//     return dialogType === 'new' || dialogType === undefined;
// }

export const group = {
    label: '明细',
    column: [
        {
            label: '搜索方式',
            prop: 'searchType',
            type: 'radio',
            view: false,
            dicData: [
                {value: 0, label: 'ABC盘点'},
                {value: 1, label: '动碰盘点'},
                {value: 2, label: '指定盘点'}
            ],
            span: 24,
            default: 0,
            hide: function (form, type) {
                return type !== 'view';
            }
        },
        {
            label: '库区',
            prop: 'zoneId',
            type: 'select',
            multiple: true,
            span: 16,
            view: false,
            props: {
                label: 'zoneName',
                value: 'zoneId'
            },
            cascaderItem: ['locId', 'locIdEnd'],
            change: function (val, obj, col, data) {
                if (col.cascader && val && val.length > 0) {
                    let ids = val.join(',');
                    selectLocation(ids).then(res => {
                        col.cascader.forEach(item => {
                            if (item.prop === 'locId' || item.prop === 'locIdEnd') {
                                item.dicData = res.data.data;
                            }
                        });
                    });

                }
            },
            hide: function (form, type) {
                return type !== 'view';
            }
        },
        {
            label: '起始库位',
            prop: 'locId',
            type: 'select',
            view: false,
            props: {
                label: 'locCode',
                value: 'locId'
            },
            hide: function (form, type) {
                if (type !== 'view') {
                    return form.searchType === 2;
                } else {
                    return false;
                }
            },
        },
        {
            label: '结束库位',
            prop: 'locIdEnd',
            type: 'select',
            props: {
                label: 'locCode',
                value: 'locId'
            },
            view: false,
            hide: function (form, type) {
                if (type !== 'view') {
                    return form.searchType === 2;
                } else {
                    return false;
                }
            },
        },
        {
            label: '货主',
            prop: 'woId',
            type: 'select',
            dicUrl: '/api/wms/basedata/owner/select',
            props: {
                label: 'ownerName',
                value: 'woId'
            },
            view: false,
            cascaderItem: ['skuName'],
            hide: function (form, type) {
                if (type !== 'view') {
                    return form.searchType === 2;
                } else {
                    return false;
                }
            },
        },
        {
            label: '物品',
            prop: 'skuCode',
            clearable: true,
            view: false,
            hide: function (form, type) {
                if (type !== 'view') {
                    return form.searchType === 2;
                } else {
                    return false;
                }
            },
        },
        {
            prop: 'abc',
            label: 'ABC分类',
            type: 'select',
            dicUrl: '/api/blade-system/dict/dictionary?code=abc',
            props: {
                label: 'dictValue',
                value: 'dictKey'
            },
            view: false,
            hide: function (form, type) {
                if (type !== 'view') {
                    return form.searchType === 0;
                } else {
                    return false;
                }
            }
        },
        {
            label: '类型',
            prop: 'changeType',
            type: 'checkGroup',
            dicData: [
                {value: 0, label: '入库'},
                {value: 1, label: '出库'},
                {value: 2, label: '移动'},
            ],
            default: [],
            span: 24,
            view: false,
            hide: function (form, type) {
                if (type !== 'view') {
                    return form.searchType === 1;
                } else {
                    return false;
                }
            }
        },
        {
            label: '开始时间',
            prop: 'startTime',
            type: 'datetime',
            view: false,
            hide: function (form, type) {
                if (type !== 'view') {
                    return form.searchType === 1;
                } else {
                    return false;
                }
            },
        },
        {
            label: '库位状态',
            prop: 'checked',
            type: 'check',
            text: '空库位',
            dicData: [
                {value: 0, label: '空库位'},
            ],
            view: false,
            hide: function (form, type) {
                if (type !== 'view') {
                    return form.countBy !== 0;
                } else {
                    return false;
                }
            }
        },
        {
            label: '查询',
            type: 'button',
            row: true,
            span: 4,
            offset: 16,
            view: false,
            click: function (self) {
                self.$refs.form.validate((valid) => {
                    if (valid) {
                        self.loading.content = true;
                        // 带上countDetailList数据量太大，后台存储日志的时候会抛异常
                        let data = Object.assign({}, self.form);
                        data.countDetailList = undefined;
                        searchSku(data).then(res => {
                            self.form.countDetailList = res.data.data;
                        }).finally(() => {
                            self.loading.content = false;
                        });
                        return true;
                    } else {
                        return false;
                    }
                });
            },
        },
        {
            label: '重置',
            type: 'button',
            span: 4,
            buttonType: 'warning',
            plain: true,
            view: false,
            click: function (self) {
                if (self && self.form) {
                    self.form.zoneId = null;
                    self.form.locId = null;
                    self.form.locIdEnd = null;
                    self.form.woId = null;
                    self.form.skuId = null;
                    self.form.skuName = null;
                    self.form.startTime = null;
                    self.form.abc = undefined;
                    self.form.checked = false;
                    self.form.searchType = 0;
                    self.form.changeType = undefined;
                    self.form.countDetailList =undefined;
                }
            },
        },
        {
            type: 'dynamic',
            prop: 'countDetailList',
            span: 24,
            addBtn: false,
            delBtn: false,
            editBtn: false,
            page: false,
            menu: false,
            selection: true,
            edit: false,
            selectionChange: function (val, form) {
                form.countDetailCheckedList = val;
            },
            children: [
                {
                    label: '库房',
                    prop: 'whName',
                    width: 160
                },
                {
                    label: '库位',
                    prop: 'locCode',
                    width: 120,
                },
                {
                    label: 'ABC分类',
                    prop: 'abcDesc',
                    width: 100,
                    hide: function (form) {
                        return form && form.searchType === 0;
                    }
                },
                {
                    label: '物品编码',
                    prop: 'skuCode',
                    width: 160,
                    hide: function (form) {
                        return form && form.countBy === 0;
                    }
                },
                {
                    label: '物品名称',
                    prop: 'skuName',
                    width: 160,
                    hide: function (form) {
                        return form && form.countBy === 0;
                    }
                },
                {
                    label: '盘点单号',
                    prop: 'countBillNo',
                    width: 160
                },
                {
                    label: '库位盘点状态',
                    prop: 'locStatusDesc',
                    width: 120
                },
                {
                    label: '上次盘点时间',
                    prop: 'lastLocCountDate',
                    width: 180,
                    hide: function (form) {
                        return form && form.countType === 0;
                    }
                },

            ]
        }
    ]
}
