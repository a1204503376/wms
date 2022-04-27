<template>
    <div id="list">
        <nodes-master-page :configure="masterConfig" v-on="form.events">
            <template v-slot:searchFrom>
                <el-form-item label="物品编码">
                    <el-input v-model="form.params.skuCode" class="d-input"></el-input>
                </el-form-item>
                <el-form-item label="状态">
                    <nodes-asn-bill-state v-model="form.params.asnState"></nodes-asn-bill-state>
                </el-form-item>
                <el-form-item label="入库方式">
                    <nodes-in-storage-type v-model="form.params.inStorageType"
                                           style="width: 180px"></nodes-in-storage-type>
                </el-form-item>
            </template>
            <template v-slot:expandSearch>
                <el-row type="flex">
                    <el-col :span="24">
                        <el-form-item label="物品编码">
                            <el-input v-model="form.params.skuCode" class="d-input"></el-input>
                        </el-form-item>
                        <el-form-item label="物品编码">
                            <el-input v-model="form.params.skuCode" class="d-input"></el-input>
                        </el-form-item>
                        <el-form-item label="物品编码">
                            <el-input v-model="form.params.skuCode" class="d-input"></el-input>
                        </el-form-item>

                        <el-form-item label="物品编码">
                            <el-input v-model="form.params.skuCode" class="d-input"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="24">
                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="form.params.createDateRange"></nodes-date-range>
                        </el-form-item>

                        <el-form-item label="物品编码">
                            <el-input v-model="form.params.skuCode" class="d-input"></el-input>
                        </el-form-item>

                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="form.params.createDateRange"></nodes-date-range>
                        </el-form-item>
                        <el-form-item label="物品编码">
                            <el-input v-model="form.params.skuCode" class="d-input"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="24">
                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="form.params.createDateRange"></nodes-date-range>
                        </el-form-item>

                        <el-form-item label="物品编码">
                            <el-input v-model="form.params.skuCode" class="d-input"></el-input>
                        </el-form-item>

                        <el-form-item label="创建日期">
                            <nodes-date-range v-model="form.params.createDateRange"></nodes-date-range>
                        </el-form-item>

                        <el-form-item label="物品编码">
                            <el-input v-model="form.params.skuCode" class="d-input"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button size="mini" type="primary" @click="onRemove">删除</el-button>
                <el-button size="mini" type="primary">冻结</el-button>
            </template>
            <template v-slot:tableTool>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="刷新"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-refresh" size="mini" @click="onRefresh"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="显隐"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-s-operation" size="mini" @click="onColumnShowHide"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="本地导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-bottom" size="mini"></el-button>
                </el-tooltip>
                <el-tooltip
                    :enterable="false"
                    class="item"
                    content="服务端导出"
                    effect="dark"
                    placement="top"
                >
                    <el-button circle icon="el-icon-download" size="mini"></el-button>
                </el-tooltip>
            </template>
            <template v-slot:table>
                <el-table
                    ref="table"
                    :data="table.data"
                    :summary-method="getSummaries"
                    border
                    highlight-current-row
                    show-summary
                    size="mini"
                    @sort-change="onSortChange">
                    <el-table-column
                        fixed
                        type="selection"
                        width="50">
                    </el-table-column>
                    <el-table-column
                        fixed
                        sortable
                        type="index">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column,index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            v-bind="column">
                        </el-table-column>
                    </template>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :current-page="page.currentPage"
                    :page-size="page.pageSize"
                    :page-sizes="[20, 50, 100]"
                    :total="page.total"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </template>
        </nodes-master-page>
        <dialog-column
            v-bind="columnShowHide"
            @close="onColumnShowHide">
        </dialog-column>
    </div>
</template>

<script>


import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
import NodesInStorageType from "@/components/wms/select/NodesInStorageType";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import NodesSearchInput from "@/components/wms/input/NodesSearchInput";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";

export default {
    name: "list",
    components: {
        DialogColumn,
        NodesSearchInput,
        NodesInStorageType,
        NodesAsnBillState,
        NodesMasterPage,
        NodesDateRange
    },
    mixins: [listMixin],
    data() {
        return {
            masterConfig: {
                showExpandBtn: true,
                showPage: true
            },
            form: {
                params: {
                    skuCode: '',
                    asnState: [10, 30, 20],
                    inStorageType: [20],
                    createDateRange: ['2022-01-01', '2022-03-01']
                }
            },
            table: {
                columnList: [
                    {
                        prop: 'name',
                        label: '姓名',
                        width: 300,
                        sortable: 'custom'
                    },
                    {
                        prop: 'date',
                        label: '日期'
                    },
                    {
                        prop: 'wages',
                        label: '工资'
                    },
                    {
                        prop: 'date',
                        label: '日期'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                    {
                        prop: 'address',
                        label: '地址'
                    },
                ]
            },
        }
    },
    created() {
        this.getTableData();
    },
    methods: {
        getTableData() {
            // API调用:post(this.searchFrom)
            function getRandomInt(min, max) {
                min = Math.ceil(min);
                max = Math.floor(max);
                return Math.floor(Math.random() * (max - min)) + min; //不含最大值，含最小值
            }

            let fill = [];
            for (let i = 0; i < 101; i++) {
                // 模拟表格数据
                let item = {
                    date: `${getRandomInt(2018, 2022)}-${getRandomInt(1, 12)}-${getRandomInt(1, 28)}`,
                    name: "王小虎" + getRandomInt(1, 101),
                    wages: getRandomInt(3000, 15000),
                    address: `上海市普陀区金沙江路 ${getRandomInt(100, 2000)} 弄`
                };
                fill.push(item);
            }
            let length = fill.length;
            this.page.total = length;
            let offset = (this.page.currentPage - 1) * this.page.pageSize;
            let number = offset + this.page.pageSize;
            this.table.data = (number >= length)
                ? fill.slice(offset, length)
                : fill.slice(offset, number);
        },
        onSearch() {
            console.log(this.form.params);
        },
        onReset() {
            console.log('重置表单');
        },
        onRemove() {
            console.log('批量删除了');
        },
        getSummaries(param) {
            const {columns, data} = param;
            const sums = [];
            columns.forEach((column, index) => {
                if (index < 2) {
                    sums[index] = '';
                }
                if (index === 2) {
                    sums[index] = '合计';
                    return;
                }
                const values = data.map(item => Number(item[column.property]));
                if (!values.every(value => isNaN(value))) {
                    sums[index] = values.reduce((prev, curr) => {
                        const value = Number(curr);
                        if (!isNaN(value)) {
                            return prev + curr;
                        } else {
                            return prev;
                        }
                    }, 0);
                    sums[index] += ' 元';
                } else {
                    sums[index] = '';
                }
            });

            return sums;
        },
    }
}
</script>
