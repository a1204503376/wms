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
                        <h3>客户单-{{ isEdit ? '编辑' : '新增' }}</h3>
                    </el-row>

                        <el-row>
                        <el-col :span="8">
                            <el-form-item label="客户编码" prop="code">
                                <el-input v-model="form.params.code"></el-input>
                            </el-form-item>
                        </el-col>
                        </el-row>
                        <el-row>
                        <el-col :span="8">
                            <el-form-item label="客户名称" prop="name">
                                <el-input v-model="form.params.name"></el-input>
                            </el-form-item>
                        </el-col>
                        </el-row>
                       <el-row>
                        <el-col :span="8">
                            <el-form-item label="客户简称" prop="simpleName">
                                <el-input v-model="form.params.simpleName"></el-input>
                            </el-form-item>
                        </el-col>
                       </el-row>

<!--                    TODO 货主组件有问题，稍后修改-->
<!--                    <el-row>-->
<!--                        <el-col :span="8">-->
<!--                            <el-form-item label="货主" prop="woId">-->
<!--                                <nodes-owner></nodes-owner>-->
<!--                            </el-form-item>-->
<!--                        </el-col>-->
<!--                    </el-row>-->
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

                                <template>
                                    <el-radio v-model="form.params.status" :label=1>是</el-radio>
                                    <el-radio v-model="form.params.status" :label=2>否</el-radio>
                                </template>

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
import NodesOwner  from "@/components/wms/select/NodesOwner"
import NodesSku from "@/components/wms/select/NodesSku";
import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
import {add} from "@/api/wms/basics/customer";
import {editMixin} from "@/mixins/edit";

export default {
    name: "new",
    components: {NodesOwner, NodesLineNumber, NodesSku, NodesInStoreType, NodesInStoreMode},
    mixins: [editMixin],
    data() {
        return {
            form: {
                params: {
                    code: '',
                    name: '',
                    simpleName: '',
                    woId: null,
                    remark: '',
                    status: 1
                },
                rules: {

                    code: [
                        {
                            required: true,
                            message: '请输入客户编码',
                            trigger: 'blur'
                        }
                    ],
                    name: [
                        {
                            required: true,
                            message: '请输入客户名称',
                            trigger: 'blur'
                        }
                    ],
                    status: [
                        {
                            required: true,
                            message: '请选择是否启用',
                            trigger: 'blur'
                        }
                    ]

                }
            }
        }
    },
    created() {

    },
    methods: {
        submitFormParams() {
          return add(this.form.params)
          .then(res => {
            return {
              msg: res.data.msg,
              router: {
                            path: '/wms/basics/customer',
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
