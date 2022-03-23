<template>
  <span>
    <el-input
      :placeholder="placeholder"
      v-model="showText"
      size="medium"
      readonly
      class="select-input"
      suffix-icon="el-icon-box"
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
      <el-form size="medium" label-position="right" label-width="100px">
        <div class="dialog__body">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="包装名称" prop="wspName">
                <el-input
                  v-model="searchData.wspName"
                  placeholder="请输入包装名称"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="上线包装名称" prop="onlinePackage">
                <el-input
                  v-model="searchData.onlinePackage"
                  placeholder="请输入上线包装名称"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="上线包装类型" prop="packageType">
                <el-input
                  v-model="searchData.packageType"
                  placeholder="请输入上线包装类型"
                ></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="2">
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
          <el-row>
            <el-col :span="24">
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
                  property="wspName"
                  label="包装名称"
                  align="center"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  property="onlinePackage"
                  label="上线包装名称"
                  align="center"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  property="packageType"
                  label="上线包装类型"
                  align="center"
                  show-overflow-tooltip
                ></el-table-column>
                <el-table-column
                  property="spec"
                  label="规格"
                  align="center"
                  show-overflow-tooltip
                ></el-table-column>
              </el-table>
            </el-col>
          </el-row>
        </div>
      </el-form>
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
        <el-button type="primary" @click="dialogConfirm">确 定</el-button>
        <el-button @click="dialogClose">取 消</el-button>
      </div>
    </el-dialog>
  </span>
</template>

<script>
import { getPage } from "@/api/wms/basedata/skupackage";

export default {
  name: "selectSkuPackage",
  props: {
    value: { type: String, default: "" },
    text: { type: String, default: "" },
    disabled: { type: Boolean, default: false },
    placeholder: { type: String, default: "请选择" },
    search: {
      type: Object,
      default: function () {
        return {
          wspName: "",
          onlinePackage: "",
          packageType: "",
          wspIds: "",
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
      this.onLoad(this.page, Object.assign({}, this.search));
    },
    dialogConfirm() {
      this.visible = false;
      if (this.currentRow) {
        this.showText = this.currentRow.wspName;
        this.$emit("change", this.currentRow.wspId, this.currentRow);
      }
    },
    dialogClose() {
      this.visible = false;
    },
    onLoad(page, params) {
      this.loading = true;
      getPage(page.currentPage, page.pageSize, params)
        .then((res) => {
          this.data = res.data.data.records;
          this.page.total = res.data.data.total;
        })
        .finally(() => {
          this.loading = false;
        });
    },
    //模糊搜索
    onSearch() {
      this.page.currentPage = 1;
      this.onLoad(this.page, this.searchData);
    },
    searchReset() {
      this.page.currentPage = 1;
      this.searchData = Object.assign({}, this.search);
      this.onLoad(this.page, this.searchData);
    },
    sizeChange(size) {
      this.page.pageSize = size;
      let search = Object.assign({}, this.search);
      this.onLoad(this.page, search);
    },
    currentChange(current) {
      this.page.currentPage = current;
      let search = Object.assign({}, this.search);
      this.onLoad(this.page, search);
    },
    //获取选中用户的数据
    handleCurrentChange(val) {
      this.currentRow = val;
    },
    //双击选中
    dblhandleCurrentChange() {
      this.dialogConfirm();
    },
  },
};
</script>

<style scoped>
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
