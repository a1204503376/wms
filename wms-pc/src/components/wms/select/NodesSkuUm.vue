<template>
    <el-select
        v-model="val"
        collapse-tags
        placeholder="请选择"
        :multiple="multiple"
        size="mini"
        style="width:100%;"
        @change="onChange"
        @visible-change="onFocus"
           >
        <el-option
            v-for="item in dataSource"
            :key="item.wsuId"
            :label="item.wsuCode"
            :value="item.wsuCode">
        </el-option>
    </el-select>

</template>

<script>

import {getSkuUmSelectResponseListBySkuId} from "@/api/wms/basics/skuUm";
import func from "@/util/func";


export default {
    name: "NodesSkuUm",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String],
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false},
      //物料编码,新增和编辑时将其设置为当前行的skuId
        skuId:{type: String, required: false}
    },
    data() {
        return {
            val:this.selectVal,
            dataSource: []
        }
    },

    watch: {
        skuId() {
            this.getDataSource(this.skuId).then(res => {
               this.selectVal = this.dataSource[0].wsuCode
            });

        },
        selectVal(newVal) {
            this.val=newVal;
        }

    },
    methods: {
        async getDataSource(skuId) {
            let skuUmSelectQuery = {
                skuId
            };
            const response = await getSkuUmSelectResponseListBySkuId(skuUmSelectQuery);
            this.dataSource = response.data.data;
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        },
        onFocus(){
          if(func.isEmpty(this.dataSource) && func.isNotEmpty(this.skuId)){
              this.getDataSource(this.skuId);
          }
        }

    }
}
</script>

<style scoped>

</style>
