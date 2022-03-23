<template>
  <el-drawer
    :before-close="beforeClose"
    :visible.sync="visible"
    label="rtl"
    size="320"
    :with-header="false"
    custom-class="demo-drawer"
    append-to-body
    ref="drawer"
  >
    <div class="demo-drawer__content">
      <!-- 顶部 -->
      <div class="drawerTop">
        <div class="closeIcon">
          <el-button icon="el-icon-d-arrow-right" size="small" @click="beforeClose"></el-button>
        </div>
        <div class="btn_container">
          <el-button type="primary" @click="searchForm">搜 索</el-button>
          <el-button type="warning" @click="resetForm">清 空</el-button>
        </div>
      </div>
      <!-- 内容（搜索元素） -->
      <el-form ref="form" :model="form" label-width="100px" size="medium">
        <el-row :gutter="20" v-for="(col, index) in this.column">
          <el-col :span="24">
            <el-form-item :label="col.label" :prop="col.prop">
              <!-- 下拉列表框 -->
              <template v-if="col.type === 'select'">
                <el-select
                  v-model="form[col.prop]"
                  filterable
                  clearable
                  :placeholder="col.placeholder || '请选择'"
                  style="width:100%;"
                  @change="(val)=>selectChange(val, null, col)"
                >
                  <el-option
                    v-for="item in col.dataSource"
                    :key="item[col.props.value]"
                    :value="item[col.props.value]"
                    :label="item[col.props.label]"
                  ></el-option>
                </el-select>
              </template>
              <!-- 下拉列表框（树形）-->
              <template v-else-if="col.type === 'select-tree'">
                <treeSelect
                  v-model="form[col.prop]"
                  :placeholder="col.placeholder || '请选择'"
                  :props="col.props"
                  :options="col.dataSource"
                  :clearable="col.clearable"
                  :accordion="col.accordion"
                  style="width:100%;"
                />
              </template>
              <!-- switch开关 -->
              <template v-else-if="col.type == 'switch'">
                <el-switch
                  v-model="form[col.prop]"
                  :active-text="col.dicData[0].label"
                  :inactive-text="col.dicData[1].label"
                  :active-value="col.dicData[0].value"
                  :inactive-value="col.dicData[1].value"
                ></el-switch>
              </template>
              <!-- 日期框 -->
              <template v-else-if="col.type === 'date'">
                <el-date-picker
                  v-model="form[col.prop]"
                  type="date"
                  :placeholder="col.placeholder || '请选择'"
                  value-format="yyyy-MM-dd HH:mm:ss"
                  style="width:100%;"
                ></el-date-picker>
              </template>
              <!-- 日期范围框 -->
              <template v-else-if="col.type === 'daterange'">
                <el-date-picker
                  v-model="form[col.prop]"
                  type="daterange"
                  start-placeholder="开始日期"
                  end-placeholder="结束日期"
                  value-format="yyyy-MM-dd HH:mm:ss"
                ></el-date-picker>
              </template>
              <!-- 选择人员 -->
              <template v-else-if="col.type === 'user'">
                <selectUserTable v-model="form[col.prop]" size="small"></selectUserTable>
              </template>
              <!-- 默认：输入框input -->
              <template v-else>
                <el-input
                  :placeholder="col.placeholder || '请输入'"
                  v-model="form[col.prop]"
                  clearable
                ></el-input>
              </template>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
  </el-drawer>
</template>

<script>
import request from "@/router/axios";
import treeSelect from "@/components/nodes/treeSelect";
import selectUserTable from "@/components/nodes/selectUserTable";

export default {
  // 搜索面板
  name: "searchPanel",
  components: {
    treeSelect,
    selectUserTable
  },
  props: {
    // 是否可見
    visible: {type: Boolean, default: false},
    // 显示的表单元素
    column: {
      type: Array,
      default: function () {
        return [];
      }
    }
  },
  mounted() {
    // 初始化
    this.$nextTick(function () {
      if (this.column) {
        for (let i = 0; i < this.column.length; i++) {
          let col = this.column[i];
          if (!col) {
            continue;
          }
          // 初始化 form 綁定的屬性
          if (!this.form[col.prop]) {
            this.$set(this.form, col.prop, "");
          }
          // 調用接口獲取數據源
          if (!col.dicUrl) {
            continue;
          }
          request({
            url: col.dicUrl,
            method: "get"
          }).then(res => {
            this.$set(col, "dataSource", res.data.data);
          });
        }
      }
    });
  },
  data() {
    return {
      form: {},
    };
  },
  methods: {
    // 关闭搜索面板
    beforeClose() {
      this.$emit("dialogResult", {visible: false});
    },
    // 重置表单
    resetForm() {
      if (this.$refs["form"]) {
        this.$refs["form"].resetFields();
      }
    },
    // 关闭搜索面板并传递 data
    searchForm() {
      this.$emit("dialogResult", {
        visible: false,
        result: true,
        data: this.form
      });
    },
    selectChange(val, obj, col, clear = true) {
      if (col) {
        // 获取对象
        if (col.dataSource) {
          obj = this.findObj(val, col.props ? col.props.value : 'value', col.dataSource);
        }
        // 级联不为空的情况下
        if (col.cascaderItem) {
          if (clear) {
            this.clearItem(col.cascaderItem);
          }
          // 获取当前列级联列
          col.cascader = [];
          this.column.forEach(column => {
            if (col.cascaderItem.indexOf(column.prop) > -1) {
              col.cascader.push(column);
            }
          });
        }
        if (col.change) {
          col.change(val, obj, col);
        }
      }
    },
    findObj(val, prop, dataSource) {
      for (let i = 0; i < dataSource.length; i++) {
        let data = dataSource[i];
        if (data[prop] === val) {
          return data;
        } else {
          if (data.children && data.children.length > 0) {
            data = this.findObj(val, prop, data.children);
            if (data) {
              return data;
            }
          }
        }
      }
    },
    clearItem(items) {
      if (!items) {
        return;
      }
      items.forEach(item => {
        // 清空字段的值
        if (this.form[item]) {
          if (this.form[item] instanceof Array) {
            this.$set(this.form, item, []);
          } else {
            this.$set(this.form, item, '');
          }
        }
        // 清空数据源的值
        this.column.forEach(column => {
          // 清空子级
          if (column.prop === item) {
            if (column['dataSource']) {
              this.$set(column, 'dataSource', []);
            }
            if (column.cascaderItem) {
              this.clearItem(column.cascaderItem);
            }
          }
        });
      });
    },
  }
};
</script>

<style scoped>
.drawerTop {
  width: 100%;
  height: 60px;
  background: white;
  display: flex;
  line-height: 50px;
  justify-content: space-between;
  vertical-align: center;
  border-bottom: 1px solid #cdcdcd;
  margin: 10px 10px 0px 0px;
}

.closeIcon {
  margin-left: 20px;
  margin-top: 3px;
}

.closeIcon .el-button--small {
  padding: 0;
  border: none;
  background: none;
  font-size: 20px;
}

.demo-drawer__content .el-form-item {
  margin-top: 20px;
  margin-bottom: 0px;
}

.demo-drawer__content {
  padding-right: 10px;
}
</style>
