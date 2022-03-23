<template>
  <el-dialog
    :title="titleText"
    :visible.sync="showDialog"
    width="80%"
    v-dialogDrag
    @open="dialogOpen"
    @close="Onclose"
    top="3vh"
  >
    <el-table
      :data="gridData"
      border
      highlight-current-row
      :header-cell-style="{
        'background-color': '#fafafa',
      }"
      height="300px"
      overflow="auto"
      style="font-size: 14px"
      v-loading="dataLoading"
      element-loading-text="数据正在加载中"
      element-loading-spinner="el-icon-loading"
    >
      <el-table-column
        type="selection"
        align="center"
        width="50"
      ></el-table-column>
      <el-table-column
        property="whName"
        label="库房"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="serialNumber"
        label="序列号"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="serialStateDesc"
        label="序列号状态"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuCode"
        label="物品编码"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuName"
        label="物品名称"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="lotNumber"
        label="批次号"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="systemProcId"
        label="系统日志"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
    </el-table>
    <div slot="footer" class="dialog-footer">
      <el-pagination
        style="float: left"
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
import { mapGetters } from "vuex";
import { getList } from "@/api/wms/count/serial.js";
export default {
  name: "serialDialog",
  props: {
    serialShow: {
      type: Boolean,
      default: true,
    },
    titleText: {
      type: String,
      default: "序列号",
    },
    stockId: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      dataLoading: false, //是否显示加载中
      showDialog: this.serialShow, //弹框是否显示
      gridData: [], //列表数据
      searchQuery: {
        stockId: "",
      },
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0,
      },
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
      getList(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.query)
      ).then((res) => {
        const data = res.data;
        this.page.total = data.total;
        this.gridData = data.records;
        this.dataLoading = false;
      });
    },
    //取消
    childCancel() {
      this.$emit("serial-cancel");
    },
    Onclose() {
      this.searchQuery = {};
    },
  },
  watch: {
    serialShow(val) {
      this.showDialog = val; //②监听外部对props属性result的变更，并同步到组件内的data属性myResult中
    },
    showDialog(val) {
      this.$emit("on-result-change", val); //③组件内对myResult变更后向外部发送事件通知
    },
  },
};
</script>