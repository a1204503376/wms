<template>
  <span>
    <el-input
      :placeholder="placeholder"
      v-model="showText"
      size="medium"
      readonly
      class="select-input"
      suffix-icon="el-icon-office-building"
      :disabled="disabled"
      v-on:click.native="onClick"
    >
    </el-input>
    <el-dialog
      :title="title"
      :visible.sync="visible"
      :close-on-click-modal="false"
      @open="dialogOpen"
      :before-close="dialogClose"
      v-dialogDrag="true"
      width="80%"
      top="3vh"
      id="my_dialogs"
      append-to-body
    >
      <el-form size="medium" label-position="right" label-width="80px">
        <div class="dialog__body">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="企业编码" prop="enterpriseCode">
                <el-input
                  v-model="searchData.enterpriseCode"
                  placeholder="请输入企业编码"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="企业名称" prop="enterpriseName">
                <el-input
                  v-model="searchData.enterpriseName"
                  placeholder="请输入企业名称"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item></el-form-item>
            </el-col>
            <el-col :span="2">
              <el-button type="primary" @click="onSearch" size="small"
                >查 询</el-button
              >
            </el-col>
            <el-col :span="2">
              <el-button type="danger" @click="searchReset" size="small"
                >重 置</el-button
              >
            </el-col>
          </el-row>
        </div>
      </el-form>
      <el-table
        :data="data"
        border
        @current-change="handleCurrentChange"
        @cell-dblclick="dblhandleCurrentChange"
        highlight-current-row
        ref="singleTable"
        :header-cell-style="{
          'background-color': '#fafafa',
        }"
        height="400px"
        max-height="400px"
        overflow="auto"
      >
        <el-table-column
          property="enterpriseCode"
          label="企业编码"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          property="enterpriseName"
          label="企业名称"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          property="enterpriseNameS"
          label="企业简称"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          property="enterpriseTypeDesc"
          label="企业类型"
          width="150"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
        <el-table-column
          property="ownerName"
          label="货主"
          width="200"
          align="center"
          show-overflow-tooltip
        ></el-table-column>
      </el-table>
      <div style="text-align: left; padding-top: 10px">
        <el-pagination
          background
          @size-change="sizeChange"
          @current-change="currentChange"
          :current-page="page.currentPage"
          :page-sizes="[20, 50, 100]"
          :page-size="page.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="page.total"
        ></el-pagination>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="addEnterprise">确 定</el-button>
        <el-button @click="dialogClose">取 消</el-button>
      </div>
    </el-dialog>
  </span>
</template>

<script>
import { getPage } from "@/api/wms/basedata/enterprise";

export default {
  name: "selectEnterprise",
  props: {
    value: { type: String, default: "" },
    text: { type: String, default: "" },
    disabled: { type: Boolean, default: false },
    placeholder: { type: String, default: "请选择" },
    search: {
      type: Object,
      default: function () {
        return {
          enterpriseCode: null,
          enterpriseName: null,
          enterpriseNameS: null,
          // 101:供应商， 102：客户， 103：承运商
          enterpriseType: null,
          woId: null,
        };
      },
    },
  },
  model: {
    prop: "value",
    event: "change",
  },
  data() {
    return {
      title: "请选择",
      showText: "",
      loading: false,
      data: [],
      page: {
        pageSize: 20,
        currentPage: 1,
        total: 0,
      },
      dic: [],
      currentRow: null, //选中行
      visible: false,
      searchData: {},
    };
  },
  mounted() {
    this.showText = this.text;
    this.searchData = Object.assign({}, this.search);
  },
  updated() {
    if (this.$refs.singleTable && this.gridData && this.gridData.length > 0) {
      this.$refs.singleTable.setCurrentRow(this.gridData[0]);
    }
  },
  watch: {
    value: {
      handler: function (newValue, oldValue) {
        // if (!newValue) {
        //     this.showText = '';
        // }
        if (newValue !== oldValue && newValue) {
          this.$emit("change", newValue);
        }
      },
    },
    text(val) {
      this.showText = val;
    },
  },
  methods: {
    onClick() {
      if (!this.disabled) {
        this.visible = true;
      }
    },
    dialogOpen() {
      this.searchData = Object.assign({}, this.search);
      this.onLoad(this.page, Object.assign({}, this.search));
    },
    onLoad(page, params) {
      this.loading = true;
      getPage(page.currentPage, page.pageSize, params)
        .then((res) => {
          this.data = res.data.data.records;
          this.page.total = res.data.data.length;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    searchReset() {
      this.page.currentPage = 1;
      this.searchData = Object.assign({}, this.search);
      this.onLoad(this.page, this.searchData);
    },
    sizeChange(size) {
      this.page.pageSize = size;
      let search = Object.assign({}, this.search);
      search.enterpriseCode = this.searchData.enterpriseCode;
      search.enterpriseName = this.searchData.enterpriseName;
      this.onLoad(this.page, search);
    },
    currentChange(current) {
      this.page.currentPage = current;
      let search = Object.assign({}, this.search);
      search.enterpriseCode = this.searchData.enterpriseCode;
      search.enterpriseName = this.searchData.enterpriseName;
      this.onLoad(this.page, search);
    },
    //获取选中用户的数据
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    //取消按钮事件
    dialogClose() {
      this.data = [];
      this.visible = false;
    },
    //双击选中
    dblhandleCurrentChange() {
      this.addEnterprise();
    },
    //确定
    addEnterprise() {
      this.data = [];
      this.visible = false;
      if (this.currentRow) {
        // this.value = this.currentRow.peId;
        let obj = Object.assign({}, this.currentRow);
        this.showText = obj.enterpriseName;
        this.$emit("change", obj.peId, obj);
      }
    },
    //模糊搜索
    onSearch() {
      this.page.currentPage = 1;
      this.onLoad(this.page, this.searchData);
    },
  },
};
</script>

<style lang="scss">
.select-input .el-input__inner {
  cursor: pointer;
}
.dialog__body {
  margin-top: 20px;
  overflow-x: hidden;
}
.el-pager {
  border: none;
}
</style>
