import request from '@/router/axios';

export const group = {
    label: '基础信息',
    column: [
        {
            label: "策略编码",
            prop: "ssiCode",
            maxlength: 50,
            rules: [
                {required: true, message: " ", trigger: "blur", pattern: /\S/,}
            ],
        },
        {
            label: "策略名称",
            prop: "ssiName",
            maxlength: 200,
            rules: [
                {required: true, message: " ", trigger: "blur", pattern: /\S/,}
            ],
        },
        {
            label: "库房名称",
            prop: "whId",
            type: "select",
            props: {
                label: 'whName',
                value: 'whId',
            },
            dicUrl: "/api/wms/warehouse/warehouse/list",
            defaultIndex: 0,
            rules: [{required: true, message: "库房不能为空", trigger: "change, blur"}],
            disabled: function (data, self) {
                return self.$refs.dynamic_relenishmentDetailList
                    && self.$refs.dynamic_relenishmentDetailList[0].tableData.length > 0;
            },
            change: function (val, obj, col, form, self) {
                let column = self.form.group[1].column[0].$option.column;
                column.forEach(col => {
                    if (col.dicUrl && col.dicUrl.length > 0) {
                        request({
                            url: col.dicUrl,
                            params: {
                                whId: val
                            }
                        }).then(res => {
                            col.dicData = res.data.data;
                        })
                    }
                });
            }
        }
    ]
}
