<template>
  <basic-container>
    <nodes-crud
      ref="table"
      v-model="form"
      :option="option"
      :data="data"
      :table-loading="loading"
      :before-open="beforeOpen"
      :permission="permissionList"
      @on-load="onLoad"
      @row-save="rowSave"
      @on-del="onDel"
      @on-multi-del="onMultiDel"
      @selection-change="selectionChange"
      @search-change="searchChange"
    >
      <template slot="menuLeft">
        <el-dropdown trigger="click" @command="handleCommand">
          <el-button type="primary" size="mini">
            <i class="el-icon-edit-outline"></i>
            操作
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-if="this.permission.owner_import" command="1" icon="el-icon-upload2"
              >导入
            </el-dropdown-item>
            <el-dropdown-item v-if="permission.owner_export" command="2" icon="el-icon-download"
              >导出
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </template>
    </nodes-crud>
    <file-upload
      :visible="fileUpload.visible"
      template-url="/api/wms/basedata/owner/export-template"
      file-name="货主"
      @callback="callbackFileUpload"
    ></file-upload>
    <data-verify
      :visible="dataVerify.visible"
      :dataSource="dataVerify.dataSource"
      uploadUrl="/api/wms/basedata/owner/import-data"
      dataVerifyUrl="/api/wms/basedata/owner/import-valid"
      @callback="callbackDataVerify"
    ></data-verify>
  </basic-container>
</template>

<script>
import {
  getPage,
  add,
  remove,
  getDetail,
  exportFile,
} from "@/api/wms/basedata/owner.js";
import { group as group_1 } from "./owner/group_1";
import { group as group_2 } from "@/views/wms/common/address";
import { group as group_3 } from "@/views/wms/common/contacts";
import fileUpload from "@/components/nodes/fileUpload";
import dataVerify from "@/components/nodes/dataVerify";
import fileDownload from "js-file-download";
import {mapGetters} from "vuex";

export default {
  name: "outstock",
  components: {
    fileUpload,
    dataVerify,
  },
  data() {
    return {
      loading: false,
      form: {},
      data: [],
      selectionList: [],
      fileUpload: {
        visible: false,
      },
      dataVerify: {
        visible: false,
        dataSource: {},
      },
      option: {
        newBtn: true,
        multiDelBtn: true,
        viewBtn: true,
        editBtn: true,
        delBtn: true,
        menu: true,
        custom: false,
        column: [
          {
            label: "货主编码",
            prop: "ownerCode",
            search: true,
            view: true,
            placeholder: "支持模糊查询",
            sortable: true,
          },
          {
            label: "货主名称",
            prop: "ownerName",
            search: true,
            width: 160,
            placeholder: "支持模糊查询",
            sortable: true,
          },
          {
            label: "国家",
            prop: "ownerCountry",
            search: true,
            sortable: true,
          },
          {
            label: "省份",
            prop: "ownerProvince",
            search: true,
            sortable: true,
          },
          {
            label: "城市",
            prop: "ownerCity",
            search: true,
            sortable: true,
          },
          {
            label: "邮编",
            prop: "ownerZipCode",
            search: true,
          },
        ],
        group: [],
      },
      searchData: {},
    };
  },
  mounted() {
    this.option.group.push(group_1);
    this.option.group.push(group_2);
    this.option.group.push(group_3);
  },
  computed: {
      ...mapGetters(["permission"]),
      permissionList() {
          return {
              add: this.vaildData(this.permission.owner_add, false),
              view: this.vaildData(this.permission.owner_view, false),
              edit: this.vaildData(this.permission.owner_edit, false),
              delete: this.vaildData(this.permission.owner_delete, false),
              导入: this.vaildData(this.permission.owner_import, false),
              导出: this.vaildData(this.permission.owner_export, false),
          }
      },
    ids() {
      let ids = [];
      this.selectionList.forEach((ele) => {
        ids.push(ele.woId);
      });
      return ids.join(",");
    },
  },
  methods: {
    onLoad(page, params = {}) {
      this.loading = true;
      getPage(
        page.currentPage,
        page.pageSize,
        Object.assign(params, this.query)
      ).then((res) => {
        const data = res.data.data;
        page.total = data.total;
        this.data = data.records;
        this.loading = false;
        this.selectionClear();
      });
    },
    rowSave(row, loading, done, type) {
      add(row).then(
        () => {
          loading();
          this.$refs.table.onLoad();
          this.$message.success("操作成功！");
        },
        (error) => {
          done();
        }
      );
    },
    onDel(row, index) {
      this.$confirm("确定删除当前数据？", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        remove(row.woId).then(() => {
          this.$refs.table.onLoad();
          this.$message({
            type: "success",
            message: "操作成功!",
          });
        });
      });
    },
    onMultiDel() {
      if (!this.selectionList || this.selectionList.length == 0) {
        this.$message.warning("至少选择一条数据！");
        return;
      }
      this.$confirm("确定将选择数据删除?", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        remove(this.ids).then(() => {
          this.$refs.table.onLoad();
          this.$message({
            type: "success",
            message: "操作成功!",
          });
          this.$refs.table.toggleSelection();
        });
      });
    },
    beforeOpen(done, type, finish) {
      if (["edit", "view"].includes(type)) {
        getDetail(this.form.woId)
          .then((res) => {
            this.form = res.data.data;
          })
          .finally(() => {
            done();
          });
      }
    },
    selectionChange(val) {
      this.selectionList = val;
    },
    selectionClear() {
      this.selectionList = [];
      this.$refs.table.toggleSelection();
    },
    handleCommand(cmd) {
      switch (parseInt(cmd)) {
        case 1:
          this.fileUpload.visible = true;
          break;
        case 2:
          this.loading = true;
          exportFile(this.searchData)
            .then((res) => {
              this.$message.success("操作成功，正在下载中...");
              fileDownload(res.data, "货主.xlsx");
            })
            .catch(() => {
              this.$message.error("系统模板目录配置有误或文件不存在");
            })
            .finally(() => {
              this.loading = false;
            });
          break;
      }
    },
    callbackFileUpload(res) {
      this.fileUpload.visible = res.visible;
      if (res.result) {
        this.dataVerify.dataSource = res.data;
        this.dataVerify.visible = true;
      }
    },
    callbackDataVerify(res) {
      this.dataVerify.visible = res.visible;
      if (res.result) {
        this.$message.success("导入成功！");
          this.$refs.table.onLoad();
      }
    },
    searchChange(data) {
      this.searchData = data;
    },
  },
};
</script>

<style scoped>
</style>
