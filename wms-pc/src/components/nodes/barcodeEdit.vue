<template>
    <el-dialog :title="title"
               :visible.sync="visible"
               :close-on-click-modal="false"
               @open="dialogOpen"
               @opened="dialogOpened"
               :before-close="dialogClose"
               v-dialogDrag="true"
               :width="width"
               top="3vh"
               class="dialogs"
               append-to-body
               destroy-on-close>
        <div class="dialog-body" v-loading="loading">
            <el-form label-position="right"
                     label-width="100px"
                     size="medium"
                     ref="form"
                     :model="form"
            >
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item prop="columnName"
                                      label="字段名称"
                                      :rules="rules.columnName"
                                      :show-message="false"
                        >
                            <el-input ref='txtColumnName' v-model="form.columnName" size="medium"
                                      placeholder="字段名称"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item prop="name" label="数据字段"
                                      :show-message="false"
                        >
                            <template v-for="col in column">
                                <el-button size="medium"
                                           style="margin-right:5px;margin-left:5px;"
                                           v-model="col.prop"
                                           @click="dataClick(col)">
                                    {{col.label}}
                                </el-button>
                            </template>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="20">
                        <el-form-item prop="name" label="自定义内容"
                                      :show-message="false"
                        >
                            <el-input ref='txtCustom'
                                      v-model="form.custom"
                                      size="medium"
                                      placeholder="自定义内容"
                                      @keyup.enter.native="saveCustom"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="4">
                        <el-button size="medium" type="primary" plain @click="saveCustom">填入</el-button>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="24">
                        <el-form-item prop="name" label="条码内容"
                                      :show-message="false"
                        >
                            <el-button size="medium" type="danger" plain @click="deleteItem">删除</el-button>
                            <el-input v-model="form.content"
                                      type="textarea"
                                      :rows="2"
                                      :readonly="true"
                                      placeholder="请输入内容">
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
            </el-form>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary"
                       :loading="loading"
                       @click="save">保 存
            </el-button>
            <el-button @click="dialogClose">关 闭</el-button>
        </div>
    </el-dialog>
</template>

<script>
    import {getDetail} from "@/api/core/param";

    export default {
        name: "barcodeEdit",
        props: {
            // 对话框显隐
            visible: {type: Boolean, default: false},
            // 所有字段列表
            fields: {type: Array, default: []},
            // 数据源 编辑时传递
            dataSource: {},
        },
        data() {
            return {
                title: '条码内容-编辑器',
                loading: false,
                width: '60%',
                form: {
                    columnName: '',
                    custom: '',
                    content: '',
                },
                rules: {
                    columnName: [{
                        required: true,
                        message: "标签名称不能为空",
                        trigger: "blur",
                        pattern: /\S/
                    }],
                },
                array: [],
                callback: {
                    visible: false,
                    success: false,
                },
                // 数据字段
                column: []
            }
        },
        methods: {
            dialogOpen() {
                getDetail({paramKey: this.$param.label.fields}).then(res => {
                    this.column = JSON.parse(res.data.paramValue);
                });
            },
            dialogOpened() {
                this.loading = true;
                if (this.dataSource) {
                    this.form.columnName = this.dataSource.columnName;
                    this.form[this.$commonConst.TABLE_ROW_KEY] = this.dataSource[this.$commonConst.TABLE_ROW_KEY];
                    this.array = JSON.parse(this.dataSource.columnContent);
                    this.arrayChanged();
                } else {
                    this.form = {
                        columnName: '',
                        custom: '',
                        content: ''
                    }
                    this.form[this.$commonConst.TABLE_ROW_KEY] = null;
                }
                this.loading = false;
                this.$refs.txtColumnName.focus();
            },
            dialogClose() {
                for (let key in this.form) {
                    this.form[key] = '';
                }
                this.array = [];
                this.callback.visible = false;
                this.callback.success = false;
                this.$emit('callback', this.callback);
            },
            save() {
                this.$refs.form.validate(valid => {
                    if (valid) {
                        if (this.fields && this.fields.length > 0) {
                            let findIndex = this.fields.findIndex(u => {
                                return u.columnName === this.form.columnName &&
                                    u[this.$commonConst.TABLE_ROW_KEY] !== this.form[this.$commonConst.TABLE_ROW_KEY];
                            });
                            if (findIndex > -1) {
                                this.$message.warning('字段名称：' + this.form.columnName + ' 已存在！');
                                this.$refs.txtColumnName.focus();
                                return false;
                            }
                        }
                        this.callback.visible = false;
                        this.callback.success = true;
                        this.callback.data = {
                            columnName: this.form.columnName,
                            columnContent: JSON.stringify(this.array)
                        }
                        if (this.form[this.$commonConst.TABLE_ROW_KEY]) {
                            this.$set(
                                this.callback.data,
                                this.$commonConst.TABLE_ROW_KEY,
                                this.form[this.$commonConst.TABLE_ROW_KEY]);
                        }
                        this.array = [];
                        this.$refs.form.resetFields();
                        this.$emit('callback', this.callback);
                    } else {
                        return false;
                    }
                });
            },
            dataClick(col) {
                this.array.push(col);

                this.arrayChanged();
            },
            saveCustom() {
                this.array.push(this.form.custom);
                this.form.custom = '';

                this.arrayChanged();
            },
            deleteItem() {
                if (!this.array || this.array.length === 0) {
                    return;
                }
                this.array.splice(this.array.length - 1, 1);

                this.arrayChanged();
            },
            arrayChanged() {
                // 更新 textarea 的内容
                let content = '';
                this.array.forEach(item => {
                    if (typeof item === 'string') {
                        content += item;
                    } else if (typeof item === 'object') {
                        content += item['label'];
                    }
                });
                this.form.content = content;
                this.$refs.txtCustom.focus();
            }
        }
    }
</script>

<style scoped>

</style>
