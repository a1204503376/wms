<template>
    <el-dialog
        title="下架数量明细"
        :visible.sync="showDialog"
        :close-on-click-modal="false"
        width="80%"
        v-dialogDrag="true"
        @open="dialogOpen"
        @close="childCancel"
        top="3vh"
        append-to-body
        destroy-on-close
    >
        <el-table
            :data="gridData"
            border
            highlight-current-row
            :header-cell-style="{
        'background-color': '#fafafa'}"
            height="300px"
            overflow="auto"
            style="font-size: 14px"
            v-loading="dataLoading"
            element-loading-text="数据正在加载中"
            element-loading-spinner="el-icon-loading"
        >
            <el-table-column type="selection" align="center" width="50"></el-table-column>
            //物品编码，物品名称，包装名称，容器编码，箱码，数量，计量单位上次入库时间，上次出库时间

            <el-table-column property="soBillNo" label="单据编码" width="160" show-overflow-tooltip></el-table-column>
            <el-table-column property="skuCode" label="物品编码" width="160" show-overflow-tooltip></el-table-column>
            <el-table-column property="skuName" label="物品名称" width="160" show-overflow-tooltip></el-table-column>

            <el-table-column property="wspName" label="包装名称" width="160" show-overflow-tooltip></el-table-column>
            <el-table-column property="lpnCode" label="容器编码" width="160" show-overflow-tooltip></el-table-column>
            <el-table-column property="" label="箱码" width="160" show-overflow-tooltip></el-table-column>
            <el-table-column property="stockQty" label="数量" width="160" show-overflow-tooltip></el-table-column>
            <el-table-column property="skuLevel" label="计量单位" width="160" show-overflow-tooltip></el-table-column>
            <el-table-column property="lastInTime" label="上次入库时间" width="160" show-overflow-tooltip></el-table-column>
            <el-table-column property="lastOutTime" label="上次出库时间" width="160" show-overflow-tooltip></el-table-column>

        </el-table>
        <div slot="footer" class="dialog-footer">
            <el-pagination
                background
                style="float:left"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange2"
                :current-page="page.currentPage"
                :page-size="page.pageSize"
                :total="page.total"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
            ></el-pagination>
            <el-button @click="childCancel">关 闭</el-button>
        </div>
    </el-dialog>
</template>
<script>
import {stockDetails} from "@/api/wms/core/stock.js";

export default {
    name: "shelvesDialog",
    props: {
        isShowDialog: {
            type: Boolean,
            default: true
        },
        stockId: {
            type: String,
            default: ""
        }
    },
    data() {
        return {
            dataLoading: false, //是否显示加载中
            showDialog: this.isShowDialog, //弹框是否显示
            gridData: [], //列表数据
            searchQuery: {stockId: "", occupyType: ""},
            page: {
                pageSize: 10,
                currentPage: 1,
                total: 0
            }
        };
    },
    methods: {
        dialogOpen() {
            this.searchQuery.stockId = this.stockId;
            this.onLoad(this.page, this.searchQuery);
        },
        //切换 每页显示 数量
        handleSizeChange(size) {
            this.page.pageSize = size;
            this.onLoad(this.page, this.searchQuery);
        },
        //切换页数
        handleCurrentChange2(current) {
            this.page.currentPage = current;
            this.onLoad(this.page, this.searchQuery);
        },
        onLoad(page, params = {}) {
            this.dataLoading = true;
            stockDetails(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                console.log(res);
                const data = res.data.data;
                this.page.total = data.total;
                this.gridData = data.records;
                this.dataLoading = false;
            });
        },
        //取消
        childCancel() {
            this.searchQuery = {};
            this.$emit("child-cancel");
        },
    },
    watch: {
        isShowDialog(val) {
            this.showDialog = val; //②监听外部对props属性result的变更，并同步到组件内的data属性myResult中
        },
        showDialog(val) {
            this.$emit("on-result-change", val); //③组件内对myResult变更后向外部发送事件通知
        }
    }
};
</script>
