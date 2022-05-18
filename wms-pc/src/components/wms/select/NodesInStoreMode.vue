<template>
    <nodes-select
        v-model="val"
        :data-source="dataSource"
        v-bind="$attrs"
    >
    </nodes-select>
</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {stateService} from "@/api/wms/state/asnBillState";

export default {
    name: "NodesInStoreMode",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Number, Array, String],
        //是否有默认值 true:有默认值  默认为false 编辑时将其设置为true
        defaultValue:{type:Boolean,required: false,default: () => false}
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: []
        }
    },
    watch: {
        val(newVal) {
            this.selectVal = newVal
        },
        selectVal(newVal) {
            this.val=newVal;
            }
    },
    async created() {
        await this.getDataSource()
        if(this.defaultValue){
            this.val = this.dataSource[0].value
        }
    },
    methods: {
        async getDataSource() {
            this.dataSource = await stateService.getStorageMethod();
        }
    }
}
</script>

<style scoped>

</style>
