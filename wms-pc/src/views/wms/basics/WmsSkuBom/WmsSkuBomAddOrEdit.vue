<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form" :model="form.params" :rules="form.rules" label-position="right" label-width="120px"
                         size="mini" style="margin-left:10px;margin-right:10px;">
                    <el-row>
                        <h3>物料清单管理-{{ isEdit ? '编辑' : '新增' }}</h3>
                    </el-row>
                    <el-row style="margin-top: 20px">
                        <el-col :span="10">
                            <el-form-item label="组合编码" label-width="90px" prop="joinSkuId">
                                <nodes-sku v-model="form.params.joinSkuId"  ></nodes-sku>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row  style="margin-top: 20px">
                        <el-col :span="10">
                            <el-form-item label="物品编码" label-width="90px" prop="skuId">
                                <nodes-sku v-model="form.params.skuId"
                                ></nodes-sku>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row  style="margin-top: 20px;">
                        <el-col :span="10">
                            <el-form-item label="货主" label-width="90px" prop="woId" >
                                <nodes-owner v-model="form.params.woId"></nodes-owner>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row  style="margin-top: 20px">
                        <el-col :span="10">
                            <el-form-item label="数量"  label-width="90px" prop="qty">
                                <el-input-number v-model="form.params.qty" @change="handleChange" :min="1"
                                                 label="请输入数量"></el-input-number>
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
    import NodesLineNumber from "@/components/wms/table/NodesLineNumber";
    import NodesOwner from "@/components/wms/select/NodesOwner";
    import NodesSku from "@/components/wms/select/NodesSku";
    import {
        editMixin
    } from "@/mixins/edit";
    import {
        save,
        selectSkuBomById
    } from "@/api/wms/basics/WmsSkuBom.js";
    import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
    import NodesLpnTypeState from "@/components/wms/select/NodesLpnTypeState";
    import Schema from 'async-validator';
    import func from "@/util/func";

    export default {
        name: "add",
        components: {
            NodesWarehouse,
            NodesLineNumber,
            NodesSku,
            NodesInStoreType,
            NodesInStoreMode,
            NodesLpnTypeState,
            NodesOwner
        },
        mixins: [editMixin],
        data() {
            return {
                form: {
                    params: {
                        woId: '',
                        skuId: '',
                        joinSkuId: '',
                        qty: 1,
                    },
                    rules: {
                        joinSkuId: [{
                            required: true,
                            message: '请选择组合编码',
                            trigger: 'blur'
                        }],
                        skuId: [{
                            required: true,
                            message: '请选择物品编码',
                            trigger: 'blur'
                        }],
                        woId: [{
                            required: true,
                            message: '请选择货主',
                            trigger: 'blur'
                        }],
                        qty: [{
                            required: true,
                            message: '请输入数量',
                            trigger: 'blur'
                        }]
                    }
                },
            }
        },
        activated() {
            if (this.isEdit) {
                this.getSkuBomById();
            }
        },
        methods: {
            onChangeSku() {
            },
            handleChange(value) {
                console.log(value);
            },
            getSkuBomById() {
                // eslint-disable-next-line no-unused-vars
                let params = {id: this.id};
                selectSkuBomById(params).then((res) => {
                    this.form.params = res.data.data;
                    this.form.params.skuId = {
                        skuCode: res.data.data.skuCode,
                        skuId: res.data.data.skuId
                    };
                    this.form.params.joinSkuId = {
                        skuCode: res.data.data.joinSkuCode,
                        skuId: res.data.data.joinSkuId
                    };
                })
            },
            submitFormParams() {
                this.form.params.skuId = this.form.params.skuId.skuId;
                this.form.params.joinSkuId = this.form.params.joinSkuId.skuId;
                save(this.form.params)
                    .then((res) => {
                        this.$message.success(res.data.msg);
                        this.loading = false;
                        this.onClose();
                        this.$router.push({
                            path: "/wms/basics/WmsSkuBom",
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
