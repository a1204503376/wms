<template>
    <el-popover
            placement="bottom-start"
            class="nodes-table-select"
            v-model="popoverVisible"
            :disabled="disabled"
            :width="width"
    >
        <el-row :gutter="20" class="select-table">
            <el-col :span="24">
                <el-input
                        ref="keywords"
                        v-model="keywords"
                        placeholder="请输入关键字"
                        :size="size"
                        :clearable="true"
                        @input="keywordsChanged"
                        style="margin-bottom: 10px"
                ></el-input>
            </el-col>
        </el-row>
        <el-row :gutter="20">
            <el-col :span="24">
                <el-table
                        :data="props.tableData"
                        style="cursor: pointer"
                        :size="size"
                        width="300"
                        border
                        v-loading="tableLoading"
                        @row-click="tableRowClick"
                >
                    <el-table-column
                            v-for="(item, index) in option.column"
                            :key="index"
                            :label="item.label"
                            :prop="item.prop"
                            :width="item.width"
                            show-overflow-tooltip
                            :column-key="item.value"
                    ></el-table-column>
                </el-table>
                <div style="text-align: right">
                    <el-pagination
                            @size-change="handleSizeChange"
                            @current-change="handleCurrentChange"
                            :current-page="page.currentPage"
                            :page-sizes="[10, 20, 30, 50]"
                            :page-size="page.pageSize"
                            :hide-on-single-page="this.page.total <= this.page.pageSize"
                            :small="true"
                            layout="total, prev, pager, next"
                            :total="page.total"
                    >
                    </el-pagination>
                </div>
            </el-col>
        </el-row>
        <el-input
                ref="input"
                type="text"
                v-model="text"
                :placeholder="placeholder"
                :suffix-icon="suffixIcon"
                slot="reference"
                class="table-select-input"
                :clearable="clearable"
                :size="size"
                readonly
                :disabled="disabled"
                v-on:click.native="onClick"
                v-on:mouseleave.native="onMouseleave"
                v-on:mouseover.native="onMouseover"
        ></el-input>
    </el-popover>
</template>

<script>
    export default {
        name: "selectTable",
        props: {
            // 是否可清空
            clearable: {type: Boolean, default: true},
            // 输入框占位文本
            placeholder: {type: String, default: "请选择..."},
            // 禁用
            disabled: {type: [Boolean, Function], default: false},
            // 尺寸
            size: {type: String, default: null},
            // 表格加载数据时显示动效
            tableLoading: {type: Boolean, default: false},
            // 绑定值
            value: {type: [Number, String], default: undefined},
            // 配置
            option: {
                default: function () {
                    return {
                        // 表格列
                        column: {
                            type: Array,
                            default: function () {
                                return [];
                            },
                        },
                    };
                },

            },
            // 绑定的值
            prop: {
                type: Object,
                default: function () {
                    return {
                        label: "",
                        value: "",
                    };
                },
            },
            // 数据
            data: {
                type: Array,
                default: function () {
                    return [];
                },
            },
        },
        data() {
            return {
                // 对外参数
                props: {
                    tableData: [],
                    value: undefined
                },
                popoverVisible: false,
                width: 500,
                page: {
                    pageSize: 5,
                    currentPage: 1,
                    total: 0,
                },
                // 检索关键字
                keywords: "",
                text: "",
                suffixIcon: "el-icon-arrow-down",
            };
        },
        watch: {
            popoverVisible: function (newValue, oldValue) {
                if (newValue !== oldValue && newValue) {
                    this.onLoad();
                }
                if(newValue) {
                    this.$emit('open');
                } else {
                    this.$emit('close');
                }
            },
            disabled: {
                handler: function () {
                    this.disabled = this.onDisabled();
                },
            },
            data: {
                handler: function (newValue) {
                    let data = Object.assign([], newValue);
                    this.$set(this.props, "tableData", data);
                    this.loadData();
                    let index = this.data.findIndex((row) => {
                        return row[this.prop.value] === this.value;
                    });
                    if (index > -1) {
                        let row = this.data[index];
                        this.text = row[this.prop.label];
                        this.$emit("change", this.value, row);
                    }
                },
                deep: true,
                immediate: true,
            },
            value: {
                handler: function (nv, ov) {
                    if (JSON.stringify(nv) == JSON.stringify(ov)) {
                        return;
                    }
                    this.props.value = this.value;
                    let index = this.data.findIndex((row) => {
                        return row[this.prop.value] === this.value;
                    });
                    if (index > -1) {
                        let row = this.data[index];
                        this.text = row[this.prop.label];
                    } else {
                        this.text = '';
                    }
                }
            }
        },
        model: {
            prop: "value",
            event: "change",
        },
        mounted() {
            this.disabled = this.onDisabled();
            // 监控窗体的大小变化
            let self = this;
            window.onresize = function () {
                // 改变弹框的宽度
                self.width = self.$refs.input.$refs.input.clientwidth - 25;
            };
        },
        methods: {
            // 表格的点击事件
            tableRowClick(row /*, column, event*/) {
                if (row[this.prop.value] !== this.value) {
                    this.props.value = row[this.prop.value];
                    this.$emit("change", this.props.value, row);
                }
                this.popoverVisible = false;
            },
            handleSizeChange(val) {
                this.page.pageSize = val;
                this.keywordsChanged();
            },
            handleCurrentChange(val) {
                this.page.currentPage = val;
                this.keywordsChanged();
            },
            // 处理分页数据
            loadData(searchData) {
                let data = searchData ? searchData : this.data;
                this.page.total = data.length;
                if (this.page.pageSize >= data.length) {
                    this.props.tableData = data;
                } else {
                    let start = this.page.pageSize * (this.page.currentPage - 1);
                    this.props.tableData = data.slice(start, start + this.page.pageSize);
                }
            },
            // 显示的时候触发，用于刷新数据
            onLoad() {
                this.$emit("on-load");
            },
            // 关键字检索
            keywordsChanged() {
                if (!this.keywords || this.keywords.length === 0) {
                    this.loadData();
                } else {
                    if (!this.option || !this.option.column || this.option.column.length === 0) {
                        return;
                    }
                    let searchData = this.data.filter((row) => {
                        let result = false;
                        for (let property in row) {
                            if (this.option.column.findIndex((col) => col.prop === property) < 0) {
                                continue;
                            }
                            if (row[property] != undefined &&(row[property] + "").indexOf(this.keywords) > -1) {
                                result = true;
                                break;
                            }
                        }
                        return result;
                    });
                    this.loadData(searchData);
                }
            },
            onMouseover() {
                if (!this.disabled && this.clearable && this.value && this.value.length > 0) {
                    this.suffixIcon = "el-icon-circle-close";
                }
            },
            onMouseleave() {
                this.suffixIcon = "el-icon-arrow-down";
            },
            onClick(event) {
                if (this.onDisabled()) {
                    return;
                }
                //event.toElement这个只兼容谷歌
                if (event.target.offsetParent.className === "el-input__suffix"
                    && this.suffixIcon === "el-icon-circle-close") {
                    if (this.clearable) {
                        this.text = "";
                        this.$emit("change", undefined, undefined);
                    } else {
                        this.popoverVisible = false;
                    }
                }
            },
            onDisabled() {
                if (this.disabled !== undefined) {
                    if (typeof this.disabled === "function") {
                        return this.disabled();
                    } else {
                        return this.disabled;
                    }
                } else {
                    return false;
                }
            },
        },
    };
</script>

<style lang="scss">
    .table-select-input .el-input__inner,
    .table-select-input .el-input__suffix-inner {
        cursor: pointer;
    }
    .select-table .el-col{
        margin-bottom:0px !important;
    }
    .select-table .el-input {
        margin-bottom:0px !important;
    }
</style>
