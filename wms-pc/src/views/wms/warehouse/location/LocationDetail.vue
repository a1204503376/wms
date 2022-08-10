<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;height: 100%">
                <el-form ref="form"
                         :model="form.params"
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
                            <el-form-item label="库位编码：">
                                {{ form.params.locCode }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库房：">
                                {{ form.params.whName }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库区：">
                                {{ form.params.zoneName }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="库位类型：">
                                {{ form.params.locType }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库位种类：">
                                {{ form.params.locCategory }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库位处理：">
                                {{ form.params.locHandling }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="校验数位：">
                                {{ form.params.checkDigit }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="线路顺序：">
                                {{ form.params.logicAllocation }}
                            </el-form-item>
                        </el-col>
                        <el-tooltip placement="top">
                            <div slot="content">破损:不能上架库存<br/>冻结:可以上架库存，但库存状态为冻结<br/>业务系统冻结:不能上架库存到该库位</div>
                            <el-col :span="8">
                                <el-form-item label="使用状态：">
                                    {{ form.params.locFlag }}
                                </el-form-item>
                            </el-col>
                        </el-tooltip>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="ABC分类：">
                                {{ form.params.abc }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货架列：">
                                {{ form.params.locColumn }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货架排：">
                                {{ form.params.locBank }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="上架顺序：">
                                {{ form.params.putOrder }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="容器类别：">
                                {{ form.params.lpnTypeCode }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <h3>维数</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="长度：">
                                {{ form.params.locLength }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="高度：">
                                {{ form.params.locHigh }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="宽度：">
                                {{ form.params.locWide }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="容量：">
                                {{ form.params.capacity }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="载重量：">
                                {{ form.params.loadWeight }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货架层：">
                                {{ form.params.locLevel }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="最大件数：">
                                {{ form.params.itemNum }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="最大托数：">
                                {{ form.params.trayNum }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <h3>混放规则</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="混放物品：">
                                {{ form.params.locSkuMix }}
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="混放批号：">
                                {{ form.params.locLotNoMix }}
                            </el-form-item>
                        </el-col>
                    </el-row>
                </el-form>
            </el-main>
            <el-footer>
                <el-row style="margin-top: 10px;line-height:60px;text-align:right;">
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
import {detail} from "@/api/wms/basics/location"
import func from "@/util/func";


export default {
    name: "add",
    mixins: [editMixin],
    props: {
        locId: {type: String, required: true}
    },
    data() {
        return {
            form: {
                params: {
                    locCode: '',
                    whName: '',
                    zoneName: '',
                    locTypeDesc: '',
                    locCategoryDesc: '',
                    locHandlingDesc: '',
                    checkDigit: '',
                    logicAllocation: '',
                    locFlagDesc: '',
                    abcDesc: '',
                    lpnTypeCode: '',
                    locHigh: '',
                    locLength: '',
                    locWide: '',
                    capacity: '',
                    loadWeight: '',
                    locLevel: '',
                    locBank: '',
                    locColumn: '',
                    putOrder: 0,
                    itemNum: 0,
                    trayNum: 0,
                    locSkuMixDesc: '',
                    locLotNoMixDesc: ''
                },
            },
        }
    },
    created() {
        this.getDataSource();
    },
    watch: {
        locId() {
            this.getDataSource();
        }
    },
    methods: {
        getDataSource() {
            if (func.isEmpty(this.locId)) {
                return;
            }
            detail(this.locId)
                .then((res) => {
                    let data = res.data.data;
                    this.form.params = data
                })
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
