<template>
    <nodes-select
        v-model="val"
        :clearable="true"
        :data-source="dataSource"
    >
    </nodes-select>
</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getReceiveStateList} from "@/api/wms/instock/receive";

export default {
    name: "NodesReceiveBillState",
    components: {NodesSelect},
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: Array
    },
    data() {
        return {
            val: this.selectVal,
            dataSource: []
        }
    },
    watch: {
        val(newVal) {
            this.$emit('selectValChange', newVal);
        },
        selectVal(newVal){
            this.val = newVal;
        }
    },
    created() {
        this.getDataSource();
    },
    methods: {
        async getDataSource() {
            const response = await getReceiveStateList();
            this.dataSource = response.data.data;
        }
    }
}
</script>

<style scoped>

</style>
