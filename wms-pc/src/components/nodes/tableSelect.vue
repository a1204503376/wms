<template>
    <el-popover
            placement="bottom-start"
            class="nodes-table-select"
            v-model="popoverVisible"
            :disabled="disabled"

    >
        <!--    @show="popoverShow"-->
        <!--    trigger="click"-->
        <!--    <el-popover-->
        <!--      placement="top"-->
        <!--      width="300"-->
        <!--      v-model="filterVisible">-->
        <!--      <el-form ref="filterObj" :model="filterObj" label-position="top">-->
        <!--        <el-row :gutter="20">-->
        <!--          <el-col span="6" style="line-height:24px;">-->
        <!--            <el-form-item label="列名" prop="filterObj.column.value">{{filterObj.column.label}}-->
        <!--            </el-form-item>-->
        <!--          </el-col>-->
        <!--          <el-col span="8">-->
        <!--            <el-form-item label="条件" prop="filterObj.condition">-->
        <!--              <el-select v-model="filterObj.condition" placeholder="请选择条件" size="mini" style="width:100%;">-->
        <!--                <el-option label="近似于" value="近似于"></el-option>-->
        <!--                <el-option label="等于" value="等于"></el-option>-->
        <!--                <el-option label="不等于" value="不等于"></el-option>-->
        <!--              </el-select>-->
        <!--            </el-form-item>-->
        <!--          </el-col>-->
        <!--          <el-col span="10">-->
        <!--            <el-form-item label="值" prop="filterObj.value">-->
        <!--              <el-input v-model="filterObj.value" style="width:100%;" placeholder="请输入值" size="mini"></el-input>-->
        <!--            </el-form-item>-->
        <!--          </el-col>-->
        <!--        </el-row>-->
        <!--      </el-form>-->
        <!--      <div style="text-align: right; margin: 0">-->
        <!--        <el-button size="mini" type="text" @click="filterCancel">取消</el-button>-->
        <!--        <el-button type="primary" size="mini" @click="filterSuccess">确定</el-button>-->
        <!--      </div>-->
        <!--    </el-popover>-->
        <el-table :data="data" style="cursor:pointer;" size="mini"
                  @row-click="tableRowClick"
                  @filter-change="filterChange">
            <el-table-column v-for="(item, index) in columns"
                             v-loading="loading"
                             :key="index"
                             :label="item.label"
                             :prop="item.value"
                             :width="item.width"
                             show-overflow-tooltip
                             :column-key="item.value"
            ></el-table-column>
        </el-table>
        <div style="text-align:right;">
            <el-pagination
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                    :current-page="page.currentPage"
                    :page-sizes="[10, 20, 30, 50]"
                    :page-size="10"
                    :hide-on-single-page="this.page.total <= this.page.pageSize"
                    :small=true
                    layout="total, prev, pager, next"
                    :total="page.total">
            </el-pagination>
        </div>
        <el-input
                ref="input"
                type="text"
                :placeholder="this.placeholder"
                suffix-icon="el-icon-arrow-down"
                slot="reference"
                class="table-select-input"
                v-model="text"
                :clearable="this.clearable"
                :disabled="disabled"
                @clear="clearHandle"
                @click="popoverShow"
                readonly></el-input>
    </el-popover>
</template>

<script>
    import request from '@/router/axios';

    export default {
        name: "tableSelect",
        model: {
            prop: 'value',
            event: 'change'
        },
        props: {
            // 可清空选项
            clearable: {type: Boolean, default: true},
            placeholder: {type: String, default: '请选择...'},
            // 绑定的值
            prop: {
                type: Object, default: {
                    label: '',
                    value: ''
                }
            },
            // 表格的列
            columns: {type: Array, default: []},
            // 获取数据接口地址
            dicUrl: {type: String, default: ''},
            showText: {type: String, default: ''},
            disabled: {type: Boolean, default: true}
        },
        data() {
            return {
                // 文本框显示的文本
                text: '',
                value: '',
                // 表格数据
                data: [],
                page: {
                    pageSize: 10,
                    currentPage: 1,
                    total: 0
                },
                popoverVisible: false,
                // filterVisible: false,
                // filterColumn: '',
                // filterObj: {
                //     column: {},
                //     condition: '',
                //     value: ''
                // },
                loading: false
            }
        },
        mounted() {
            this.text = this.showText;
            this.onLoad(this.page, {});
        },
        watch: {
            popoverVisible(val) {
                if (!val) {
                    this.filterVisible = false;
                }
            },
            showText(val){
                if (this.text !== val){
                    this.text = val;
                }
            },
            dicUrl(val) {
                if (this.dicUrl !== val) {
                    this.dicUrl = val;
                    this.onLoad(this.page, {});
                }
            }
        },
        methods: {
            // 初始化值
            onLoad(page, params = {}) {
                this.loading = true;
                const current = page.currentPage;
                const size = page.pageSize;
                request({
                    url: this.dicUrl,
                    method: 'get',
                    params: {
                        ...params,
                        current,
                        size,
                    }
                }).then(res => {
                    const data = res.data.data;
                    this.page.total = data.total;
                    this.data = data.records;
                    this.loading = false;
                });
            },
            change(val) {
                console.log(val);
            },
            handleSizeChange(val) {
                this.page.pageSize = val;
                this.onLoad(this.page, {});
            },
            handleCurrentChange(val) {
                this.page.currentPage = val;
                this.onLoad(this.page, {});
            },
            popoverShow() {
                if (this.disabled) {
                    return;
                }
                this.popoverVisible = true;
            },
            // 切换选项
            tableRowClick(row/*, column, event*/) {
                if (row[this.prop.value] !== this.value) {
                    this.value = row[this.prop.value];
                    this.text = row[this.prop.label];
                    this.$emit('change', this.value, this.text);
                }
                this.popoverVisible = false;
            },
            // 清除选中
            clearHandle() {
                this.value = '';
                this.text = '';
                this.$emit('change', null, '');
            },
            filterChange(filters) {
                // let source = new Map();
                // for (let i = 0; i < this.columns.length; i++) {
                //   let columnName = this.columns[i].value;
                //   source[columnName] = filters[columnName] ? filters[columnName][0] : null;
                //   if (source[columnName] === 'custom') {
                //     // 显示自定义框
                //     this.filterObj.column = this.columns[i];
                //     this.filterVisible = true;
                //     return;
                //   }
                // }
                // this.onLoad(this.page, Object.assign({}, source));
            },
            getFilters(item) {
                let dataSource = [];
                // dataSource.push({text: '自定义', value: 'custom'});
                // if (this.data) {
                //   for (let i = 0; i < this.data.length; i++) {
                //     let text = this.data[i][item.value];
                //     let obj = {text: text, value: text};
                //     let index = dataSource.findIndex(u => u.value === obj.value);
                //     if (index < 0) {
                //       dataSource.push(obj);
                //     }
                //   }
                // }
                return dataSource;
            },
            // filterMethod(value, row, column) {
            //   const property = column['property'];
            //   return row[property] === value;
            // },
            hideOnSinglePage() {
                return this.page.total <= this.page.pageSize;
            },
            filterCancel() {
                this.filterVisible = false;
            },
            filterSuccess() {
                // let source = new Map();
                // source[this.filterObj.column.value] = this.filterObj.value;
                // this.filterVisible = false;
                // this.onLoad(this.page, Object.assign({}, source));
            }
        }
    }
</script>

<style scoped>

    .el-scrollbar .el-scrollbar__view .el-select-dropdown__item {
        height: auto;
        max-height: 500px;
        padding: 0;
        overflow: hidden;
        overflow-y: auto;
    }

    .el-select-dropdown__item.selected {
        font-weight: normal;
    }

    ul li >>> .el-tree .el-tree-node__content {
        height: auto;
        padding: 0 20px;
    }

    .el-tree-node__label {
        font-weight: normal;
    }

    .el-tree >>> .is-current .el-tree-node__label {
        color: #409EFF;
        font-weight: 700;
    }

    .el-tree >>> .is-current .el-tree-node__children .el-tree-node__label {
        color: #606266;
        font-weight: normal;
    }

    .table-select-input >>> .el-input__inner {
        cursor: pointer !important;
    }
</style>
