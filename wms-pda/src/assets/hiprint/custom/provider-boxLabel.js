import {hiprint} from "@/utils/hiprint/hiprint.bundle.js";

export const  providerBoxLabel = (function () {
    return function () {
        let addElementTypes = function (context) {
            context.allElementTypes = [];
            context.boxLabelInfo = [];
            context.addPrintElementTypes(
                "boxLabelInfo",
                [
                    new hiprint.PrintElementTypeGroup("常规", [
                        {
                            tid: 'text', title: '文本', data: '文本', type: 'text',
                            "options": {
                                "height": 14,
                                "width": 107,
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 14,
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
                            tid: 'hline',
                            title: '横线',
                            type: 'hline'
                        },
                        {
                            tid: 'vline',
                            title: '竖线',
                            type: 'vline'
                        },
                        {
                            tid: 'rect',
                            title: '矩形',
                            type: 'rect'
                        },
                        {
                            tid: 'oval',
                            title: '椭圆',
                            type: 'oval'
                        }
                    ]),
                    new hiprint.PrintElementTypeGroup("数据字段", [
                        {
                            tid: 'boxLabelInfo.enterpriseCode',
                            title: '企业编码',
                            field: 'enterpriseCode',
                            data: '[企业编码]',
                            type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'enterpriseCode',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'boxLabelInfo.enterpriseName',
                            title: '企业名称',
                            field: 'enterpriseName',
                            data: '[企业名称]',
                            type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'enterpriseName',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'boxLabelInfo.qty', title: '数量', field: 'qty', data: '[数量]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'qty',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'boxLabelInfo.billNo', title: '订单编码', field: 'billNo', data: '[订单编码]', type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'billNo',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'boxLabelInfo.lotNumber',
                            title: '批次号',
                            field: 'lotNumber',
                            data: '[批次号]',
                            type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'lotNumber',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'boxLabelInfo.grossWeight',
                            title: '毛重',
                            field: 'grossWeight',
                            data: '[毛重]',
                            type: 'text',
                            "options": {
                                "height": 12,
                                "width": 107,
                                "field": 'grossWeight',
                                "fontSize": 12,
                                "fontWeight": "700",
                                "textAlign": "left",
                                "lineHeight": 12,
                                "hideTitle": true,
                            }
                        },
                        {
                            tid: 'boxLabelInfo.barcode1',
                            title: '二维码',
                            data: '[二维码]',
                            field: 'barcode1',
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
                    ])
                ]
            );
        };

        return {
            addElementTypes: addElementTypes
        };

    };
})();
