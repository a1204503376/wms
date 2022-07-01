<template>
    <div id="receiveLog">
        <nodes-master-page :permission="permissionObj" v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex">
                    <el-col :span="8">
                        <el-form-item label="收货单编码" label-width="90px">
                            <el-input v-model.trim="form.params.receiveNo" :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="物品编码" label-width="90px">
                            <el-input v-model.trim="form.params.skuCode" :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item label="库位" label-width="90px">
                            <nodes-location v-model="form.params.locIdList"></nodes-location>
                        </el-form-item>
                    </el-col>
                </el-row>


            </template>
            <template v-slot:expandSearch>
                <e-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="LPN" label-width="90px">
                            <el-input v-model.trim="form.params.lpnCode" :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="箱号" label-width="90px">
                            <el-input v-model.trim="form.params.boxCode" :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="序列号" label-width="90px">
                            <el-input v-model.trim="form.params.snCode" :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="收货人" label-width="90px">
                            <el-input v-model.trim="form.params.createUser" :clearable="true"></el-input>
                        </el-form-item>
                    </el-col>
                </e-row>
                <el-row>
                    <el-col :span="6">
                        <el-form-item label="收货时间" label-width="90px">
                            <nodes-date-range v-model="form.params.createTimeDateRange" style="width: 200px">
                            </nodes-date-range>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="库位" label-width="90px">
                            <nodes-location v-model="form.params.locIdList"></nodes-location>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="库房" label-width="90px">
                            <nodes-warehouse :multiple="true" v-model="form.params.whIdList"></nodes-warehouse>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="货主" label-width="90px">
                            <nodes-owner style="width: 180px" v-model="form.params.woId"></nodes-owner>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button v-if="permissionObj.repeal" icon="el-icon-caret-left" size="mini" type="warning"
                    @click="onRepeal">撤销收货
                </el-button>
            </template>
            <template v-slot:tableTool>
                <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="服务端导出" effect="dark" placement="top">
                    <el-button circle icon="el-icon-download" size="mini" @click="exportData"></el-button>
                </el-tooltip>
                <el-tooltip :enterable="false" class="item" content="本地导出" effect="dark" placement="top">
                    <excel-export :filename="exportExcelName" :sheet="exportExcelSheet"
                        style="display: inline-block;margin-left: 10px">
                        <el-button circle icon="el-icon-bottom" size="mini" @click="onExportLocalData" />
                    </excel-export>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table ref="table" :data="table.data" border highlight-current-row size="mini" style="width: 100%"
                    @sort-change="onSortChange">
                    <el-table-column fixed type="selection" width="50">
                    </el-table-column>
                    <el-table-column fixed sortable type="index">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column, index) in table.columnList">
                        <el-table-column :key="index" show-overflow-tooltip v-bind="column" width="130">
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination :current-page="page.current" :page-size="page.size" :page-sizes="[20, 50, 100]"
                    :total="page.total" background layout="total, sizes, prev, pager, next, jumper" v-bind="page"
                    @size-change="handleSizeChange" @current-change="handleCurrentChange">
                </el-pagination>
            </template>
        </nodes-master-page>
        <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
        </dialog-column>
    </div>
</template>

<script>
import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import { listMixin } from "@/mixins/list";
import NodesLocation from "@/components/wms/select/NodesLocation";
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import { getPage, exportExcel } from "@/api/wms/instock/receiveLog"
import fileDownload from "js-file-download";
import { ExcelExport } from 'pikaz-excel-js'
import { nowDateFormat } from "@/util/date";

export default {
    name: "receiveLog",
    components: {
        NodesWarehouse,
        NodesOwner,
        NodesLocation,
        DialogColumn,
        NodesSearchInput,
        NodesMasterPage,
        NodesDateRange,
        ExcelExport,
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    receiveNo: "",
                    skuCode: "",
                    boxCode: "",
                    lpnCode: "",
                    snCode: "",
                    createUser: "",
                    createTimeDateRange: ["", ""],
                    locIdList: [],
                    whIdList: [],
                    woId: ""
                },
            },
            table: {
                columnList: [
                    {
                        prop: "receiveNo",
                        label: "收货单编码",
                        sortable: "custom",
                    },
                    {
                        prop: "lineNo",
                        label: "行号",
                        sortable: "custom"
                    },
                    {
                        prop: "skuCode",
                        label: "物品编码",
                        sortable: "custom"
                    },
                    {
                        prop: "skuName",
                        label: "物品名称",
                        sortable: "custom"
                    },
                    {
                        prop: "qty",
                        label: "收货数量",
                        sortable: "custom"
                    },
                    {
                        prop: "wsuName",
                        label: "计量单位",
                        sortable: "custom"
                    },
                    {
                        prop: "boxCode",
                        label: "箱号",
                        sortable: "custom"
                    },
                    {
                        prop: "lpnCode",
                        label: "LPN",
                        sortable: "custom"
                    },
                    {
                        prop: "snCode",
                        label: "序列号",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot1",
                        label: "生产批次",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot2",
                        label: "规格型号",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot3",
                        label: "收货日期",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot4",
                        label: "专用客户",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot5",
                        label: "钢背批次",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot6",
                        label: "摩擦块批次",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot7",
                        label: "产品标识代码",
                        sortable: "custom"
                    },
                    {
                        prop: "skuLot8",
                        label: "是否CRCC验证",
                        sortable: "custom"
                    },
                    {
                        prop: "whName",
                        label: "库房",
                        sortable: "custom"
                    },
                    {
                        prop: "locCode",
                        label: "库位",
                        sortable: "custom"
                    },
                    {
                        prop: "ownerName",
                        label: "货主",
                        sortable: "custom"
                    },
                    {
                        prop: "createUserName",
                        label: "收货人",
                        sortable: "custom"
                    },
                    {
                        prop: "createTime",
                        label: "收货时间",
                        sortable: "custom"
                    }
                ],
            },
        };
    },
    watch: {
        $route(to) {
            if (to.query && to.query.isRefresh === 'true') {
                this.refreshTable();
            }
        }
    },
    computed: {
        permissionObj() {
            return {
                search: this.vaildData(this.permission.receiveLog_search, false),
                repeal: this.vaildData(this.permission.receiveLog_repeal, false),
            }
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        getTableData() {
            getPage(this.page, this.form.params)
                .then((res) => {
                    let pageObj = res.data.data;
                    this.table.data = pageObj.records;
                    this.page.total = pageObj.total;
                })
        },
        refreshTable() {
            this.getTableData();
        },
        onReset() {
            this.form.params = {
                receiveNo: "",
                skuCode: "",
                boxCode: "",
                lpnCode: "",
                snCode: "",
                createUser: "",
                createTimeDateRange: ["", ""],
                locIdList: [],
                whIdList: [],
                woId: ""
            }
        },
        exportData() {
            this.loading = true;
            exportExcel(this.form.params)
                .then((res) => {
                    this.$message.success("操作成功，正在下载中...");
                    fileDownload(res.data, `收货记录${nowDateFormat("yyyyMMddhhmmss")}.xlsx`);
                })
                .catch(() => {
                    this.$message.error("系统模板目录配置有误或文件不存在");
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        onExportLocalData() {
            this.exportCurrentDataToExcel("收货记录", "收货记录")
        },
        onRepeal() {
            let rows = this.$refs.table;
            console.log(rows);
        }
    },
};
</script>
