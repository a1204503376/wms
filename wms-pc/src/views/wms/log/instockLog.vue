<template>
    <basic-container>
        <nodes-crud
            ref="table"
            v-model="form"
            :data="data"
            :option="option"
            :table-loading="loading"
            @on-load="onLoad"
            @selection-change="selectionChange"
        ></nodes-crud>
    </basic-container>
</template>

<script>
import {getPage} from "@/api/wms/log/instockLog";

export default {
    data() {
        return {
            form: {},
            query: {},
            loading: true,
            selectionList: [],
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: true,
                editBtn: false,
                delBtn: false,
                menu: false,
                custom: false,
                column: [
                    {
                        label: "顺序",
                        prop: "executeOrder",
                        width: 100,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "执行时间",
                        prop: "createTime",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "上架策略编码",
                        prop: "ssiCode",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "上架策略名称",
                        prop: "ssiName",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "库房",
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
                        label: "处理顺序",
                        prop: "ssidProcOrder",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "上架计算代码",
                        prop: "instockFunction",
                        width: 180,
                        sortable: true,
                        type: 'select',
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.instockFunction,
                        props: {
                            label: 'dictValue',
                            value: 'dictKey'
                        }
                    },
                    {
                        label: "用户名称",
                        prop: "createUser",
                        width: 160,
                        sortable: true,
                        type: 'select-table-user',
                        dicUrl: '/api/blade-user/list',
                        props: {
                            value: 'id',
                            label: 'realName'
                        }
                    },
                    {
                        label: "是否成功",
                        prop: "isSuccess",
                        width: 120,
                        sortable: true,
                        type: 'select',
                        dictData: [
                            {
                                label: '是',
                                value: 1
                            },
                            {
                                label: '否',
                                value: 0
                            }
                        ],
                    },
                    {
                        label: "执行内容",
                        prop: "aplProcLog",
                        type: "textarea",
                        width: 200
                    }
                ]
            },
            data: []
        };
    },
    computed: {
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.aplId);
            });
            return ids.join(",");
        }
    },
    methods: {
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        }
    }
};
</script>

<style>
</style>
