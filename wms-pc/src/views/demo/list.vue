<template>
    <div id="list">
        <nodes-master-page v-on="form.events">
            <template v-slot:searchFrom>
                <el-row type="flex" class="search-elRow">
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <el-input
                                v-model.trim="form.params.skuCode"
                                class="search-input">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="a.b" label-width="90px">
                            <el-input
                                v-model="form.params.a.b" class="search-input">
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="ASN单状态" label-width="90px">
                            <nodes-asn-bill-state
                                v-model="form.params.asnState"
                                class="search-input">
                            </nodes-asn-bill-state>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="入库方式" label-width="90px">
                            <nodes-in-store-mode
                                v-model="form.params.inStorageType"
                                class="search-input">
                            </nodes-in-store-mode>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex" class="search-elRow">
                    <el-col :span="6">
                        <el-form-item label="状态" label-width="90px">
                            <nodes-lpn-type-state
                                v-model="form.params.lpnTypeState"
                                class="search-input">
                            </nodes-lpn-type-state>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <el-input v-model="form.params.skuCode" class="search-input"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <el-input v-model="form.params.skuCode" class="search-input"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <el-input v-model="form.params.skuCode" class="search-input"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row type="flex">
                    <el-col :span="6">
                        <el-form-item label="创建日期" label-width="90px">
                            <nodes-date-range v-model="form.params.createDateRange"></nodes-date-range>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <el-input v-model="form.params.skuCode" class="search-input"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="创建日期" label-width="90px">
                            <nodes-date-range v-model="form.params.createDateRange"></nodes-date-range>
                        </el-form-item>
                    </el-col>
                    <el-col :span="6">
                        <el-form-item label="物品编码" label-width="90px">
                            <el-input v-model="form.params.skuCode" class="search-input"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </template>
            <template v-slot:batchBtn>
                <el-button size="mini" type="primary" @click="onAdd">新增</el-button>
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
                    :height="table.height"
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
                        type="index"
                        width="50">
                        <template slot="header">
                            #
                        </template>
                    </el-table-column>
                    <template v-for="(column,index) in table.columnList">
                        <el-table-column
                            v-if="!column.hide"
                            :key="index"
                            show-overflow-tooltip
                            width="150"
                            v-bind="column">
                        </el-table-column>
                    </template>
                    <el-table-column
                        fixed="right"
                        label="操作"
                        width="120">
                        <template v-slot="{row}">
                            <el-button v-if="permissionObj.add" size="mini" type="text" @click="onEdit(row)">编辑</el-button>
                            <el-button size="mini" type="text" @click="onViewDetails(row)">查看详情</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </template>
            <template v-slot:page>
                <el-pagination
                    :page-sizes="[20, 50, 100]"
                    background
                    layout="total, sizes, prev, pager, next, jumper"
                    v-bind="page"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </template>
        </nodes-master-page>
        <div v-if="columnShowHide.visible">
            <dialog-column
                v-bind="columnShowHide"
                @close="onColumnShowHide">
            </dialog-column>
        </div>
    </div>
</template>

<script>

import NodesMasterPage from "@/components/wms/general/NodesMasterPage";
import NodesAsnBillState from "@/components/wms/select/NodesAsnBillState";
import NodesLpnTypeState from "@/components/wms/select/NodesLpnTypeState";
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesDateRange from "@/components/wms/general/NodesDateRange";
import DialogColumn from "@/components/element-ui/crud/dialog-column";
import {listMixin} from "@/mixins/list";

export default {
    name: "list",
    components: {
        DialogColumn,
        NodesInStoreMode,
        NodesAsnBillState,
        NodesMasterPage,
        NodesDateRange,
        NodesLpnTypeState
    },
    mixins: [listMixin],
    data() {
        return {
            form: {
                params: {
                    skuCode: '',
                    asnState: [10, 30, 20],
                    inStorageType: [20],
                    lpnTypeState: [],
                    createDateRange: ['2022-01-01', '2022-03-01'],
                    a: {
                        b: 123
                    }
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
                        label: '工资',
                        // left/center/right
                        align: 'right'
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
                ]
            },
        }
    },
    created() {
        this.getTableData();
    },
    computed: {
        permissionObj() {
            return {
                add: this.vaildData(this.permission.demo_add, false),
            }
        }
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
                    id: i + 1,
                    date: `${getRandomInt(2018, 2022)}-${getRandomInt(1, 12)}-${getRandomInt(1, 28)}`,
                    name: "王小虎" + getRandomInt(1, 101),
                    wages: getRandomInt(3000, 15000),
                    address: `上海市普陀区金沙江路 ${getRandomInt(100, 2000)} 弄`
                };
                fill.push(item);
            }
            let length = fill.length;
            this.page.total = length;
            let offset = (this.page.current - 1) * this.page.size;
            let number = offset + this.page.size;
            this.table.data = (number >= length)
                ? fill.slice(offset, length)
                : fill.slice(offset, number);

            this.handleRefreshTable();
        },
        onSearch() {
            console.log(this.form.params);
        },
        onAdd() {
            this.$router.push({
                name: 'demoEdit',
                params: {
                    id: '0'
                }
            });
        },
        onEdit(row) {
            this.$router.push({
                name: 'demoEdit',
                params: {
                    id: row.id.toString()
                }
            });
        },
        onViewDetails(row) {
            this.$router.push({
                name: 'DEMO查看详情',
                params: {
                    id: row.id.toString()
                }
            });
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
