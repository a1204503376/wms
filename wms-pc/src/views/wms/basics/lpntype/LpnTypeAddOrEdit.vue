<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form" :model="form.params" :rules="form.rules" label-position="right" label-width="120px"
                    size="mini" style="margin-left:10px;margin-right:10px;">
                    <el-row>
                        <h3>容器管理-{{ isEdit ? '编辑' : '新增' }}</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="容器类型编码" prop="code">
                                <el-input v-model="form.params.code"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="容器类型" prop="lpnType">
                                <nodes-lpn-type-state :multiple="false" v-model="form.params.lpnType">
                                </nodes-lpn-type-state>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="容器编码规则" prop="lpnNoRule" >
                                <el-input v-model="form.params.lpnNoRule" placeholder="填写格式为必须是YyMmDdX"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="容器重量">
                                <el-input oninput="value=value.replace(/[^\d.]/g,'')" v-model="form.params.weight">
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
    import {
        editMixin
    } from "@/mixins/edit";
    // eslint-disable-next-line no-unused-vars
    import {
        // eslint-disable-next-line no-unused-vars
        newLpnType,
    } from "@/api/wms/basics/LpnType.js";
    import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
    // eslint-disable-next-line no-unused-vars
    import NodesLpnTypeState from "@/components/wms/select/NodesLpnTypeState";
    // eslint-disable-next-line no-unused-vars
    import Schema from 'async-validator';
    import func from "../../../../util/func";

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
                        weight:[{
                            required: true,
                            message: '请输入容器重量',
                            trigger: 'blur'
                        }]
                    }
                },
            }
        },
        methods: {
            lpnNoRuleRate (rule, value, callback) {
                // eslint-disable-next-line no-unused-vars
                var stringValue=value;
                // eslint-disable-next-line no-unused-vars
                var valueList=stringValue.split("");
                // eslint-disable-next-line no-unused-vars
                var valueListlength=0;
                for(var i=0;i<valueList.length;i++)
                {
                    if (valueList[i]=='Y'||valueList[i]=='M'||valueList[i]=='D'||valueList[i]=='y'||valueList[i]=='m'||valueList[i]=='d'||valueList[i]=='X') {
                        // else必须写，否则校验规则通过后的代码不会执行
                        valueListlength++;
                    }
                }
                if (valueListlength==valueList.length) {
                    // else必须写，否则校验规则通过后的代码不会执行
                    callback()
                }else {
                    callback(new Error('字符只能是YyMmDdXx'))
                }
            },
            submitFormParams() {
                        newLpnType(this.form.params)
                            .then((res) => {
                                this.$message.success(res.data.msg);
                                this.loading=false;
                                this.onClose();
                                this.$router.push({
                                    path: "/wms/basics/LpnType",
                                    query: {
                                        isRefresh: 'true'
                                    }
                                })
                            })


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
