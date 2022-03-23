<template>
    <el-dialog
        title="装车明细"
        :visible.sync="showDialogs"
        :close-on-click-modal="false"
        width="80%"
        v-dialogDrag="true"
        @open="dialogOpen"
        @close="childCancels"
        top="3vh"
        append-to-body
        destroy-on-close
    >
        <el-button
            type="primary"
            size="mini"
            style="margin-top:10px;margin-left: 15px"
            @click="cancelLoad"
        >撤销装车
        </el-button>

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
            @selection-change="handleSelectionChange">
        >
            <el-table-column type="selection" align="center" width="50"></el-table-column>
            //物品编码，物品名称，包装名称，容器编码，箱码，数量，计量单位上次入库时间，上次出库时间

            <el-table-column property="lpnCode" label="容器编码" width="260" show-overflow-tooltip></el-table-column>
            <el-table-column property="createTime" label="装车时间" show-overflow-tooltip></el-table-column>

        </el-table>
        <div slot="footer" class="dialog-footer">
            <el-button @click="childCancels">关 闭</el-button>
        </div>
    </el-dialog>
</template>
<script>
import {soTruckDetail,detailRemove} from "@/api/wms/outstock/soTruckHeader.js";

export default {
    name: "cancelLoading",
    props: {
        isShowDialogs: {
            type: Boolean,
            default: true
        },
        truckId: {
            type: String,
            default: ""
        }
    },
    data() {
        return {
            dataLoading: false, //是否显示加载中
            showDialogs: this.isShowDialogs, //弹框是否显示
            gridData: [], //列表数据
            searchQuery: {truckId: ""},
            selectionList: [], //选中的数据
        };
    },
    methods: {
        dialogOpen() {
            this.onLoad(this.searchQuery);
        },
        onLoad() {
            this.dataLoading = true;
            soTruckDetail(this.truckId).then(res => {
                const data = res.data.data;
                this.gridData = data;
                this.dataLoading = false;
            });
        },
        //取消
        childCancels() {
            this.$emit("randomCancel");
        },
        cancelLoad(){
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一条数据！");
                return;
            }
            this.$confirm("是否确认 撤销 当前勾选的装车信息？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                detailRemove(this.ids).then(res => {
                    this.onLoad();
                }).finally(() => {
                    this.loading = false;
                });
            }).catch(() => {
                this.loading = false;
            });
        },
        handleSelectionChange(val) {
            this.selectionList = val;
        },
    },
    watch: {
        isShowDialogs(val) {
            this.showDialogs = val; //②监听外部对props属性result的变更，并同步到组件内的data属性myResult中
        },
        showDialogs(val) {
            this.$emit("on-result-change", val); //③组件内对myResult变更后向外部发送事件通知
        }
    },
    computed:{
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.truckDetailId);
            });
            return ids.join(",");
        },
    }
};
</script>

