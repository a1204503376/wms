<template>
  <div id="supplier">
    <nodes-master-page :configure="masterConfig" v-on="form.events" :permission="permissionObj">
      <template v-slot:searchFrom>
        <el-form-item label="供应商编码">
          <el-input v-model="form.params.code"></el-input>
        </el-form-item>
        <el-form-item label="供应商名称">
          <el-input v-model="form.params.name"></el-input>
        </el-form-item>

      </template>
      <template v-slot:expandSearch>
        <el-row type="flex">
          <el-col :span="24">
            <el-form-item label="创建日期">
              <nodes-date-range v-model="form.params.createTimeDateRange"></nodes-date-range>
            </el-form-item>
            <el-form-item label="更新日期">
              <nodes-date-range v-model="form.params.updateTimeDateRange"></nodes-date-range>
            </el-form-item>
          </el-col>
        </el-row>
      </template>
      <template v-slot:batchBtn>
        <el-button size="mini" type="primary" icon="el-icon-plus" v-if="permissionObj.add">新增</el-button>
        <el-button size="mini" type="danger" plain icon="el-icon-delete" @click="onRemove" v-if="permissionObj.delete">删除</el-button>
      </template>
      <template v-slot:tableTool>
        <el-tooltip :enterable="false" class="item" content="刷新" effect="dark" placement="top">
          <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
        </el-tooltip>
        <el-tooltip :enterable="false" class="item" content="显隐" effect="dark" placement="top">
          <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
        </el-tooltip>
        <el-tooltip :enterable="false" class="item" content="本地导出" effect="dark" placement="top">
          <el-button circle icon="el-icon-bottom" size="mini"></el-button>
        </el-tooltip>
        <el-tooltip :enterable="false" class="item" content="服务端导出" effect="dark" placement="top">
          <el-button circle icon="el-icon-download" @click="exportData" size="mini"></el-button>
        </el-tooltip>
      </template>
      <template v-slot:table>
        <el-table ref="table" :data="table.data" :summary-method="getSummaries" border highlight-current-row
          show-summary size="mini" style="width: 100%" @sort-change="onSortChange">
          <el-table-column fixed type="selection" width="50">
          </el-table-column>
          <template v-for="(column, index) in table.columnList">
            <el-table-column v-if="!column.hide" :key="index" show-overflow-tooltip v-bind="column">
            </el-table-column>
          </template>
          <el-table-column prop="status" label="启用" width="100"
            :filters="[{ text: '是', value: 1 }, { text: '否', value: -1 }]" :filter-method="filterTag"
            filter-placement="bottom-end">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'" disable-transitions>{{ scope.row.status ===
                  1 ? '是' : '否'
              }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </template>
      <template v-slot:page>
        <el-pagination :current-page="page.current" :page-size="page.size" :page-sizes="[20, 50, 100]"
          :total="page.total" background v-bind="page" layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange" @current-change="handleCurrentChange">
        </el-pagination>
      </template>
    </nodes-master-page>
    <dialog-column v-bind="columnShowHide" @close="onColumnShowHide">
    </dialog-column>
  </div>
</template>

<script>
import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
// import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
// import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import { listMixin } from "@/mixins/list";
import { page, remove, exportFile } from "@/api/wms/basics/supplier";
import fileDownload from "js-file-download";


export default {
  name: "supplier",
  components: {
    DialogColumn,
    NodesSearchInput,
    // NodesInStoreMode,
    // NodesAsnBillState,
    NodesMasterPage,
    NodesDateRange,
  },
  mixins: [listMixin],
  data() {
    return {
      masterConfig: {
        showExpandBtn: true,
      },
      form: {
        params: {
          code: "",
          name: "",
          createTimeDateRange: ["", ""],
          updateTimeDateRange: ["", ""],
        },
      },
      table: {
        columnList: [
          {
            prop: "code",
            label: "供应商编码",
            sortable: "custom",
          },
          {
            prop: "name",
            label: "供应商名称",
          },
          {
            prop: "simpleName",
            label: "供应商简称",
          },
          {
            prop: "ownerName",
            label: "货主",
          },
          {
            prop: "remark",
            label: "备注",
          },
          {
            prop: "createTime",
            width: 130,
            label: "创建时间",
          },
          {
            prop: "createUser",
            label: "创建人",
          },
          {
            prop: "updateTime",
            width: 130,
            label: "更新时间",
          },
        ],
      },
    };
  },
  computed: {
    permissionObj() {
      return {
        search: this.vaildData(this.permission.supplier_search, false),
        add: this.vaildData(this.permission.supplier_add, false),
        delete: this.vaildData(this.permission.supplier_delete, false)
      }
    }
  },
  created() { },
  methods: {
    getTableData() {
      page(this.page, this.form.params)
        .then((res) => {
          let pageObj = res.data.data;
          this.table.data = pageObj.records;
          this.page.total = pageObj.total;
          console.log(pageObj);
        })
        .catch((e) => {
          console.log(e);
        });
    },
    onReset() {
      this.form.params = {
        name: '',
        code: '',
        createTimeDateRange: [],
        updateTimeDateRange: []
      }
    },
    onRemove() {
      let rows = this.$refs.table.selection;
      if (rows.length <= 0) {
        this.$message({
          message: "警告，至少选择一条记录",
          type: "warning",
        });
        return;
      }
      this.$confirm("此操作将删除, 是否删除?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let removeObj = {
            ids: []
          };
          rows.forEach((item) => {
            removeObj.ids.push(item.id);
          });
          remove(removeObj)
            .then((res) => {
              this.$message({
                type: "success",
                message: res.data.msg,
              });
              this.getTableData();
            })
            .catch((e) => {
              console.log(e);
            });
        })
      // .catch(() => {
      //   this.$message({
      //     type: "info",
      //     message: "已取消删除",
      //   });
      // });
    },
    exportData() {
      this.loading = true;
      exportFile(this.form.params)
        .then((res) => {
          this.$message.success("操作成功，正在下载中...");
          fileDownload(res.data, "供应商.xlsx");
        })
        .catch(() => {
          this.$message.error("系统模板目录配置有误或文件不存在");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    filterTag(value, row, column) {
      return row.status === value;
    },
    getSummaries() { },
  },
};
</script>
