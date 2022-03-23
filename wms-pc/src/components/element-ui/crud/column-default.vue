<template>
    <div>
        <af-table-column width="1px"></af-table-column>
        <!-- 折叠面板  -->
        <af-table-column v-if="tableOption.expand"
                         :fixed="vaildData(tableOption.expandFixed, config.expandFixed)"
                         :width="tableOption.expandWidth || config.expandWidth"
                         align="center"
                         type="expand">
            <template slot-scope="{row}">
                <slot :index="row.$index"
                      :row="row"
                      name="expand"></slot>
            </template>
        </af-table-column>
        <!-- 选择框 -->
        <af-table-column v-if="tableOption.selection !== false"
                         :fixed="vaildData(tableOption.selectionFixed, config.selectionFixed)"
                         :reserve-selection="vaildData(tableOption.reserveSelection)"
                         :selectable="tableOption.selectable"
                         :width="tableOption.selectionWidth || config.selectionWidth"
                         align="center"
                         type="selection"></af-table-column>
        <!-- 序号 -->
        <af-table-column v-if="tableOption.index !== false"
                         :fixed="vaildData(tableOption.indexFixed, config.indexFixed)"
                         :label="tableOption.indexLabel || config.indexLabel"
                         :width="tableOption.indexWidth || config.indexWidth"
                         align="center"
                         type="index">
        </af-table-column>
    </div>
</template>

<script>

import config from "./config.js";


export default {
    name: "column-default",
    data() {
        return {
            config: config,
        }
    },
    inject: ["crud"],
    props: {
        tableOption: {
            type: Object,
            default: () => {
                return {};
            }
        }
    },
    methods: {
        setSort() {
            this.rowDrop()
            this.columnDrop()
        },
        rowDrop() {
            const el = this.crud.$refs.table.$el.querySelectorAll(this.config.dropRowClass)[0]
            this.crud.tableDrop(el, evt => {
                const oldIndex = evt.oldIndex;
                const newIndex = evt.newIndex;
                const targetRow = this.crud.list.splice(oldIndex, 1)[0]
                this.crud.list.splice(newIndex, 0, targetRow)
                this.crud.$emit('sortable-change', oldIndex, newIndex, targetRow, this.crud.list)
            })
        },
        columnDrop() {
            let el = this.crud.$refs.table.$el.querySelector(this.config.dropColClass);
            let headerLen = el.children.length
            headerLen = headerLen - this.crud.columnOption.length - 2;
            this.crud.tableDrop(el, evt => {
                const oldIndex = evt.oldIndex - headerLen;
                const newIndex = evt.newIndex - headerLen;
                this.crud.headerSort(oldIndex, newIndex)
            })
        },
    }
}
</script>
