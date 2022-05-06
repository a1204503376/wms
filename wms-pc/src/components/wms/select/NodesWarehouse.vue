<template>
    <nodes-select
        v-model="val"
        :data-source="dataSource"
    >
    </nodes-select>
</template>

<script>
import NodesSelect from "@/components/wms/general/NodesSelect";
import {getWarehouseSelectResponseList} from "@/api/wms/warehouse/warehouse";

export default {
    name: "NodesWarehouse",
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
        }
    },
    created() {
        this.getDataSource();
    },
    methods: {
        async getDataSource() {
            const response = await getWarehouseSelectResponseList();
            this.dataSource = response.data.data;
        }
    }
}
</script>

<style scoped>

</style>
