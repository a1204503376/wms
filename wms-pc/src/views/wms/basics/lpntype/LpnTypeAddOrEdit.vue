<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form" :model="form.params" :rules="form.rules" label-position="right" label-width="120px"
                         size="mini" style="margin-left:10px;margin-right:10px;">
                    <el-row>
                        <h3>容器类型管理-{{ isEdit ? '编辑' : '新增' }}</h3>
                    </el-row>
                    <el-row style="margin-top: 20px;">
                        <el-col :span="10">
                            <el-form-item
                                label="类型编码"
                                label-width="90px"
                                prop="code">
                                <el-input
                                    placeholder="请填写类型编码"
                                    v-model="form.params.code">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row style="margin-top: 20px;">
                        <el-col :span="10">
                            <el-form-item label="类别" label-width="90px" prop="lpnType">
                                <nodes-lpn-type-state v-model="form.params.lpnType" :multiple="false">
                                </nodes-lpn-type-state>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row style="margin-top: 20px;">
                        <el-col :span="10">
                            <el-form-item label="编码规则" label-width="90px" prop="lpnNoRule">
                                <el-input v-model="form.params.lpnNoRule" placeholder="填写格式为必须是YyMmDdX"></el-input>
                                <el-tooltip content="自动生成的容器编码格式：类型编码+规则"
                                            style="float:right;z-index: 99;margin-top: 7px;margin-right: -15px">
                                    <i class="el-icon-question"></i>
                                </el-tooltip>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row style="margin-top: 20px;">
                        <el-col :span="10">
                            <el-form-item label="重量(KG)" label-width="90px">
                                <el-input
                                    :clearable="true"
                                    v-model="form.params.weight"
                                    :maxLength="9"
                                    oninput="value=value.replace(/[^\d.]/g,'')">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </el-main>
            <el-footer>
                <el-row style="margin-top: 10px;line-height:60px;text-align:right;">
                    <el-button :loading="loading" type="primary" @click="onSubmit">
                        保 存
                    </el-button>
                    <el-button :loading="loading" @click="onClose">
                        关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesInStoreType from "@/components/wms/select/NodesInStoreType";
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editMixin} from "@/mixins/edit";
import {getLpnTypeById, newLpnType, updateLpnTypeById,} from "@/api/wms/basics/LpnType.js";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesLpnTypeState from "@/components/wms/select/NodesLpnTypeState";
import func from "@/util/func";

export default {
    name: "add",
    components: {
        NodesWarehouse,
        NodesLineNumber,
        NodesSku,
        NodesInStoreType,
        NodesInStoreMode,
        NodesLpnTypeState
    },
    mixins: [editMixin],
    data() {
        return {
            form: {
                params: {
                    code: '',
                    lpnType: '',
                    lpnNoRule: '',
                    weight: 0
                },
                rules: {
                    code: [{
                        required: true,
                        message: '请输入容器类型编码',
                        trigger: 'blur'
                    }],
                    lpnType: [{
                        type: 'number',
                        required: true,
                        message: '请选择容器类型',
                        trigger: 'change'
                    }],
                    lpnNoRule: [{
                        required: true,
                        message: '按照规格填写容器编码规则',
                        trigger: 'blur',
                        validator: this.lpnNoRuleRate
                    }],
                    weight: [{
                        required: true,
                        message: '请输入容器重量',
                        trigger: 'blur'
                    }]
                }
            },
        }
    },
    activated() {
        if (this.isEdit) {
            this.getLpnTypeById();
        }
    },
    methods: {
        getLpnTypeById() {
            let params = {id: this.id};
            getLpnTypeById(params).then((res) => {
                this.form.params = res.data.data;
            })
        },
        lpnNoRuleRate(rule, value, callback) {
            if(func.isEmpty(value)){
                callback(new Error(''))
                return;
            }
            let valueList = value.split("");
            let valueListLength = 0;
            for (let i = 0; i < valueList.length; i++) {
                if (valueList[i] === 'Y' || valueList[i] === 'M' || valueList[i] === 'D' || valueList[i] === 'y' || valueList[i] === 'm' || valueList[i] === 'd' || valueList[i] === 'X') {
                    // else必须写，否则校验规则通过后的代码不会执行
                    valueListLength++;
                }
            }
            if (valueListLength === valueList.length) {
                // else必须写，否则校验规则通过后的代码不会执行
                callback()
            } else {
                callback(new Error(''))
            }
        },
        submitFormParams() {
            if (this.isEdit) {
                return updateLpnTypeById(this.form.params).then((res) => {
                    return {
                        msg: res.data.msg,
                        router: {
                            path: '/wms/basics/LpnType',
                            query: {
                                isRefresh: 'true'
                            }
                        }
                    };
                })
            } else {
                return newLpnType(this.form.params)
                    .then((res) => {
                        return {
                            msg: res.data.msg,
                            router: {
                                path: '/wms/basics/LpnType',
                                query: {
                                    isRefresh: 'true'
                                }
                            }
                        };
                    });
            }

        },
        onChangeRadio(value) {
            this.form.params.status = value;
        }
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
