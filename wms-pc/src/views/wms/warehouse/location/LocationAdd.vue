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
                        <h3>常规</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="库位编码" prop="code">
                                <el-input v-model="form.params.code"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库房" prop="name">
                                <el-input v-model="form.params.name"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库区" prop="simpleName">
                                <el-input v-model="form.params.simpleName"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="应用类型" prop="code">
                                <el-input v-model="form.params.code"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库位种类" prop="name">
                                <el-input v-model="form.params.name"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库位处理" prop="simpleName">
                                <el-input v-model="form.params.simpleName"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="校验数位" prop="code">
                                <el-input v-model="form.params.code"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="线路顺序" prop="name">
                                <el-input v-model="form.params.name"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="使用状态" prop="simpleName">
                                <el-input v-model="form.params.simpleName"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="ABC" prop="code">
                                <el-input v-model="form.params.code"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货架列" prop="name">
                                <el-input v-model="form.params.name"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货架排" prop="simpleName">
                                <el-input v-model="form.params.simpleName"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="上架顺序" prop="name">
                                <el-input v-model="form.params.name"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="适用的类型容器" prop="code">
                                <el-input v-model="form.params.code"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <h3>维数</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="长度" prop="woId">
                                <nodes-owner
                                    v-model="form.params.woId"
                                    :multiple="false">
                                </nodes-owner>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="高度" prop="remark">
                                <el-input v-model="form.params.remark"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="宽度" prop="status">
                                <el-radio v-model="form.params.status" :label=1>是</el-radio>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="容量" prop="woId">
                                <nodes-owner
                                    v-model="form.params.woId"
                                    :multiple="false">
                                </nodes-owner>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="载重量" prop="remark">
                                <el-input v-model="form.params.remark"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货架层" prop="status">
                                <el-radio v-model="form.params.status" :label=1>是</el-radio>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="最大件数" prop="woId">
                                <nodes-owner
                                    v-model="form.params.woId"
                                    :multiple="false">
                                </nodes-owner>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="最大托数" prop="remark">
                                <el-input v-model="form.params.remark"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <h3>混放规则</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="混放物品" prop="woId">
                                <nodes-owner
                                    v-model="form.params.woId"
                                    :multiple="false">
                                </nodes-owner>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="混放批号" prop="remark">
                                <el-input v-model="form.params.remark"></el-input>
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
                    >保 存
                    </el-button>
                    <el-button
                        :loading="loading"
                        @click="onClose"
                    >关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>

import {editMixin} from "@/mixins/edit";
import {add} from "@/api/wms/basics/supplier"
import NodesOwner from "@/components/wms/select/NodesOwner";

export default {
    name: "add",
    components: {NodesOwner},
    mixins: [editMixin],
    data() {
        return {
            form: {
                params: {
                    code: '',
                    name: '',
                    simpleName: '',
                    status: 1,
                    woId: '',
                    remark: ''
                },
                rules: {
                    code: [
                        {
                            required: true,
                            message: '请输入库位编码',
                            trigger: 'blur'
                        }
                    ],
                    name: [
                        {
                            required: true,
                            message: '请选择库房',
                            trigger: 'change'
                        }
                    ],
                    simpleName: [
                        {
                            required: true,
                            message: '请选择库区',
                            trigger: 'change'
                        }
                    ],
                }
            },
        }
    },
    methods: {
        submitFormParams() {
            return add(this.form.params)
                .then(res => {
                    return {
                        msg: res.data.msg,
                        router: {
                            path: '/wms/basics/supplier',
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
