<template>
    <el-select
        v-model="val"
        :multiple="true"
        :clearable="true"
        :remote-method="remoteMethod"
        :filterable="true"
        :collapse-tags="true"
        placeholder="请输入关键词"
        popper-class="myPopper"
        :remote="true"
        @remove-tag="onRemoveTag"
        @clear="onClear">
        <template style="margin-left: 10px">
            <el-table
                ref="table"
                :data="tableData"
                :height="tableHeight"
                :border="true"
                :highlight-current-row="true"
                row-key="skuId"
                size="mini"
                style="width: 100%"
                @select="onSelect"
                @select-all="onSelectAll">
                <template>
                    <el-option
                        v-for="item in options"
                        :key="item.skuId"
                        :label="item.skuCode"
                        :value="item.skuName">
                    </el-option>
                    <el-table-column
                        :index="-1"
                        :show-overflow-tooltip="false"
                        type="selection"
                        width="55">
                    </el-table-column>
                    <template v-for="(column, index) in columnList">
                        <el-table-column
                            :key="index"
                            :show-overflow-tooltip="true"
                            v-bind="column">
                        </el-table-column>
                    </template>
                </template>
            </el-table>
        </template>
        <el-row>
            <div style="text-align: right">
                <el-pagination
                    :current-page="page.current"
                    :hide-on-single-page="false"
                    :page-size="page.size"
                    :small="true"
                    :total="page.total"
                    layout="total, prev, pager, next"
                    v-bind="page"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </div>
        </el-row>
    </el-select>
</template>

<script>

import {getSkuSelectByPage} from "@/api/wms/basics/sku";
import func from "@/util/func";

export default {
    name: "NodesSkuQuery",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array],
        // 是否禁用 默认为 false不禁用
        disabled: {type: Boolean, required: false, default: () => false},
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"},
    },
    watch: {
      selectVal(newVal/*, oldVal*/) {
          // 重置
          if (func.isEmpty(newVal)){
              this.val = [];
              this.$refs.table.clearSelection();
              this.selectRows = [];
              // this.tableData = [];
              // this.options = [];
          }
      }
    },
    data() {
        return {
            // 外层页面上显示的值
            val: [],
            columnList: [
                {
                    label: "物品编码",
                    prop: "skuCode",
                    width: "125"
                },
                {
                    label: "物品名称",
                    prop: "skuName",
                    width: "125"
                },
                {
                    label: "物品规格",
                    prop: "skuSpec",
                    width: "125"
                }
            ],
            // 表格数据
            tableData: [],
            tableHeight: 229,
            // 表格加载
            tableLoading: false,
            // 分页对象
            page: {
                total: 0,
                size: 5,
                current: 1,
                ascs: "",
                descs: "",
            },
            options: [],
            selectRows: [],
            key: '',
        }
    },
    methods: {
        // 单个框事件
        onSelect(selection, row) {
            let selectRows = this.selectRows;
            if (selectRows.includes(row)){
                this.selectRows.splice(selectRows.indexOf(row),1)
            }else {
                this.selectRows.push(row);
            }
            this.val = this.selectRows.map(x => x.skuCode)
            this.$emit('selectValChange', this.selectRows.map(x => x.skuId)); //回传选中的skuId
        },
        // 全选框事件
        onSelectAll(selection) {
            // 取消全选时，selection为[]
            if (func.isEmpty(selection)){
                this.selectRows = this.selectRows.filter(x =>{
                    return !this.tableData.includes(x);
                })
            }else {
                selection.forEach(x => {
                    if (!this.selectRows.includes(x)){
                        this.selectRows.push(x);
                    }
                })
            }
            this.val = this.selectRows.map(x => x.skuCode)
            this.$emit('selectValChange', this.selectRows.map(x => x.skuId));  //回传选中的skuId
        },
        // 清除单个tag事件
        onRemoveTag(val){
            let cancelSelectRows = this.selectRows.filter(x => x.skuCode === val);
            this.$refs.table.toggleRowSelection(cancelSelectRows[0], false)
            this.selectRows.splice(this.selectRows.indexOf(cancelSelectRows[0]),1)
            this.$emit('selectValChange', this.selectRows.map(x => x.skuId));   //回传skuId
        },
        // 清除全部tag事件
        onClear() {
            this.$refs.table.clearSelection();
            this.selectRows = [];
            this.$emit('selectValChange', []); //回传skuId，值为[]
        },
        remoteMethod(val) {
            if (func.isNotEmpty(val)){
                this.key = val
                this.getTableData();
            }
        },
        async getTableData() {
            if (func.isNotEmpty(this.key)){
                let {data: {data}} = await getSkuSelectByPage(this.page, this.key);
                this.tableData = data.records;
                this.page.total = data.total;
                this.options = this.tableData;

                //this.$nextTick()将回调延迟到下次 DOM 更新循环之后执行
                this.$nextTick(() => {
                    // 翻页之后，已勾选过(未取消勾选)的行，重新勾选上
                    let selectSkuIds = this.selectRows.map(x => x.skuId);
                    this.tableData.forEach(value => {
                        if (selectSkuIds.includes(value.skuId)){
                            this.$refs.table.toggleRowSelection(value, true)
                        }
                    })
                })
            }
        },
        handleCurrentChange(current) {
            this.page.current = current;
            this.getTableData();
        },
        // 下面两个方法暂时没用
        handleSizeChange(size) {
            this.page.size = size;
            this.getTableData();
        },
        handleSortChange(column) {
            let prop = column.prop;
            let order = column.order;
            if (order === "ascending") {
                this.page.ascs = prop;
                this.page.descs = "";
            } else if (order === "descending") {
                this.page.descs = prop;
                this.page.ascs = "";
            } else {
                this.page.ascs = "";
                this.page.descs = "";
            }
            this.getTableData();
        },
    },
}
</script>
<style>


</style>
