<template>
    <basic-container>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :table-loading="loading"
            @on-load="onLoad"
            @selection-change="selectionChange"
        >
            <template slot="menuLeft">
                <el-button
                    type="success"
                    icon="el-icon-download"
                    size="mini"
                    v-if="downButton.includes('导出')"
                    @click="exportData"
                >导出
                </el-button
                >
            </template>
        </nodes-crud>
    </basic-container>
</template>
<script>
import {mapState} from "vuex";
import {getList, exportFile} from "@/api/wms/count/countrecord";
import {getDetail as getParamDetail} from "@/api/core/param";
import fileDownload from "js-file-download";

export default {
    name: "countrecord",
    data() {
        return {
            operation: false,
            operationButton: ["导出"],
            loading: false,
            data: [],
            selectionList: [],
            dialogs: false, //搜索界面的显隐
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                column: [
                    {
                        label: "库房",
                        prop: "whName",
                        width: 180,
                        sortable: true,
                    },
                    {
                        label: "盘点单编码",
                        prop: "countBillNo",
                        width: 180,
                        sortable: true,
                        search: true,
                    },
                    {
                        label: "库位编码",
                        prop: "locCode",
                        width: 180,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "任务分组编码",
                        prop: "taskGroup",
                        width: 180,
                        sortable: true,
                    },
                    {
                        label: "容器编码",
                        prop: "lpnCode",
                        width: 160,
                        search: true,
                    },
                    {
                        label: "物品编码",
                        prop: "skuCode",
                        width: 160,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "物品名称",
                        prop: "skuName",
                        width: 160,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "盘点数量",
                        prop: "countQty",
                        width: 100,
                    },
                    {
                        label: "包装名称",
                        prop: "wspName",
                        width: 180,
                        sortable: true,
                    },
                    {
                        label: "计量单位名称",
                        prop: "wsuName",
                        width: 140,
                        sortable: true,
                    },
                    {
                        label: "层级",
                        prop: "skuLevelDesc",
                        width: 120,
                        sortable: true,
                    },
                    {
                        label: "换算倍率",
                        prop: "convertQty",
                        width: 100,
                    },
                    {
                        label: "盘点序列号",
                        prop: "serialNumber",
                        width: 120,
                    },
                    {
                        label: "盘点时间",
                        prop: "procTime",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "盘点时间",
                        prop: "procTimeArray",
                        hide: true,
                        search: true,
                        type: "daterange",
                    },
                    {
                        label: "用户名称",
                        prop: "userName",
                        width: 160,
                        sortable: true,
                    },
                    {
                        label: "盘点状态",
                        prop: "recordStateDesc",
                        width: 160,
                        search: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" +
                            this.$dict.countRecordStatus,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "批次号",
                        prop: "lotNumber",
                        width: 180,
                        sortable: true,
                    },
                ],
            },
            searchForm: {
                countBillId: "",
            },
        };
    },
    created() {
        getParamDetail({paramKey: "account:lotCount"}).then((res) => {
            let column = this.option.column;
            for (let i = 1; i <= res.data.paramValue; i++) {
                let skuLot = {
                    label: "物品批属性" + i,
                    prop: "skuLot" + i,
                    hide: false,
                    width: 140,
                    sortable: true,
                    search: true,
                    placeholder: "支持模糊查询",
                };
                column.push(skuLot);
            }
        });
    },
    computed: {
        ...mapState("leftMenu", ["downButton"]),
    },
    watch: {
        downButton(val) {
            let state = false;
            this.downButton.forEach((v) => {
                if (this.operationButton.includes(v)) {
                    state = true;
                }
            });
            this.operation = state;
        },
    },
    methods: {
        //默认渲染数据
        onLoad(page, params = {}) {
            this.loading = true;
            getList(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then((res) => {
                var data = res.data;
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
        //搜索
        onSubmit() {
            var params = this.searchForm;
            this.$refs.table.onLoad();
            this.handleClose();
        },
        //关闭搜索弹层
        handleClose() {
            this.dialogs = false;
        },
        //搜索 清空表单
        clearForm() {
            this.searchForm.countBillId = "";
        },
        exportData() {
            this.loading = true;
            exportFile(this.searchData)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res, "盘点日志.xlsx");
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
    },
};
</script>
<style lang="scss">
.countrecord {
    height: 100%;
}
</style>
