<template>
    <basic-container>
        <nodes-form :group="group" v-model="form"></nodes-form>
    </basic-container>
</template>


<script>

import nodesForm from "@/components/element-ui/form/index"

export default {
    components: {
        nodesForm
    },
    data(){
        return{
            form: {},
            group: [{
                column:[
                    {
                        prop: "boxNum",
                        label: "箱号",
                        type: "text",
                        disabled: true,
                        default: function () {
                            return 1;
                        },
                        span:20
                    },{
                        label: "物品",
                        disabled: false,   //是否置灰
                        prop: "skuName",
                        width: 150,
                        sortable: true,
                        type: 'select-table-sku',
                        rules: [{required: true, message: "物品不能为空", trigger: "blur"}],
                        maxlength: 50,
                        span:20
                    },{
                        prop: "enterpriseName",
                        label: "客户",
                        disabled: false,   //是否置灰
                        width: 150,
                        sortable: true,
                        // search: {
                        //     enterpriseType: '102'
                        // },
                        type: 'select-table-enterprise',
                        rules: [{required: true, message: "客户不能为空", trigger: "blur", pattern: /\S/,}],
                        maxlength: 50,
                        span:20
                    },{
                        prop: "announcements",
                        label: "注意事项",
                        type: "checkGroup",
                        dicData: [
                            {
                                value: 0,
                                label: "怕湿"
                            },
                            {
                                value: 1,
                                label: "小心轻放"
                            }
                            ,
                            {
                                value: 2,
                                label: "向上"
                            },
                            {
                                value: 3,
                                label: "禁止翻滚"
                            },
                            {
                                value: 4,
                                label: "防晒"
                            }
                        ],
                        span:20
                    },{
                        prop: "serialNum",
                        label: "序列号",
                        disabled: false,   //是否置灰
                        type: "textarea",
                        rules: [{required: true, message: "序列号不能为空", trigger: "blur", pattern: /\S/,}],
                        maxlength: 50,
                        span:20
                    },
                ]
            }]
        }
    }
}
</script>
