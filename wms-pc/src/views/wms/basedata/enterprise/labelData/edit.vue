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
            <el-form label-position="right"
                     label-width="100px"
                     size="medium"
                     ref="form"
                     :model="form"
            >
                <el-row :gutter="20">
                    <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
                        <el-form-item prop="labelName" label="标签名称"
                                      :rules="rules.name"
                                      :show-message="false"
                        >
                            <el-input placeholder="请输入"
                                      v-model="form.labelName"
                                      clearable
                                      show-word-limit>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
                        <el-form-item prop="skuLevel" label="包装层级"
                                      :show-message="false"
                        >
                            <el-select v-model="form.skuLevel"
                                       filterable
                                       clearable
                                       collapse-tags
                                       placeholder="请选择"
                                       style="width:100%;"
                            >
                                <el-option
                                        v-for="item in sourceSkuLevel"
                                        :key="item.dictKey"
                                        :value="item.dictKey"
                                        :label="item.dictValue"
                                ></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :sm="12" :md="12" :lg="8" :xl="8">
                        <el-form-item prop="isActive" label="是否启用"
                                      :rules="rules.isActive"
                                      :show-message="false"
                        >
                            <el-radio-group v-model="form.isActive">
                                <el-radio v-for="item in sourceActive"
                                          :label="item.value">
                                    {{item.label}}
                                </el-radio>
                            </el-radio-group>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-table
                                class="table-style"
                                ref="dynamicTable"
                                :data="form.labelColumnList"
                                border
                                height="500"
                                style="width: 100%"
                                show-overflow-tooltip
                        >
                            <el-table-column type="index" width="55" align="center">
                                <template slot="header">
                                    <el-button style="padding:4px;" type="primary" icon="el-icon-plus" size="mini"
                                               circle
                                               @click="rowAdd"></el-button>
                                </template>
                            </el-table-column>
                            <el-table-column
                                    prop="columnName"
                                    label="字段名称"
                                    min-width="80"
                                    width="120"
                                    :show-overflow-tooltip="true"
                            ></el-table-column>
                            <el-table-column
                                    prop="columnContent"
                                    label="字段内容"
                                    min-width="80"
                                    :show-overflow-tooltip="true"
                            ></el-table-column>
                            <el-table-column
                                    fixed="right"
                                    label="操作"
                                    width="80"
                                    :key="Math.random()"
                            >
                                <template slot-scope="scope">
                                    <el-tooltip class="item" effect="dark" :enterable="false" content="编辑"
                                                placement="top">
                                        <el-button type="text" @click="rowEdit(scope)">
                                            <i class="el-icon-edit"></i>
                                        </el-button>
                                    </el-tooltip>
                                    <el-tooltip class="item" :enterable="false" effect="dark"
                                                content="删除" placement="top">
                                        <el-button type="text" @click="rowDel(scope)">
                                            <i class="el-icon-delete"></i>
                                        </el-button>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                        </el-table>
                    </el-col>
                </el-row>
            </el-form>
            <barcode-edit
                    :visible="barcodeEdit.visible"
                    :fields="barcodeEdit.fields"
                    :dataSource="barcodeEdit.dataSource"
                    @callback="callbackBarcodeEdit"
            ></barcode-edit>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary"
                       @click="save"
                       :loading="loading">保 存
            </el-button>
            <el-button @click="dialogClose">关 闭</el-button>
        </div>
    </el-dialog>
</template>

<script>
    import {submit} from "@/api/wms/basedata/label";
    import {getDictByCode} from "@/api/core/dict";
    import barcodeEdit from "@/components/nodes/barcodeEdit";

    export default {
        name: "edit",
        props: {
            visible: {type: Boolean, default: false},
            dataSource: {type: Object, default: {}},
        },
        components: {barcodeEdit},
        data() {
            return {
                title: '标签数据',
                loading: false,
                form: {
                    labelName: '',
                    isActive: 1,
                    labelColumnList: [],
                    labelColumnDeleteList: []
                },
                rules: {
                    name: [{
                        required: true,
                        message: "标签名称不能为空",
                        trigger: "blur",
                        pattern: /\S/
                    }],
                    isActive: [{
                        required: true,
                        message: "是否启用不能为空",
                        trigger: "blur",
                        pattern: /\S/
                    }]
                },
                callback: {
                    visible: false,
                    success: false
                },
                sourceSkuLevel: [],
                sourceActive: [
                    {label: '是', value: 1},
                    {label: '否', value: 2}
                ],
                barcodeEdit: {
                    visible: false,
                    dataSource: null,
                    fields: [],
                }
            }
        },
        watch:{
            'form.labelColumnList':{
                handler:function(){
                    let data = [];
                    for (let i = 0; i < this.form.labelColumnList.length; i++) {
                        let row = this.form.labelColumnList[i];
                        // 对所有行生成唯一id
                        this.$set(row, this.$commonConst.TABLE_ROW_KEY, this.guid());
                    }
                }
            }
        },
        methods: {
            dialogOpen() {
                getDictByCode(this.$dict.skuLevel).then(res => {
                    this.sourceSkuLevel = res.data.data;
                });
                if (this.dataSource) {
                    this.title = '标签数据-编辑';
                    this.form = Object.assign({}, this.dataSource);
                } else {
                    this.title = '标签数据-新增';
                    this.form = {
                        labelName: '',
                        isActive: 1,
                        labelColumnList: [],
                    };
                }
            },
            dialogClose() {
                this.$refs.form.resetFields();
                this.callback.visible = false;
                this.callback.success = false;
                this.$emit('callback', this.callback);
            },
            save() {
                this.loading = true;
                submit(this.form).then(res => {
                    this.$message.success('操作成功！');
                    this.callback.visible = false;
                    this.callback.success = true;
                    this.$emit('callback', this.callback);
                }).finally(() => {
                    this.loading = false;
                });
            },
            rowAdd() {
                this.barcodeEdit.fields = Object.assign([], this.form.labelColumnList);
                this.barcodeEdit.dataSource = null;
                this.barcodeEdit.visible = true;
            },
            rowEdit(scope) {
                this.barcodeEdit.fields = Object.assign([], this.form.labelColumnList);
                this.barcodeEdit.dataSource = scope.row;
                this.barcodeEdit.visible = true;
            },
            rowDel(scope) {
                this.$confirm("确定删除当前数据？", {
                    confirmButtonText: "确定",
                    cancelButtonText: "取消",
                    type: "warning"
                }).then(() => {
                    let findIndex = this.form.labelColumnList.findIndex(item => {
                        return item[this.$commonConst.TABLE_ROW_KEY] === scope.row[this.$commonConst.TABLE_ROW_KEY];
                    });
                    if (findIndex > -1) {
                        let data = Object.assign({}, this.form.labelColumnList[findIndex]);
                        if (!this.form.labelColumnDeleteList) {
                            this.$set(this.form, 'labelColumnDeleteList', []);
                        }
                        this.form.labelColumnDeleteList.push(data);
                        // 从所有数据中删除
                        this.form.labelColumnList.splice(findIndex, 1);
                    }
                });
            },
            callbackBarcodeEdit(res) {
                if (res.success) {
                    let findIndex = this.form.labelColumnList.findIndex(item => {
                        return item[this.$commonConst.TABLE_ROW_KEY] === res.data[this.$commonConst.TABLE_ROW_KEY];
                    });
                    if (findIndex > -1) {
                        // 修改
                        let data = Object.assign(this.form.labelColumnList[findIndex], res.data);
                        this.form.labelColumnList.splice(findIndex, 1, data);
                    } else {
                        // 新增
                        let data = Object.assign({}, res.data);
                        this.$set(data, this.$commonConst.TABLE_ROW_KEY, this.func.guid());
                        this.form.labelColumnList.push(data);
                    }
                }
                this.barcodeEdit.visible = res.visible;
            },
            // 生成唯一id
            guid() {
                return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
                    let r = Math.random() * 16 | 0,
                        v = c == 'x' ? r : (r & 0x3 | 0x8);
                    return v.toString(16);
                });
            },
        }
    }
</script>

<style scoped>

</style>
