import {getListByZoneId} from "@/api/wms/warehouse/location";
import {getSelectList } from "@/api/wms/warehouse/zone.js";
export const group = {
    label: "明细",
    column: [{
        prop: "relDetailVos",
        type: "dynamic",
        span: 24,
        addBtn:false,
        children: [
            {
                label: '库房',
                prop: 'fromWhId',
                width:'180',
                show: ['fromWhName'],
                type: 'select',
                disabled:true,
                dicUrl: '/api/wms/warehouse/warehouse/list',
                cascaderItem: ['fromZoneId'],
                isOther:false,
                props: {
                    label: 'whName',
                    value: 'whId'
                },
                clearable: true,
                change: function (val, obj, scope, self) {
                    if (obj) {
                        scope.row.fromWhName = obj.whName;
                        scope.row.fromWhId = obj.whId;
                    } else {
                        scope.row.fromZoneName = '';
                    }
                    if (!val) {
                        return;
                    }
                    if (this.cascader) {
                        this.cascader.forEach(item => {
                            if (item.prop === 'fromZoneId') {
                                // 设置数据源
                                getSelectList(val).then(res => {
                                    item.dicData = res.data.data;
                                });
                            }
                        });
                    }
                }
            },
            {
                label: '从库区',
                prop: 'fromZoneId',
                type: 'select',
                width:'180',
                show: ['fromZoneName'],
                cascaderItem: ['fromLocId'],
                props: {
                    label: 'zoneName',
                    value: 'zoneId'
                },
                clearable: true,
                change: function (val, obj, scope, self) {
                    if (obj) {
                        scope.row.fromZoneName = obj.zoneName;
                        scope.row.fromZoneId = obj.zoneId;
                    } else{
                        scope.row.fromLocName = "";
                    }
                    if (!val) {
                        return;
                    }
                    if (this.cascader) {
                        this.cascader.forEach(item => {
                            if (item.prop === 'fromLocId') {
                                // 设置数据源
                                getListByZoneId(val).then(res => {
                                    item.dicData = res.data.data;
                                });
                            }
                        });
                    }
                }
            },
            {
                label: '从库位',
                prop: 'fromLocId',
                type: 'select',
                width:'180',
                show: ['fromLocName'],
                dicUrl: '/api/wms/warehouse/location/list',
                props: {
                    label: 'locCode',
                    value: 'locId'
                },
                clearable: true,
                change: function (val, obj, scope, self) {
                    if (obj) {
                        scope.row.fromLocName = obj.locCode;
                        scope.row.fromLocId = obj.locId;
                    }  else{
                        scope.row.fromLocName = "";
                    }
                }
              },
            {
                label: '自容器',
                width: 150,
                prop: 'fromLpnCode',
                type: 'input',
                hide:function(){
                    let params = JSON.parse(window.localStorage.getItem('saber-param'));
                    let lpnParam = params.content.filter(item => item.paramKey=='system::lpnEnable');
                    let flag = lpnParam[0].paramValue;
                    if(flag=='1'){
                        return false;
                    }
                    return true;
                }
            },
            {
                label: '至库房',
                prop: 'toWhId',
                width:'180',
                show: ['toWhName'],
                type: 'select',
                hide:true,
                isOther:false,
                disabled:true,
                dicUrl: '/api/wms/warehouse/warehouse/list',
                cascaderItem: ['toZoneId'],
                props: {
                    label: 'whName',
                    value: 'whId'
                },
                clearable: true,
                change: function (val, obj, scope, self) {
                    if (obj) {
                        scope.row.toWhName = obj.whName;
                        scope.row.toWhId = obj.whId;
                    } else {
                        scope.row.toZoneName = '';
                    }
                    if (!val) {
                        return;
                    }
                    if (this.cascader) {
                        this.cascader.forEach(item => {
                            if (item.prop === 'toZoneId') {
                                // 设置数据源
                                getSelectList(val).then(res => {
                                    item.dicData = res.data.data;
                                });
                            }
                        });
                    }
                }
            },
            {
                label: '至库区',
                prop: 'toZoneId',
                type: 'select',
                width:'180',
                show: ['toZoneName'],
                disabled:true,
                dicUrl: '/api/wms/warehouse/zone/list',
                cascaderItem: ['toLocId'],
                props: {
                    label: 'zoneName',
                    value: 'zoneId'
                },
                clearable: true,
                change: function (val, obj, scope, self) {
                    if (obj) {
                        scope.row.toZoneName = obj.zoneName;
                        scope.row.toZoneId = obj.zoneId;
                    } else{
                        scope.row.toLocName = "";
                    }
                    if (!val) {
                        return;
                    }
                    if (this.cascader) {
                        this.cascader.forEach(item => {
                            if (item.prop === 'toLocId') {
                                // 设置数据源
                                getListByZoneId(val).then(res => {
                                    item.dicData = res.data.data;
                                });
                            }
                        });
                    }
                }
            },
            {
                label: '至库位',
                prop: 'toLocId',
                type: 'select',
                width:'180',
                disabled:true,
                show: ['toLocName'],
                dicUrl: '/api/wms/warehouse/location/list',
                props: {
                    label: 'locCode',
                    value: 'locId'
                },
                clearable: true,
                change: function (val, obj, scope, self) {
                    if (obj) {
                        scope.row.toLocName = obj.locCode;
                        scope.row.toLocId = obj.locId;
                    }  else{
                        scope.row.toLocName = "";
                    }
                }
              },
            {
                label: '至容器',
                width: 150,
                prop: 'toLpnCode',
                type: 'text',
                hide:function(){
                    let params = JSON.parse(window.localStorage.getItem('saber-param'));
                    let lpnParam = params.content.filter(item => item.paramKey=='system::lpnEnable');
                    let flag = lpnParam[0].paramValue;
                    if(flag=='1'){
                        return false;
                    }
                    return true;
                }
            },
            {
                label: '所属货主',
                width: 150,
                prop: 'woName',
                type: 'text'
            },
            {
                label: '物品编码',
                width: 150,
                prop: 'skuCode',
                type: 'text'
            },
            {
                label: '物品名称',
                width: 150,
                prop: 'skuName',
                type: 'text'
            },
            {
                label: '补货数量',
                width: 150,
                prop: 'relQty',
                type: 'inputNumber'
            },
            {
                label: '计量单位',
                width: 150,
                prop: 'umName',
                type: 'text'
            },
            {
                label: '实际量',
                width: 150,
                prop: 'realQty',
                type: 'text'
            },
            {
                label: '执行状态',
                width: 150,
                prop: 'relBillStateDesc',
                type: 'text'
            },
        ]
    }]
}
