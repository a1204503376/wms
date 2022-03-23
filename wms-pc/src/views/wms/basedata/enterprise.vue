import fileDownload from "js-file-download";
<template>
  <basic-container>
    <nodes-crud ref="table" v-model="form" :option="option" :data="data" :table-loading="loading"
      :before-open="beforeOpen" :permission="permissionList" @on-load="onLoad" @row-save="rowSave" @on-del="onDel"
      @on-multi-del="onMultiDel" @selection-change="selectionChange" @menu-command="menuCommand"
      @search-change="searchChange">
      <template slot="menuLeft">
        <el-dropdown trigger="click" @command="handleCommand">
          <el-button type="primary" size="mini">
            <i class="el-icon-edit-outline"></i>
            操作
            <i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item v-if="permission.enterprise_import" command="1" icon="el-icon-upload2">导入
            </el-dropdown-item>
            <el-dropdown-item v-if="permission.enterprise_export" command="2" icon="el-icon-download">导出
            </el-dropdown-item>
            <!-- <el-dropdown-item v-if="permission.enterprise_labelData" command="3"
                                          icon="el-icon-copy-document" divided>标签数据
                        </el-dropdown-item> -->
          </el-dropdown-menu>
        </el-dropdown>
      </template>
    </nodes-crud>

    <file-upload :visible="fileUpload.visible" template-url="/api/wms/basedata/enterprise/export-template"
      file-name="来往企业" @callback="callbackFileUpload"></file-upload>
    <data-verify :visible="dataVerify.visible" :dataSource="dataVerify.dataSource"
      uploadUrl="/api/wms/basedata/enterprise/import-data" dataVerifyUrl="/api/wms/basedata/enterprise/import-valid"
      @callback="callbackDataVerify"></data-verify>
    <label-data :visible="labelData.visible" @callback="callbackLabelData"></label-data>
    <bind-label-data :visible="bindLabelData.visible" :dataId="bindLabelData.dataId" @callback="callbackBindLabelData">
    </bind-label-data>
  </basic-container>
</template>

<script>
  import { getPage, add, remove, getDetail, exportFile } from "@/api/wms/basedata/enterprise.js";
  import { group as group_1 } from "./enterprise/group_1";
  import { group as group_2 } from "@/views/wms/common/address";
  import { group as group_3 } from "@/views/wms/common/contacts";
  import fileUpload from "@/components/nodes/fileUpload";
  import dataVerify from "@/components/nodes/dataVerify";
  import fileDownload from "js-file-download";
  import LabelData from "./enterprise/labelData";
  import BindLabelData from "./enterprise/bindLabelData";
  import { mapGetters } from "vuex";
  import region from '/public/json/region.json';

  export default {
    name: "outstock",
    components: {
      BindLabelData,
      LabelData,
      fileUpload,
      dataVerify,
    },
    data() {
      return {
        loading: false,
        form: {},
        data: [],
        selectionList: [],
        option: {
          newBtn: true,
          multiDelBtn: true,
          viewBtn: true,
          editBtn: true,
          delBtn: true,
          menu: true,
          custom: false,
          // menuItem: [
          //   {
          //     label: '关联标签',
          //     command: 1,
          //     divided: true
          //   }
          // ],
          column: [
            {
              label: "企业编码",
              prop: "enterpriseCode",
              placeholder: '支持模糊查询',
              search: true,
              view: true,
              width: 120,
              sortable: true
            },
            {
              label: "企业名称",
              prop: "enterpriseName",
              search: true,
              placeholder: '支持模糊查询',
              width: 200,
              sortable: true
            },
            {
              label: "企业简称",
              prop: "enterpriseNameS",
              search: true,
              width: 100,
              sortable: true
            },
            {
              label: "企业类型",
              prop: "enterpriseType",
              search: true,
              width: 100,
              type: "select",
              dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.enterpriseType,
              props: {
                label: "dictValue",
                value: "dictKey"
              }
            },
            {
              label: "所属货主",
              prop: "woId",
              search: true,
              type: "select",
              dicUrl: "/api/wms/basedata/owner/list",
              props: {
                label: "ownerName",
                value: "woId"
              }
            },
            {
              label: "国家",
              prop: "country",
                search: true,
              sortable: true,
            },
            {
              label: "省份",
              prop: "province",
                search: true,
              sortable: true
            },
            {
              label: "城市",
              prop: "city",
                search: true,
              sortable: true
            },
            {
              label: "邮编",
              prop: "zipCode",
              search: true
            }
          ],
          group: []
        },
        fileUpload: {
          visible: false
        },
        dataVerify: {
          visible: false,
          dataSource: {},

        },
        searchData: {},
        labelData: {
          visible: false
        },
        bindLabelData: {
          visible: false,
          dataId: null
        }
      };
    },
    computed: {
      ...mapGetters(["permission"]),
      permissionList() {
        return {
          add: this.vaildData(this.permission.enterprise_add, false),
          delete: this.vaildData(this.permission.enterprise_delete, false),
          edit: this.vaildData(this.permission.enterprise_edit, false),
          view: this.vaildData(this.permission.enterprise_view, false),
          导入: this.vaildData(this.permission.enterprise_import, false),
          导出: this.vaildData(this.permission.enterprise_export, false),
          // 标签数据: this.vaildData(this.permission.enterprise_labelData, false),
        }
      },
      ids() {
        let ids = [];
        this.selectionList.forEach(ele => {
          ids.push(ele.peId);
        });
        return ids.join(",");
      },
    },
    mounted() {
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
        ).then(res => {
          const data = res.data.data;
          page.total = data.total;
          this.data = data.records;
          this.loading = false;
          this.selectionClear();
        });
      },
      rowSave(row, loading, done, type) {
        let enterpriseTypeArray = Object.assign([], row.enterpriseType);
        row.enterpriseType = enterpriseTypeArray.join(',');
        add(row).then(
          () => {
            loading();
            this.$refs.table.onLoad();
            this.$message.success("操作成功！");
          },
          error => {
            row.enterpriseType = enterpriseTypeArray;
            done();
          }
        );
      },
      onDel(row, index) {
        this.$confirm("确定删除当前数据？", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          remove(row.peId).then(() => {
            this.$refs.table.onLoad();
            this.$message({
              type: "success",
              message: "操作成功!"
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
          type: "warning"
        }).then(() => {
          remove(this.ids).then(() => {
            this.$refs.table.onLoad();
            this.$message({
              type: "success",
              message: "操作成功!"
            });
            this.$refs.table.toggleSelection();
          });
        });
      },
      beforeOpen(done, type, finish) {
        if (["edit", "view"].includes(type)) {
          getDetail(this.form.peId)
            .then(res => {
              let strArray = res.data.data.enterpriseType.split(',')
              let enterpriseTypeArray = [];
              strArray.forEach(item => {
                enterpriseTypeArray.push(parseInt(item));
              });
              res.data.data.enterpriseType = enterpriseTypeArray;
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
      dialogResult(result) {
        if (result.result) {
          this.$refs.table.onLoad();
        }
        this.dialogEdit.visible = result.visible;
      },
      handleCommand(cmd) {
        switch (parseInt(cmd)) {
          case 1:     // 导入
            this.fileUpload.visible = true;
            break;
          case 2:     // 导出
            this.loading = true;
            exportFile(this.searchData).then(res => {
              this.$message.success('操作成功，正在下载中...');
              fileDownload(res.data, "来往企业.xlsx");
            }).catch(() => {
              this.$message.error('系统模板目录配置有误或文件不存在！');
            }).finally(() => {
              this.loading = false;
            });
            break;
          case 3:     // 标签数据
            this.labelData.visible = true;
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
      menuCommand(cmd, row) {
        switch (cmd) {
          case 1:
            this.bindLabelData.dataId = row.peId;
            this.bindLabelData.visible = true;
            break;
        }
      },
      callbackLabelData(res) {
        this.labelData.visible = res.visible;
      },
      callbackBindLabelData(res) {
        this.bindLabelData.visible = res.visible;
      }
    }
  };
</script>

<style scoped>
</style>
