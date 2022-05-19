<template>
    <el-select
        v-model="val"
        collapse-tags
        placeholder="请选择"
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
        sku:{type: Object, required: false}
    },
    data() {
        return {
            val:this.selectVal,
            dataSource: []
        }
    },
    watch:{
        sku: async function(){
            await this.getDataSource(this.sku.skuId);
            if(this.dataSource.length>0) {
                this.val = this.dataSource[0].wsuCode;
                this.onChange(this.val);
            }
        },
    },
    methods: {
        async getDataSource(skuId) {
            let skuUmSelectQuery = {
                skuId
            };
            const response = await findSkuUmSelectResponseListBySkuId(skuUmSelectQuery);
            this.dataSource = response.data.data;
        },
        onChange(val) {
            this.$emit('selectValChange', val);
        },
        /**
         * 编辑的时候获取所有的计量单位
         */
        onFocus(){
          if(func.isEmpty(this.dataSource) && func.isNotEmpty(this.sku)){
              this.getDataSource(this.sku);
          }
        }

    }
}
</script>

<style scoped>

</style>
