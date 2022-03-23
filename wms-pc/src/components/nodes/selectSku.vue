<template>
    <div>
        <el-input
            ref="input"
            readonly
            :placeholder="placeholder"
            v-model="showText"
            size="medium"
            class="select-input"
            :suffix-icon="suffixIcon"
            :disabled="disabled"
            :clearable="clearable"
            v-on:click.native="onClick"
            v-on:mouseleave.native="onMouseleave"
            v-on:mouseover.native="onMouseover"
        ></el-input>
        <el-dialog
            :title="title"
            :visible.sync="visible"
            :close-on-click-modal="false"
            @open="dialogOpen"
            :before-close="dialogClose"
            v-dialogDrag="true"
            width="80%"
            top="3vh"
            id="my_dialogs"
            append-to-body
        >
            <el-form size="medium" label-position="right" label-width="80px">
                <div class="dialog__body">
                    <el-row :gutter="20">
                        <el-col :span="5">
                            <el-form-item label="物品分类" prop="skuTypeId">
                                <treeSelect
                                    placeholder="请选择物品类型"
                                    v-model="searchData.skuTypeId"
                                    :props="selectTreeOptions.props"
                                    :options="skuTypeSource"
                                    :value="selectTreeOptions.valueId"
                                    :clearable="selectTreeOptions.isClearable"
                                    :accordion="selectTreeOptions.isAccordion"
                                    style="width: 100%"
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="物品编码" prop="skuCode">
                                <el-input
                                    type="text"
                                    v-model="searchData.skuCode"
                                    placeholder="请输入物品编码"
                                />
                            </el-form-item>
                        </el-col>

                        <el-col :span="5">
                            <el-form-item label="物品名称" prop="skuName">
                                <el-input
                                    type="text"
                                    style="width: 100%"
                                    v-model="searchData.skuName"
                                    placeholder="请输入物品名称"
                                />
                            </el-form-item>
                        </el-col>
                        <el-col :span="5">
                            <el-form-item label="SN管理" prop="isSn">
                                <el-select
                                    type="text"
                                    style="width: 100%"
                                    v-model="searchData.isSn"
                                    placeholder="请选择"
                                    clearable=true
                                >
                                    <el-option label="否" value="0"></el-option>
                                    <el-option label="是" value="1"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="2">
                            <el-button type="primary" @click="onSearch" size="small"
                            >查 询
                            </el-button
                            >
                        </el-col>
                        <el-col :span="2">
                            <el-button type="danger" @click="searchReset" size="small"
                            >重 置
                            </el-button
                            >
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="24">
                            <el-table
                                :data="data"
                                v-loading="loading"
                                border
                                @current-change="handleCurrentChange"
                                @cell-dblclick="dblhandleCurrentChange"
                                highlight-current-row
                                ref="singleTable"
                                :header-cell-style="{
                  'background-color': '#fafafa',
                }"
                                height="400px"
                                max-height="400px"
                                overflow="auto"
                            >
                                <el-table-column
                                    property="skuCode"
                                    label="物品编码"
                                    align="center"
                                    width="150"
                                    show-overflow-tooltip
                                ></el-table-column>
                                <el-table-column
                                    property="skuName"
                                    label="物品名称"
                                    width="150"
                                    align="center"
                                    show-overflow-tooltip
                                ></el-table-column>
                                <el-table-column
                                    property="skuNameS"
                                    label="物品名称简称"
                                    width="110"
                                    align="center"
                                    show-overflow-tooltip
                                ></el-table-column>
                                <el-table-column
                                    property="typeName"
                                    label="物品分类"
                                    align="center"
                                    width="160"
                                    show-overflow-tooltip
                                ></el-table-column>
                                <el-table-column
                                    property="wspName"
                                    label="包装"
                                    align="center"
                                    show-overflow-tooltip
                                ></el-table-column>
                                <el-table-column
                                    property="isSnDesc"
                                    label="SN管理"
                                    width="90"
                                    align="center"
                                ></el-table-column>
                            </el-table>
                        </el-col>
                    </el-row>
                </div>
            </el-form>
            <div style="text-align: left; padding-top: 10px">
                <el-pagination
                    background
                    @size-change="sizeChange"
                    @current-change="currentChange"
                    :current-page="page.currentPage"
                    :page-sizes="[20, 50, 100]"
                    :page-size="page.pageSize"
                    layout="total, sizes, prev, pager, next, jumper"
                    :total="page.total"
                ></el-pagination>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="dialogConfirm">确 定</el-button>
                <el-button @click="dialogClose">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import {getPage} from "@/api/wms/basedata/sku";
import {getTree as getSkuTypeTree} from "@/api/wms/basedata/skutype";
import treeSelect from "./treeSelect";

export default {
    name: "selectSku",
    components: {treeSelect},
    props: {
        value: {type: String, default: ""},
        text: {type: String, default: ""},
        disabled: {type: Boolean, default: false},
        placeholder: {type: String, default: "请选择"},
        clearable: {type: Boolean, default: false},
        search: {
            type: Object,
            default: function () {
                return {
                    woId: "",
                    whId: "",
                    skuTypeId: "",
                    skuTypeIdName: "",
                    skuTypePfep: "",
                    skuCode: "",
                    skuName: "",
                    stockIds:""
                };
            },
        },
        beforeOpen: {
            type: Function, default: function () {

            }
        }
    },
    model: {
        prop: "value",
        event: "change",
    },
    data() {
        return {
            title: "请选择",
            showText: "",
            loading: false,
            data: [],
            page: {
                pageSize: 20,
                currentPage: 1,
                total: 0,
            },
            suffixIcon: "el-icon-goods",
            dic: [],
            currentRow: null, //选中行
            visible: false,
            searchData: {},
            skuTypeSource: [],
            selectTreeOptions: {
                isClearable: true, // 可清空（可选）
                isAccordion: true, // 可收起（可选）
                valueId: 20, // 初始ID（可选）
                props: {
                    // 配置项（必选）
                    value: "id",
                    label: "title",
                    children: "children",
                    // disabled:true
                },
            },
        };
    },
    mounted() {
        this.showText = this.text;
        this.searchData = Object.assign({}, this.search);
    },
    updated() {
        if (this.$refs.singleTable && this.gridData && this.gridData.length > 0) {
            this.$refs.singleTable.setCurrentRow(this.gridData[0]);
        }
    },
    watch: {
        value: {
            handler: function (newValue, oldValue) {
                if (newValue !== oldValue && newValue) {
                    this.value = newValue;
                    this.$emit("change", newValue);
                }
            },
        },
        text(val) {
            this.showText = val;
        },
        search: {
            handler: function (newValue, oldValue) {
                console.log(newValue)
            },
            immediate: true,
            deep: true
        }
    },
    methods: {
        onClick(event) {
            if (!this.disabled) {
                //event.toElement这个只兼容谷歌
                if (
                    event.target.offsetParent.className === "el-input__suffix" &&
                    this.clearable
                ) {
                    this.showText = "";
                    this.$emit("change", "");
                } else {
                    this.visible = true;
                }
            }
        },
        onMouseover() {
            if (this.clearable && this.value) {
                this.suffixIcon = "el-icon-close";
            }
        },
        onMouseleave() {
            this.suffixIcon = "el-icon-goods";
        },
        dialogOpen() {
            if (this.beforeOpen) {
                this.beforeOpen();
            }
            let woId = parseInt(this.search.woId);
            if (isNaN(woId)) {
                this.search.woId = null;
            }
            this.searchData = Object.assign({}, this.search);
            // 绑定物品分类
            getSkuTypeTree({woId: this.search.woId}).then((res) => {
                this.skuTypeSource = res.data.data;
            });
            this.onLoad(this.page, Object.assign({}, this.search));
        },
        dialogConfirm() {
            this.visible = false;
            if (this.currentRow) {
                let obj = Object.assign({}, this.currentRow);
                this.showText = obj.skuName;
                this.$emit("change", obj.skuId, obj);
            }
        },
        dialogClose() {
            this.visible = false;
        },
        onLoad(page, params) {
            this.loading = true;
            getPage(page.currentPage, page.pageSize, params)
                .then((res) => {
                    this.data = res.data.data.records;
                    this.page.total = res.data.data.total;
                })
                .finally(() => {
                    this.loading = false;
                });
        },
        //模糊搜索
        onSearch() {
            this.page.currentPage = 1;
            this.onLoad(this.page, this.searchData);
        },
        searchReset() {
            this.page.currentPage = 1;
            this.searchData = Object.assign({}, this.search);
            this.onLoad(this.page, this.searchData);
        },
        sizeChange(size) {
            this.page.pageSize = size;
            let search = Object.assign({}, this.search);
            search.skuCode = this.searchData.skuCode;
            search.skuName = this.searchData.skuName;
            this.onLoad(this.page, search);
        },
        currentChange(current) {
            this.page.currentPage = current;
            let search = Object.assign({}, this.search);
            search.skuCode = this.searchData.skuCode;
            search.skuName = this.searchData.skuName;
            this.onLoad(this.page, search);
        },
        //获取选中用户的数据
        handleCurrentChange(val) {
            this.currentRow = val;
        },
        //双击选中
        dblhandleCurrentChange() {
            this.dialogConfirm();
        },
    },
};
</script>

<style lang="scss">
.select-input .el-input__inner {
    cursor: pointer;
}

.dialog__body {
    margin-top: 20px;
    overflow-x: hidden;
}

.el-pager {
    border: none;
}
</style>
