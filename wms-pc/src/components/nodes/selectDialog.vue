<template>
    <el-dialog :title="title"
               :visible.sync="visible"
               :close-on-click-modal="false"
               @open="dialogOpen"
               :before-close="dialogClose"
               v-dialogDrag="true"
               width="80%"
               top="3vh"
               class="dialogs"
               append-to-body>
        <div class="dialog-body">
            <!-- 搜索条件 -->
            <el-row :gutter="20">
                <el-form ref="searchForm" :model="search.form" label-width="100px" size="medium">
                    <el-col :span="8" v-for="(col, index) in search.column">
                        <el-form-item :label="col.label" :prop="col.prop">
                            <!-- 下拉列表框 -->
                            <template v-if="col.type === 'select'">
                                <el-select
                                        v-model="search.form[col.prop]"
                                        filterable
                                        clearable
                                        placeholder="请选择"
                                        style="width:100%;"
                                >
                                    <el-option
                                            v-for="item in col.dictData"
                                            :key="item[col.props.value]"
                                            :value="item[col.props.value]"
                                            :label="item[col.props.label]"
                                    ></el-option>
                                </el-select>
                            </template>
                            <!-- 默认：输入框input -->
                            <template v-else>
                                <el-input
                                        :placeholder="col.placeholder || '请输入'"
                                        v-model="search.form[col.prop]"
                                        clearable
                                ></el-input>
                            </template>
                        </el-form-item>
                    </el-col>
                    <el-col v-if="search.column && search.column.length > 0"
                            :span="8" style="line-height:36px;"
                            align="right">
                        <el-button type="primary" @click="onSearch" size="small">查 询</el-button>
                        <el-button type="danger" @click="onSearchReset" size="small">重 置</el-button>
                    </el-col>
                </el-form>
            </el-row>
            <!-- 数据表格 -->
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-table
                            class="table-style"
                            ref="table"
                            :data="tableData"
                            border
                            height="500px"
                            style="width: 100%"
                            show-overflow-tooltip
                            highlight-current-row
                            v-loading="loading"
                            @selection-change="selectionChange"
                            @row-click="rowClick"
                    >
                        <el-table-column
                                type="index"
                                width="55"
                                align="center"
                        >
                            <template slot="header">#</template>
                        </el-table-column>
                        <el-table-column v-if="multiple" type="selection"
                                         width="55" align="center">
                        </el-table-column>
                        <!-- 循环配置的列 -->
                        <template v-for="item in table.column">
                            <el-table-column
                                    v-if="!item.hide"
                                    :align="item.align"
                                    :prop="item.prop"
                                    :label="item.label"
                                    :key="item.prop"
                                    min-width="80"
                                    :show-overflow-tooltip="true"
                            ></el-table-column>
                        </template>
                    </el-table>
                </el-col>
            </el-row>
            <!-- 分页组件 -->
            <el-row :gutter="20">
                <el-col :span="24">
                    <el-pagination
                            background
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            @hideSingleOnPage="hideOnSinglePage"
                            :current-page="page.currentPage"
                            :page-sizes="[20, 50, 100]"
                            :page-size="page.pageSize"
                            layout="total, sizes, prev, pager, next, jumper"
                            :total="page.total">
                    </el-pagination>
                </el-col>
            </el-row>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary"
                       @click="confirm"
                       :loading="loading">确 认
            </el-button>
            <el-button @click="dialogClose">关 闭</el-button>
        </div>
    </el-dialog>
</template>

<script>
    import request from '@/router/axios';

    export default {
        name: "selectDialog",
        props: {
            // 组件显隐
            visible: {type: Boolean, default: false},
            // 组件标题
            title: {type: String, default: '请选择'},
            // 是否允许多选
            multiple: {type: Boolean, default: false},
            // 获取数据接口
            queryUrl: {type: String, default: ''},
            // 保存数据接口
            saveUrl: {type: String, default: ''},
            // 保存的对象模型
            saveObj: {type: [Object, Array], default: null},
            // 调用接口的参数
            params: {type: Object, default: {}},
            // 表格配置
            table: {
                type: Object, default: function () {
                    return {
                        column: []
                    };
                }
            }
        },
        data() {
            return {
                loading: false,
                selectionList: [],
                tableData: [],
                page: {
                    total: 0,
                    pageSize: 20,
                    currentPage: 1,
                    ascs: "",   //正序
                    descs: "",  //倒序
                },
                callback: {
                    visible: false,
                    success: false,
                    data: null,
                    dataList: []
                },
                search: {
                    form: {},
                    column: []
                }
            }
        },
        mounted() {
            this.search.column = this.table.column.filter(u => {
                return u.search;
            });
        },
        methods: {
            dialogOpen() {
                if (this.search.column) {
                    for (let i = 0; i < this.search.column.length; i++) {
                        let item = this.search.column[i];
                        if (!item.dictUrl) {
                            continue;
                        }
                        request({
                            url: item.dictUrl,
                            method: 'get'
                        }).then(res => {
                            this.$set(item, 'dictData', res.data.data);
                        });
                    }
                }
                this.onLoad(this.page, {});
            },
            dialogClose() {
                this.callback.visible = false;
                this.callback.success = false;
                this.callback.data = null;
                this.callback.dataList = [];
                this.$emit('callback', this.callback);
            },
            selectionChange(val) {
                this.selectionList = val;
            },
            selectionClear() {
                this.selectionList = [];
                if (this.$refs.table) {
                    this.$refs.table.clearSelection();
                }
            },
            handleSizeChange(val) {
                this.page.pageSize = val;
                this.onLoad(this.page, this.searchPanel ? this.searchPanel.data : {});
            },
            handleCurrentChange(val) {
                this.page.currentPage = val;
                this.onLoad(this.page, this.searchPanel ? this.searchPanel.data : {});
            },
            hideOnSinglePage() {
                return this.page && this.page.total <= this.page.pageSize;
            },
            onLoad(page, params = {}) {
                if (!this.queryUrl || this.queryUrl.length === 0) {
                    return;
                }
                this.loading = true;
                if (this.params) {
                    for (let item in this.params) {
                        this.$set(params, item, this.params[item]);
                    }
                }
                request({
                    url: this.queryUrl,
                    method: 'get',
                    params: {
                        ...params,
                        current: page.currentPage,
                        size: page.pageSize,
                    }
                }).then(res => {
                    const data = res.data.data;
                    this.page.total = data.total;
                    this.tableData = data.records;
                    this.loading = false;
                    this.selectionClear();
                }).finally(() => {
                    this.loading = false;
                });
            },
            confirm() {
                if (!this.selectionList || this.selectionList.length === 0) {
                    return;
                }
                let data;
                if (this.multiple) {
                    // 多选：调用接口传数组
                    data = [];
                    if (this.saveObj) {
                        for (let i = 0; i < this.selectionList.length; i++) {
                            let obj = Object.assign({}, this.saveObj);
                            let item = this.selectionList[i];

                            for (let prop in obj) {
                                if (!obj[prop]) {
                                    obj[prop] = item[prop];
                                }
                            }

                            data.push(obj);
                        }
                    }
                } else {
                    // 单选：调用接口传对象
                    data = {};
                    if (this.saveObj) {
                        for (let i = 0; i < this.selectionList.length; i++) {
                            let obj = Object.assign({}, this.saveObj);
                            let item = this.selectionList[i];

                            for (let prop in obj) {
                                if (!obj[prop]) {
                                    obj[prop] = item[prop];
                                }
                            }

                            data = Object.assign({}, obj);
                        }
                    }
                }
                let requestCount = 0;
                if (this.saveUrl && this.saveUrl.length > 0) {
                    requestCount++;
                    this.loading = true;
                    // 调用保存接口
                    request({
                        url: this.saveUrl,
                        method: 'post',
                        data: data
                    }).then(res => {

                    }).finally(() => {
                        this.loading = false;
                        requestCount--;
                    });
                }
                let self = this;
                let interval = setInterval(function () {
                    if (requestCount === 0) {
                        self.callback.visible = false;
                        self.callback.success = true;
                        self.callback.data = Object.assign({}, self.selectionList[0]);
                        self.callback.dataList = Object.assign([], self.selectionList);
                        self.$emit('callback', self.callback);
                        clearInterval(interval);
                    }
                });
            },
            rowClick(row, column, cell, event) {
                this.$refs.table.toggleRowSelection(row);
            },
            onSearch() {
                this.onLoad(this.page, this.search.form);
            },
            onSearchReset() {
                this.$refs.searchForm.resetFields();
            }
        }
    }
</script>

<style scoped>
    .el-form-item {
        margin-bottom: 0px;
    }
</style>
