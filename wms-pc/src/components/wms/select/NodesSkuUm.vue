<template>
    <el-select
        :clearable="true"
        :disabled="disabled"
        :size="size"
        @change="onChange"
        @visible-change="onFocus"
        collapse-tags
        placeholder="请选择"
        v-model="val"
    >
        <el-option
            :key="item.wsuId"
            :label="item.wsuCode"
            :value="item.wsuCode"
            v-for="item in dataSource">
        </el-option>
    </el-select>

</template>

<script>

    import {findSkuUmSelectResponseListBySkuId} from "@/api/wms/basics/skuUm";
    import func from "@/util/func";

    /**
     * 新建时，根据skuID获取所有的计量单位并默认显示第一个
     * 编辑的时候默认绑定计量单位编码，获得焦点的时候获取该物料的所有计量单位
     */
    export default {
        name: "NodesSkuUm",
        model: {
            prop: 'selectVal',
            event: 'selectValChange'
        },
        props: {
            selectVal: [String],
            //物料编码,新增和编辑时将其设置为当前行的sku对象
            sku: {type: Object, required: false},
            // 组件大小，默认为mini, 支持 medium/small/mini
            size: {type: String, required: false, default: () => "mini"},
            // 是否禁用 默认为 false不禁用
            disabled: {type: Boolean, required: false, default: () => false}
        },
        data() {
            return {
                val: this.selectVal,
                dataSource: []
            }
        },
        watch: {
            sku: async function (newVal, oldVal) {
                if (func.isNotEmpty(this.sku.skuId)) {
                    await this.getDataSource(this.sku.skuId);
                    if (this.dataSource.length > 0) {
                        this.val = this.dataSource[0].wsuCode;
                        this.onChange(this.val);
                    } else {
                        this.onChange('');
                    }
                } else {
                    this.dataSource = [];
                    this.onChange('');
                }
            },
            selectVal(newVal) {
                this.val = newVal;
            }
        },
        methods: {
            async getDataSource(skuId) {
                if (func.isNotEmpty(skuId)) {
                    let skuUmSelectQuery = {
                        skuId
                    };
                    const response = await findSkuUmSelectResponseListBySkuId(skuUmSelectQuery);
                    this.dataSource = response.data.data;
                } else {
                    this.dataSource = undefined;
                    this.$emit('selectValChange', undefined);
                }
            },
            onChange(val) {
                this.$emit('selectValChange', val);
            },
            /**
             * 编辑的时候获取所有的计量单位
             */
            onFocus() {
                if (func.isEmpty(this.dataSource) && func.isNotEmpty(this.sku.id)) {
                    this.getDataSource(this.sku.id);
                }
            }

        }
    }
</script>

<style scoped>

</style>
