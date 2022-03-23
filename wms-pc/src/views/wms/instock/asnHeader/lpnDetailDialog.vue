<template>
  <el-dialog
    title="LPN查询"
    :visible.sync="showDialog"
    width="80%"
    v-dialogDrag
    top="3vh"
    @close="Onclose"
    @open="dialogOpen"
    append-to-body
  >
    <el-table
      :data="gridData"
      border
      highlight-current-row
      ref="singleTable"
      :header-cell-style="{
        'background-color': '#fafafa'}"
      height="400px"
      max-height="400px"
      overflow="auto"
      v-loading="dataLoading"
    >
      <el-table-column property="asnBillNo" label="订单编号" width="180" align="center"></el-table-column>
      <el-table-column property="lpnCode" label="LPN编码" width="180" align="center"></el-table-column>
      <el-table-column property="lpnQty" label="数量" align="center"></el-table-column>
      <el-table-column property="skuName" label="物品名称" width="160" align="center"></el-table-column>
      <el-table-column property="umName" label="计量单位名称" width="160" align="center"></el-table-column>
      <el-table-column property="umCode" label="计量单位编码" width="160" align="center"></el-table-column>
      <el-table-column property="skuSpec" label="规格" align="center"></el-table-column>
      <el-table-column property="lpnStatusDesc" label="占用状态" align="center"></el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
      <el-pagination
        style="float:left"
        background
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
import { getListByBillIds } from "@/api/wms/instock/lpndetail";
export default {
  name: "lpnDetailDialog",
  props: {
    isShowDialog: {
      type: Boolean,
      default: true
    },
    billIds: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
      showDialog: this.isShowDialog, //弹框是否显示
      dataLoading: false,
      gridData: [], //列表数据
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0
      }
    };
  },
  methods: {
    dialogOpen() {
      this.onLoad(this.page, this.billIds);
    },
    //切换 每页显示 数量
    handleSizeChange(size) {
      this.page.pageSize = size;
      this.onLoad(this.page, this.billIds);
    },
    //切换页数
    handleCurrentChange2(current) {
      this.page.currentPage = current;
      this.onLoad(this.page, this.billIds);
    },
    onLoad(page, billIds) {
      this.dataLoading = true;
      getListByBillIds(page.currentPage, page.pageSize, String(billIds)).then(
        res => {
          const data = res.data.data;
          this.page.total = data.total;
          this.gridData = data.records;
          this.dataLoading = false;
        }
      );
    },
    //取消
    childCancel() {
      this.Onclose();
    },
    Onclose() {
      this.$emit("child-cancel");
    }
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
