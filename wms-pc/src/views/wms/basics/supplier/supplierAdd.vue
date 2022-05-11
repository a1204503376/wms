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
                        <h3>供应商-{{ isEdit ? '编辑' : '新增' }}</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="供应商编码" prop="code">
                                <el-input v-model="form.params.code"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="供应商名称" prop="name">
                                <el-input v-model="form.params.name"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="供应商简称" prop="simpleName">
                                <el-input v-model="form.params.simpleName"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="货主" prop="woId">
                                <nodes-owner
                                    v-model="form.params.woId">
                                </nodes-owner>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="备注" prop="remark">
                                <el-input v-model="form.params.remark"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="是否启用" prop="status">
                                <el-radio-group v-model="status" @change="onChangeRadio">
                                    <el-radio :label=1>是</el-radio>
                                    <el-radio :label=-1>否</el-radio>
                                </el-radio-group>
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
import NodesInStoreMode from "@/components/wms/select/NodesInStoreMode";
import NodesInStoreType from "@/components/wms/select/NodesInStoreType";
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {editMixin} from "@/mixins/edit";
import {add} from "@/api/wms/basics/supplier"
import NodesOwner from "@/components/wms/select/NodesOwner";
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
// import func from "@/util/func";

export default {
    name: "add",
    components: {NodesWarehouse, NodesOwner, NodesLineNumber, NodesSku, NodesInStoreType, NodesInStoreMode},
    mixins: [editMixin],
    data() {
        return {
            status: 1,
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
                            message: '请输入供应商编码',
                            trigger: 'blur'
                        }
                    ],
                    name: [
                        {
                            required: true,
                            message: '请输入供应商名称',
                            trigger: 'blur'
                        }
                    ],
                    simpleName: [
                        {
                            message: '请输入供应商简称',
                            trigger: 'blur'
                        }
                    ],
                    status: [
                        {
                            type: 'number',
                            required: true,
                            message: '请选择启用状态',
                            trigger: 'change'
                        }
                    ],
                    woId: [
                        {
                            message: '请选择货主',
                            trigger: 'blur'
                        }
                    ],
                    remark: [
                        {
                            message: '请填写备注',
                            trigger: 'blur'
                        }
                    ],
                }
            },
        }
    },
    methods: {
        submitFormParams() {
            add(this.form.params)
                .then(()=>{
                   this.$message.success("新增成功");
                   this.$router.push({
                       path:"/wms/basics/supplier",
                       query: {
                           isRefresh:'true'
                       }
                   })
                })
                .catch((e)=>{
                    this.$message.error(e);
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
