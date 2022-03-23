import {hiprint} from "@/utils/hiprint/hiprint.bundle.js";

export const  providerLocation = (function () {
    return function () {
        let addElementTypes = function (context) {
            context.addPrintElementTypes(
                "location",
                [
                    new hiprint.PrintElementTypeGroup("常规", [
                        {
                            tid: 'location.text', title: '文本', data: '文本', type: 'text',
                            "options": {
                                "height": 42,
                                "width": 107,
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'barcode',
                            title: '二维码',
                            data: 'Nodes',
                            type: 'text',
                            "options": {
                                "height": 50,
                                "width": 50,
                                "fontSize": 19,
                                "fontWeight": "700",
                                "textAlign": "center",
                                "lineHeight": 39,
                                "hideTitle": true,
                                "textType": "qrcode"
                            }
                        },
                        {tid: 'image', title: '图片', data: '/Content/assets/hi.png', type: 'image'},
                        // {
                        //     tid: 'table',
                        //     title: '表格',
                        //     type: 'table',
                        //     columns: [
                        //         [
                        //             {title: '列1', align: 'center', field: 'position', width: 100},
                        //             {title: '列2', align: 'center', field: 'company', width: 100},
                        //         ]
                        //     ]
                        // },
                        {
                            tid: 'location.tableCustom',
                            title: '表格',
                            type: 'tableCustom'
                        },
                        {
                            tid: 'location.html', title: 'html',
                            formatter: function (data, options) {
                                return $('<div style="height:100%;width:100%;background:red;border-radius: 50%;"></div>');
                            },
                            type: 'html'
                        },
                    ]),
                    new hiprint.PrintElementTypeGroup("辅助", [
                        {
                            tid:'hline',
                            title: '横线',
                            type: 'hline'
                        },
                        {
                            tid:'vline',
                            title: '竖线',
                            type: 'vline'
                        },
                        {
                            tid:'rect',
                            title: '矩形',
                            type: 'rect'
                        },
                        {
                            tid:'oval',
                            title: '椭圆',
                            type: 'oval'
                        }
                    ]),
                    new hiprint.PrintElementTypeGroup("数据字段", [
                        {
                            tid: 'location.whName', title: '库房名称', field: 'whName', data: '[库房名称]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'whName',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'location.zoneName', title: '库区名称', field: 'zoneName', data: '[库区名称]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'zoneName',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'location.locCode', title: '库位编码', field: 'locCode', data: '[库位编码]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'locCode',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'location.locTypeDesc', title: '库位类型', field: 'locTypeDesc', data: '[库位类型]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'locTypeDesc',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'location.locCategoryDesc', title: '库位种类', field: 'locCategoryDesc', data: '[库位种类]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'locCategoryDesc',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'location.logicAllocation', title: '路线顺序', field: 'logicAllocation', data: '[路线顺序]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'logicAllocation',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'location.capacity', title: '容量', field: 'capacity', data: '[容量]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'capacity',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'location.loadWeight', title: '载重量', field: 'loadWeight', data: '[载重量]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'loadWeight',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'location.trayNum', title: '最大存放托数', field: 'trayNum', data: '[最大存放托数]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'trayNum',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                    ])
                ]
            );
        };

        return {
            addElementTypes: addElementTypes
        };

    };
})();
