<template>
  <basic-container>
    <nodes-crud
      ref="table"
      :option="option"
      :data="data"
      v-model="form"
      :table-loading="loading"
      :permission="permissionList"
      @on-load="onLoad"
      @on-del="onDel"
      @row-save="rowSave"
      @on-multi-del="onMultiDel"
      @selection-change="selectionChange"
      @search-change="searchChange"
      :before-open="beforeOpen"
    >
      <template slot="menuLeft">
        <el-dropdown trigger="click" @command="handleCommand">
          <el-button type="primary" size="mini">
            <i class="el-icon-edit-outline"></i>
            操作
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-if="permission.skulot_import" command="1" icon="el-icon-upload2"
              >导入</el-dropdown-item
            >
            <el-dropdown-item v-if="permission.skulot_export" command="2" icon="el-icon-download"
              >导出</el-dropdown-item
            >
          </el-dropdown-menu>
        </el-dropdown>
      </template>
    </nodes-crud>
    <file-upload
      :visible="fileUpload.visible"
      template-url="/api/wms/basedata/skulot/export-template"
      file-name="批属性"
      @callback="callbackFileUpload"
    ></file-upload>
    <data-verify
      :visible="dataVerify.visible"
      :dataSource="dataVerify.dataSource"
      uploadUrl="/api/wms/basedata/skulot/import-data"
      dataVerifyUrl="/api/wms/basedata/skulot/import-valid"
      @callback="callbackDataVerify"
    ></data-verify>
  </basic-container>
</template>

<script>
import {
  getPage,
  remove,
  add,
  getLableDetail,
  getLotLength,
  exportFile,
} from "@/api/wms/basedata/skulot";
import { getParamValue } from "@/util/param";
import { group as group_1 } from "./skulot/group_1.js";
import { group as group_2 } from "./skulot/group_2.js";
import { group as group_3 } from "./skulot/group_3.js";
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
      data: [],
      selectionList: [],
      form: {},
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
        column: [
          {
            label: "编码",
            prop: "skuLotCode",
            search: true,
            view: true,
            width: 120,
            placeholder: "支持模糊查询",
            sortable: true,
          },
          {
            label: "名称",
            prop: "skuLotName",
            search: true,
            width: 120,
            placeholder: "支持模糊查询",
            sortable: true,
          },
          {
            label: "所属货主",
            prop: "woId",
            search: true,
            type: "select",
            dicUrl: "/api/wms/basedata/owner/list",
            props: {
              label: "ownerName",
              value: "woId",
            },
          },
          {
            label: "备注",
            prop: "remark",
            width: 200,
          },
        ],
        custom: false,
        group: [],
      },
      searchData: {},
    };
  },
  computed: {
      ...mapGetters(["permission"]),
      permissionList() {
          return {
              add: this.vaildData(this.permission.skulot_add, false),
              delete: this.vaildData(this.permission.skulot_delete, false),
              edit: this.vaildData(this.permission.skulot_edit, false),
              view: this.vaildData(this.permission.skulot_view, false),
              导入: this.vaildData(this.permission.skulot_import, false),
              导出: this.vaildData(this.permission.skulot_export, false),
          }
      },
    ids() {
      let ids = [];
      this.selectionList.forEach((ele) => {
        ids.push(ele.wslId);
      });
      return ids.join(",");
    },
  },
  mounted() {
    const lotCount = getParamValue(this.$param.system.lotCount);
      let column = this.option.column;
      for (let i = 1; i <= lotCount; i++) {
          let skuLot = {
              label: "批属性" + i,
              prop: "skuLot" + i,
              width: 120,
              maxlength: 200,
              sortable: true,
          };
          column.push(skuLot);
      }
      for (let i = 1; i <= lotCount; i++) {
          let skuLot = {
              label: "标签" + i,
              prop: "skuLotLabel" + i,
              width: 110,
              maxlength: 200,
              sortable: true,
          };
          column.push(skuLot)
      }
    group_2.column.length = 0;
    group_3.column.length = 0;
    for (let i = 1; i <= lotCount; i++) {
      group_2.column.push({
        prop: "skuLot" + i,
        label: "批属性" + i,
        maxlength: 200,
          rules:[{required: true, message: "批属性"+i+"不能为空", trigger: "blur", pattern:/\S/,}],
      });
      group_3.column.push({
        prop: "skuLotLabel" + i,
        label: "标签" + i,
        maxlength: 200,
          rules:[{required: true, message: "标签"+i+"不能为空", trigger: "blur", pattern:/\S/,}],
      });
    }

    this.option.group.push(group_1);
    this.option.group.push(group_2);
    this.option.group.push(group_3);
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
    //保存
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
    beforeOpen(done, type, finish) {
      //console.log(done,type,finish,'done')
      if (["edit", "view"].includes(type)) {
        getLableDetail(this.form.wslId)
          .then((res) => {
            //console.log(res,'beforeOpen')
            this.form = res.data.data;
          })
          .catch(() => {})
          .finally(() => {
            done();
          });
      }
    },
    onDel(row, index) {
      this.$confirm("确定删除当前数据？", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        remove(row.wslId).then(() => {
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
              fileDownload(res.data, "批属性.xlsx");
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
