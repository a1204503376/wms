<template>
    <el-dialog
        title="物品台账"
        :visible.sync="dialogTableVisible"
        top="3vh"
        @open="open()"
        @close="close()"
        append-to-body
    >
        <div class="dialog-box">
            <el-form :inline="true" class="demo-form-inline">
                <el-form-item label="物品">
                    <SelectSku
                        v-model="ids"
                        :search="searchData"
                        size="medium"
                        ref="selectsku"
                    ></SelectSku>
                </el-form-item>

                <el-form-item label="日期">
                    <el-date-picker
                        v-model="searchTime"
                        type="daterange"
                        value-format="yyyy-MM-dd"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                    >
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="分组">
                    <el-select placeholder="请选择分组列" clearable v-model="group">
                        <el-option label="单据编号" value="billNo"></el-option>
                        <!-- <el-option label="日期" value="optTime"></el-option> -->
                        <el-option label="操作人" value="optUser"></el-option>
                        <el-option label="业务类型" value="optType"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit">查询</el-button>
                </el-form-item>
                <el-form-item label="库存数量:">
                    <span>{{ total }}</span>
                </el-form-item>
            </el-form>
            <el-table
                :data="tableData"
                ref="table"
                style="width: 100%"
                width="100%"
                height="400"
                row-key="uuid"
                border
                :table-loading="loading"
                lazy
                :load="load"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
            >
                <el-table-column
                    prop="billNo"
                    label="单据编号"
                    width="230"
                    :show-overflow-tooltip="true"
                >
                </el-table-column>
                <el-table-column prop="optTime" label="日期" width="180">
                </el-table-column>
                <el-table-column prop="optUser" label="操作人"></el-table-column>
                <el-table-column prop="optType" label="业务类型"></el-table-column>
                <el-table-column prop="optQty" label="数量"></el-table-column>
            </el-table>
        </div>
        <span slot="footer" class="dialog-footer">
      <el-button @click="close()">关 闭</el-button>
    </span>
    </el-dialog>
</template>

<script>
import SelectSku from "@/components/nodes/selectSku";
import {accounts} from "@/api/wms/core/stock";

export default {
    name: "standingBook",
    components: {SelectSku},
    props: {
        visible: {type: Boolean, default: false},
        searchData: {
            type: Object,
            default: function () {
                return {};
            },
        },
    },
    watch: {
        visible: {
            handler: function () {
                this.dialogTableVisible = this.visible;
                this.$emit("callback", this);
            },
        },
        searchTime: {
            handler: function () {
                if (this.searchTime && this.searchTime.length > 0) {
                    this.startTime = this.searchTime[0];
                    this.endTime = this.searchTime[1];
                }
            },
        },
    },

    data() {
        return {
            dialogTableVisible: false,
            tableData: [],
            ids: "",
            searchTime: [],
            startTime: "",
            endTime: "",
            group: "",
            total: 0,
            loading: false
        };
    },
    methods: {
        open() {
            this.loading = true;
            accounts(this.ids, this.startTime, this.endTime, this.group, null, this.searchData.stockIds).then(
                (res) => {
                    this.loading = false;
                    this.tableData = res.data.data;
                }
            );
        },
        onSubmit() {
            if (this.ids == "") {
                this.$message.warning("请选择物品！");
                return;
            }
            this.tableData = [];
            this.loading = true;
            accounts(this.ids, this.startTime, this.endTime, this.group).then(
                (res) => {
                    this.loading = false;
                    this.tableData = res.data.data;
                    let temp = this.tableData.map((x) => parseInt(x.optQty));
                    if (temp.length > 0) {
                        this.total = temp.reduce((acc, val) => acc + val);
                    }
                    this.$refs.table.doLayout();
                }
            );
        },
        close() {
            this.visible = false;
            this.tableData = [];
            this.ids = "";
            this.searchTime = [];
            this.startTime = "";
            this.endTime = "";
            this.group = "";
            this.total = 0;
            this.$refs.selectsku.showText = "";
        },
        load(tree, treeNode, resolve) {
            this.loading = true;
            accounts(this.ids, this.startTime, this.endTime, this.group, tree, this.searchData.stockIds).then(
                (res) => {
                    this.loading = false;
                    resolve(res.data.data);
                }
            );
        },
    },
};
</script>

<style scoped>
.dialog-box {
    padding-top: 10px;
    padding-left: 10px;
    padding-right: 10px;
}
</style>
