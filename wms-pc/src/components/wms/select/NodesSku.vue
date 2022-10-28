<template>
    <el-select
        ref="select"
        v-model="val"
        :clearable="true"
        :remote-method="remoteMethod"
        :filterable="true"
        :size="size"
        :collapse-tags="true"
        placeholder="请输入关键词"
        popper-class="myPopper"
        :remote="true">
        <template style="margin-left: 10px">
            <el-table
                ref="table"
                :data="tableData"
                :height="tableHeight"
                :border="true"
                :highlight-current-row="true"
                @row-click="onRowClick"
                size="mini"
                style="width: 100%">
                <template>
                    <el-option
                        v-for="item in options"
                        :key="item.skuId"
                        :label="item.skuCode"
                        :value="item.skuName">
                    </el-option>
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
        selectVal: [Object],
        // 是否禁用 默认为 false不禁用
        disabled: {type: Boolean, required: false, default: () => false},
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"},
    },
    data() {
        return {
            // 外层页面上显示的值
            val: '',
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
            key: '',
        }
    },
    watch: {
        // 1、监听外部给的默认值。 或者 2、手动选择后，赋选中(下面回传的sku对象)的值。
        selectVal(selectVal){
            this.val = selectVal.skuCode;
        }
    },
    methods: {
        onRowClick(row) {
            this.$emit('selectValChange', row); // 回传sku对象
            this.$refs.select.blur(); //使组件去焦点，收起下拉选项
        },
        async remoteMethod(val) {
            if (func.isNotEmpty(val)){
                this.key = val
                await this.getTableData();

                // 根据物品编码搜索时，如果搜索结果只有一个且和当前关键字一致，则直接回传
                if (this.tableData.length === 1 && this.tableData[0].skuCode === val){
                    this.$emit('selectValChange', this.tableData[0]); // 回传sku对象
                    this.$refs.select.blur(); //使组件去焦点，收起下拉选项
                }
            }
        },
        async getTableData() {
            if (func.isNotEmpty(this.key)){
                let {data: {data}} = await getSkuSelectByPage(this.page, this.key)
                    this.tableData =  data.records;
                    this.page.total = data.total;
                    this.options = this.tableData;
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
