<template>
  <div class="row-search">
    <el-table border :data="tableData" :show-header="false" ref="table">
      <el-table-column
      fixed
        width="50">
          <el-button
            type="primary"
            size="mini"
            circle
            icon="el-icon-search"
            @click="filterable"
          ></el-button>
      </el-table-column>
      <template v-for="(col, index) in columns">
        <el-table-column
          v-if="!col.hide"
          :prop="col.prop"
          :label="col.label"
          :min-width="col.width"
          :key="index"
        >
          <template slot-scope="scope">
          <!-- <template v-if="option.columnFilter"> -->
            <template v-if="col.type == 'tableSelect'">
              <el-select
                v-model="scope.row[col.prop]"
                placeholder="请选择"
                collapse-tag
                clearable
                style="width: 100%"
                size="small"
                @change="(val)=>selectChange(val, null, col, scope)"
              >
                <el-option
                  v-for="item in col.dicData"
                  :key="col.props ? item[col.props.value] : item['value']"
                  :label="col.props ? item[col.props.label] : item['label']"
                  :value="col.props ? item[col.props.value] : item['value']"
                ></el-option>
              </el-select>
            </template>
            <template v-else-if="col.type == 'datePicker'">
                  <el-date-picker
                    v-model="scope.row[col.prop]"
                    type="datetime"
                    style="width: 100%"
                    format="yyyy-MM-dd HH:mm"
                    value-format="yyyy-MM-dd HH:mm"
                    size="small"
                    placeholder="选择日期">
                  </el-date-picker>
            </template>
            <template v-else>
                  <el-input
                  :placeholder="!col.readonly ? '请输入' : ''"
                  v-model="scope.row[col.prop]"
                  clearable
                  :readonly="col.readonly"
                  :maxlength="col.maxlength"
                  :minlength="col.minlength"
                  show-word-limit
                  size="small"
                  >
                  </el-input>
              </template>
          </template>
          <!-- </template> -->
        </el-table-column>
      </template>
      <el-table-column
         v-if="option.menu"
         :fixed="columnOperate.fixed"
         :label="columnOperate.label"
         :align="columnOperate.align"
         :width="columnOperate.width"
      >
      <template></template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
let timer = null;
import request from "@/router/axios";
export default {
  name: "rowSearch",
  props: {
    option: Object,
    top: {
      //搜索行显示垂直位置(纵向位置)
      type: Number,
      default: 48,
    },
    // value: {
    //   //表格数据
    //   type: Array
    // },
    columns: {
      //表格列数据
      type: Array,
    },
    columnOperate: {
      type: Object
    }
  },
  activated(){

  },
  created() {
        // 初始化
        if (this.columns) {
          this.columns.filter((col) => {
            return col.hide === true ? false : true;
        })
        for (let i = 0; i < this.columns.length; i++) {
            let col = this.columns[i];
            if (!col) {
                continue;
            }
            // 调用接口获取数据源
            if (!col.dicUrl) {
                continue;
            }
            request({
                url: col.dicUrl,
                method: "get"
            }).then(res => {
                this.$set(col, "dicData", res.data.data);
            });
        }
    }
  },
  mounted() {},
  data() {
    return {
      tableData: [{}],
    };
  },
  methods: {
    selectChange(val, obj, col, scope, clear = true) {
     // alert(1111)
      if (col) {
        // 获取对象
        if (col.dicData) {
            obj = this.findObj(val, col.props ? col.props.value : 'value', col.dicData);
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
    findObj(val, prop, dicData) {
        for (let i = 0; i < dicData.length; i++) {
            let data = dicData[i];
            if (data[prop] === val) {
                return data;
            }
        }
    },
    clearItem(items) {
      if (!items) {
          return;
      }
      items.forEach(item => {
          // 清空数据源的值
          this.columns.forEach(column => {
              // 清空子级
              if (column.prop === item) {
                  if (column['dicData']) {
                      this.$set(column, 'dicData', []);
                  }
                  if (column.cascaderItem) {
                      this.clearItem(column.cascaderItem);
                  }
              }
          });
      });
    },
    filterable() {
      this.$emit("filterable", this.tableData[0]);
    },
  },
};
</script>

<style lang="scss">
.row-search {
  position: absolute;
  z-index: 99;
  width: 100%;
  left: 0;
  top: 35px;
  height: 30px;
  overflow: hidden;
  border-bottom: 1px solid #EBEEF5;
  .el-input--small .el-input__inner {
    height: 25px;
    line-height: 25px;
  }
  .el-table__body-wrapper::-webkit-scrollbar { width: 0 !important }
  .el-table__body-wrapper { -ms-overflow-style: none; overflow: -moz-scrollbars-none; }
}
</style>
