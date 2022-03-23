<template>
    <af-table-column :label="columnOption.label"
                     :min-width="columnOption.minWidth"
                     :width="columnOption.width"
                     :render-header="columnOption.renderHeader"
                     :align="columnOption.align || crud.tableOption.align"
                     :header-align="columnOption.headerAlign || crud.tableOption.headerAlign"
                     :prop="columnOption.prop">

        <template v-for="column in columnOption.children">
            <column-dynamic v-if="column.children && column.children.length>0"
                            :key="column.label"
                            :columnOption="column">
            </column-dynamic>
            <af-table-column v-else-if="vaildColumn(column)"
                             :key="column.prop"
                             :prop="column.prop"
                             :label="column.label"
                             filter-placement="bottom-end"
                             :filter-method="getColumnProp(column,'filterMethod')?handleFiltersMethod:undefined"
                             :filter-multiple="vaildData(column.filterMultiple,true)"
                             :show-overflow-tooltip="column.overHidden !== false"
                             :min-width="column.minWidth || 100"
                             :sortable="getColumnProp(column,'sortable')"
                             :render-header="column.renderHeader"
                             :align="column.align || crud.tableOption.align"
                             :header-align="column.headerAlign || crud.tableOption.headerAlign"
                             :width="getColumnProp(column,'width')"
                             :fixed="getColumnProp(column,'fixed')">

            </af-table-column>
        </template>
    </af-table-column>
</template>

<script>

export default {
    name: 'column-dynamic',
    inject: ["dynamic", 'crud'],
    props: {
        t: Function,
        columnOption: {
            type: Object,
            required: true
        }
    },
    created() {
        const list = [
            'getColumnProp',
            "corArray",
            "openImg",
            "detailData",
            "vaildLabel",
            "vaildColumn",
            "handleDetail",
            "handleShowLabel",
            "handleChange",
            "columnChange",
            "getImgList",
            "handleFiltersMethod",
            "handleFilters"
        ];
        Object.keys(this.dynamic).forEach(ele => {
            if (list.includes(ele)) {
                this[ele] = this.dynamic[ele];
            }
        });
    }
};
</script>
