<template>
    <basic-container>
        <edit :visible="dialogEdit.visible"
              :isView="dialogEdit.isView"
              :dataSource="dialogEdit.dataSource"
              @dialogResult="editCallback"
        ></edit>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :table-loading="loading"
            @on-load="onLoad"
            @on-view="onView"
            @selection-change="selectionChange"
        ></nodes-crud>
    </basic-container>
</template>
<script>
import edit from "./systemProcLog/edit";
import {getPage} from "@/api/wms/log/systemProcLog";

export default {
    name: "systemproc",
    components: {edit},
    data() {
        return {
            loading: false,
            isView: true,
            isShow: false,
            selectionList: [], //选中的数据
            data: [], //列表数据
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: true,
                editBtn: false,
                delBtn: false,
                menu: false,
                column: [
                    {
                        label: "业务发生时间",
                        prop: "procTime",
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "日志内容",
                        prop: "systemProcLog",
                        width: 350,
                        sortable: true,
                        search:true
                    },
                    {
                        label: "用户名称",
                        prop: "userName",
                        sortable: true,
                        width: 120,
                    },
                    {
                        label: "数据键",
                        prop: "dataId",
                        width: 120,
                        sortable: true,
                    },
                    {
                        label: "数据项目",
                        prop: "dataItemDesc",
                        width: 160,
                        sortable: true,
                        sortProp: 'dataItem'
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
                        label: "触发时间",
                        prop: "triggerRange",
                        hide: true,
                        search: true,
                        type: 'date-picker'
                    },
                ]
            },
            dialogEdit: {
                visible: false,
                isView: false,
                dataSource: null
            }
        };
    },
    methods: {
        //默认渲染数据
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                let data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        onView(row, index) {
            this.dialogEdit.dataSource = row.systemProcId;
            this.dialogEdit.isView = true;
            this.dialogEdit.visible = true;
        },
        editCallback(dialogResult) {
            this.dialogEdit.visible = dialogResult.visible;
        }
    }
};
</script>
<style lang="scss">
</style>
