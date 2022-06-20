<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form.params"
                         :rules="form.rules"
                         label-position="right"
                         label-width="120px"
                         size="mini"
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <h3>系统版本-编辑</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="版本号数值" prop="verNum">
                                <el-input-number style="width: 289px" v-model="form.params.verNum"></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="版本号名称" prop="verName">
                                <el-input v-model="form.params.verName"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="更新地址" prop="updateUrl">
                                <el-input v-model="form.params.updateUrl"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="备注" prop="updateRemark">
                                <el-input
                                    type="textarea"
                                    v-model="form.params.updateRemark">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </el-main>
            <el-footer>
                <el-row style="margin-top: 10px;line-height:60px;text-align:right;">
                    <el-button
                        :loading="loading"
                        type="primary"
                        @click="onSubmit"
                    >
                        保 存
                    </el-button>
                    <el-button
                        :loading="loading"
                        @click="onClose"
                    >
                        关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>

import {editMixin} from "@/mixins/edit";
import func from "@/util/func";
import {detailByEdit, edit} from "@/api/wms/system/updateVer";
import NodesDictionary from "@/components/wms/select/NodesDictionary";

export default {
    name: "edit",
    components: {NodesDictionary},
    mixins: [editMixin],
    props: {
        suvId: {type: String, required: true}
    },
    data() {
        return {
            form: {
                params: {
                    verNum: '',
                    verName: '',
                    moduleType: '',
                    updateUrl: '',
                    updateRemark: '',
                    updateType: ''
                },
                rules: {
                    verNum: [
                        {
                            required: true,
                            message: '请输入版本号数值',
                            trigger: 'blur'
                        }
                    ],
                    verName: [
                        {
                            required: true,
                            message: '请输入版本号名称',
                            trigger: 'blur'
                        }
                    ],
                    updateUrl: [
                        {
                            message: '请输入更新地址',
                            trigger: 'blur'
                        }
                    ],
                }
            },
        }
    },
    created() {
        this.getDataSource();
    },
    methods: {
        getDataSource() {
            if (func.isEmpty(this.suvId)) {
                return;
            }
            detailByEdit(this.suvId).then((res) => {
                this.form.params = res.data.data;
            })
        },
        submitFormParams() {
            return edit(this.form.params)
                .then(res => {
                    return {
                        msg: res.data.msg,
                        router: {
                            path: '/wms/system/updateVer',
                            query: {
                                isRefresh: 'true'
                            }
                        }
                    };
                });
        },
    }
}
</script>

<style scoped>
.d-table-header-required:before {
    content: "*";
    color: #F56C6C;
    margin-right: 4px;
}
</style>
