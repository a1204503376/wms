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
    <el-form>
      <el-row :gutter="24">
        <el-col :span="8">
          <el-select
            ref="el1"
            v-model="searchQuery.whId"
            placeholder="请选择库房"
            style="width: 100%"
          >
            <el-option
              v-for="item in warehouseSource"
              :key="item.whId"
              :value="item.whId"
              :label="item.whName"
              @change="getwhId"
            ></el-option>
          </el-select>
        </el-col>
        <el-col :span="8">
          <el-input
            placeholder="请选择物品"
            v-model="searchQuery.skuName"
            :readonly="true"
          >
            <i
              slot="append"
              style="font-size: 18px; color: skyblue"
              class="el-icon-shopping-bag-1"
              @click="dialogCommGoods = true"
            ></i>
          </el-input>
        </el-col>
        <el-col :span="8">
          <el-input
            placeholder="请输入批次号"
            v-model="searchQuery.lotNumber"
          ></el-input>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="21" align="right">
          <el-button type="primary" @click="refreshChange">查 询</el-button>
        </el-col>
        <el-col :span="3" align="right">
          <el-button type="danger" @click="searchReset">重 置</el-button>
        </el-col>
      </el-row>
    </el-form>
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
        width="180"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot1"
        label="物品批属性1"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot2"
        label="物品批属性2"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot3"
        label="物品批属性3"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot4"
        label="物品批属性4"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot5"
        label="物品批属性5"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot6"
        label="物品批属性6"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot7"
        label="物品批属性7"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot8"
        label="物品批属性8"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot9"
        label="物品批属性9"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot10"
        label="物品批属性10"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot11"
        label="物品批属性11"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot12"
        label="物品批属性12"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot13"
        label="物品批属性13"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot14"
        label="物品批属性14"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="skuLot15"
        label="物品批属性15"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="proTypeDesc"
        label="处理类型"
        width="160"
        show-overflow-tooltip
      ></el-table-column>
      <el-table-column
        property="procTime"
        label="操作时间"
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
    <!-- <common-goods
      ref="commGood"
      :dialogCommGoods="dialogCommGoods"
      @cancelGoods="cancelGoods"
      @goodsDataSuccess="goodsDataSuccess"
    ></common-goods> -->
  </el-dialog>
</template>
 
<script>
import request from "@/router/axios";
import { getPage } from "@/api/wms/count/lotLog.js";
import { getAll } from "@/api/wms/warehouse/warehouse.js";
//import commonGoods from "@/components/secondDialog/commonGoods.vue"; //引入物品二级弹框

export default {
  name: "traceDialog",
  components: {
    // commonGoods
  },
  props: {
    isShowDialog: {
      type: Boolean,
      default: true,
    },
    titleText: {
      type: String,
      default: "批次跟踪",
    },
    whId: {
      tyep: String,
      default: "",
    },
    sku: {
      type: Object,
      default: [],
    },
    lotNumber: {
      type: String,
      default: "",
    },
  },
  data() {
    return {
      dataLoading: false, //是否显示加载中
      showDialog: this.isShowDialog, //批次跟踪弹框是否显示
      dialogCommGoods: false, //物品二级弹框是否显示
      warehouseSource: [], //库房下拉数据
      gridData: [], //批次日志
      searchQuery: {
        whId: "",
        lotNumber: "",
        skuId: "",
        skuCode: "",
        skuName: "",
      },
      page: {
        pageSize: 10,
        currentPage: 1,
        total: 0,
      },
    };
  },
  //mounted() {},
  methods: {
    dialogOpen() {
      this.getwhData();
      this.searchQuery.whId = this.whId;
      if (this.sku) {
        this.searchQuery.skuId = this.sku.skuId;
        this.searchQuery.skuCode = this.sku.skuCode;
        this.searchQuery.skuName = this.sku.skuName;
        this.searchQuery.lotNumber = this.lotNumber;
      }
      this.onLoad(this.page, this.searchQuery);
    },
    //查询
    refreshChange() {
      this.onLoad(this.page, this.searchQuery);
    },
    //重置
    searchReset() {
      this.searchQuery = {};
      this.onLoad(this.page);
    },
    onLoad(page, params = {}) {
      this.dataLoading = true;
      getPage(
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
    //默认获取 库房
    getwhData() {
      getAll().then((res) => {
        this.warehouseSource = res.data;
      });
    },
    //库房下拉改变事件
    getwhId(val) {
      this.searchQuery.whId = val;
    },
    //取消
    childCancel() {
      this.$emit("child-cancel");
    },
    Onclose() {
      this.$refs.searchQuery.resetFields();
    },
    //子级物品确定回调
    goodsDataSuccess(val) {
      this.searchQuery.skuName = val.skuName;
      this.searchQuery.skuCode = val.skuCode;
      this.searchQuery.skuId = val.skuId;
      this.dialogCommGoods = false;
    },
    //子级物品取消回调
    cancelGoods() {
      this.dialogCommGoods = false;
    },
  },
  watch: {
    isShowDialog(val) {
      this.showDialog = val; //②监听外部对props属性result的变更，并同步到组件内的data属性myResult中
    },
    showDialog(val) {
      this.$emit("on-result-change", val); //③组件内对myResult变更后向外部发送事件通知
    },
  },
};
</script>
 
<style>
.el-form .el-row {
  margin: 10px 0;
}
</style>