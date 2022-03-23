<template>
    <basic-container>
        <nodes-crud
            ref="curd"
            v-model="form"
            :option="option"
            :data="data"
            :table-loading="loading"
            @on-load="onLoad"
            @selection-change="selectionChange"
        >
            <template slot="menuLeft"></template>
            <template slot="menuRight"></template>
            <template slot="menu"></template>
        </nodes-crud>
    </basic-container>
</template>

<script>
import {getPage} from "@/api/wms/log/taskHistory";

export default {
    data() {
        return {
            form: {},
            query: {},
            loading: true,
            data: [],
            selectionList: [],
            option: {
                editBtn: true,
                menu: false,
                custom: false,
                column: [

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
                        label: "任务类型",
                        prop: "taskTypeCd",
                        width: 120,
                        sortable: true,
                        sortProp: 'taskTypeCd',
                        search: true,
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.taskType,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    {
                        label: "任务执行方式",
                        prop: "taskProcType",
                        width: 140,
                        sortable: true,
                        sortProp: 'taskProcType',
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.taskProcType,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    {
                        label: "单据种类",
                        prop: "billTypeCd",
                        width: 120,
                        sortable: true,
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/billtype/list",
                        props: {
                            label: 'billTypeName',
                            value: 'billTypeCd'
                        },
                    },
                    {
                        label: "单据编码",
                        prop: "billNo",
                        width: 180,
                        sortable: true,
                        search: true
                    },
                    {
                        label: "用户编码",
                        prop: "userCode",
                        sortable: true,
                        width: 120,
                    },
                    {
                        label: "用户名称",
                        prop: "userName",
                        sortable: true,
                        width: 120,
                    },
                    {
                        label: "数量",
                        prop: "taskQty",
                        width: 100,
                        sortable: true,
                        rules: [
                            {
                                pattern: /^[0-9]*$/,
                            }
                        ]
                    },
                    {
                        label: "任务描述",
                        prop: "taskRemark",
                        width: 100
                    },
                    {
                        label: "任务分派时间",
                        prop: "allotTime",
                        width: 140,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "任务开始执行时间",
                        prop: "beginTime",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "任务关闭时间",
                        prop: "closeTime",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    }
                ],
                group: []
            }
        };
    },
    computed: {
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.taskHistoryId);
            });
            return ids.join(",");
        }
    },
    mounted() {
    },
    methods: {
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.curd.toggleSelection();
        },
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(page.currentPage, page.pageSize, params).then(res => {
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
.taskhistory {
    height: 100%;
}
</style>
