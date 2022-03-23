<template>
    <!-- 操作栏 -->
    <el-table-column v-if="vaildData(tableOption.menu, config.menu) && crud.getPermission('menu')"
                     :align="tableOption.menuAlign || config.menuAlign"
                     :fixed="vaildData(tableOption.menuFixed, config.menuFixed)"
                     :header-align="tableOption.menuHeaderAlign || config.menuHeaderAlign"
                     :label="tableOption.menuTitle || config.menuTitle"
                     :width="crud.isMobile ? (tableOption.menuXsWidth || config.menuXsWidth):( tableOption.menuWidth || config.menuWidth)"
                     prop="menu"
    >
        <template slot="header" slot-scope="scope">
            {{ tableOption.menuTitle || config.menuTitle }}<br/>
            <el-button
                :icon="config.icons.searchBtn"
                :size="config.controlSize"
                circle
                type="primary"
                @click="onLoad()"
            ></el-button>
            <el-button icon="el-icon-refresh-left"
                       :size="config.controlSize"
                       circle
                       type="warning"
                       @click="onClearSearch"></el-button>
        </template>
        <template slot-scope="{row, $index}">
            <el-dropdown v-if="isMenu && !row.$search"
                         :size="config.controlSize"
                         @command="executeCommand($event, row, $index)">
                <el-button :size="config.controlSize"
                           type="text">
                    {{ tableOption.menuBtnTitle || '更多' }}
                    <i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item v-if="tableOption.editBtn != false"
                                      :icon="config.editBtnIcon"
                                      command="9999998"
                    >{{ "编 辑" }}
                    </el-dropdown-item>
                    <el-dropdown-item v-if="tableOption.delBtn != false"
                                      :icon="config.delBtnIcon"
                                      command="9999999"
                    >{{ "删 除" }}
                    </el-dropdown-item>
                    <template v-for="cmdItem in tableOption.menuItem">
                        <template>
                            <el-dropdown-item
                                :key="cmdItem.label"
                                :command="cmdItem.command"
                                :divided="cmdItem.divided"
                            >{{ cmdItem.label }}
                            </el-dropdown-item>
                        </template>
                    </template>
                    <slot :disabled="crud.btnDisabled"
                          :index="$index"
                          :row="row"
                          :size="config.controlSize"
                          :type="menuText('primary')"
                          name="menuBtn"></slot>
                </el-dropdown-menu>
            </el-dropdown>
            <template v-else-if="['button','text','icon'].includes(menuType)">
                <template v-if="vaildData(tableOption.cellBtn,config.cellBtn)">
                    <el-button :disabled="crud.btnDisabledList[$index]"
                               :icon="config.editBtnIcon"
                               :size="config.controlSize"
                               :type="menuText('primary')">
                        <template v-if="!isIconMenu">
                            {{ "编 辑" }}
                        </template>
                    </el-button>
                    <el-button :icon="config.saveBtnIcon"
                               :size="config.controlSize"
                               :type="menuText('primary')">
                        <template v-if="!isIconMenu">
                            {{ "保 存" }}
                        </template>
                    </el-button>
                    <el-button :disabled="crud.btnDisabledList[$index]"
                               :icon="config.cancelBtnIcon"
                               :size="config.controlSize"
                               :type="menuText('danger')">
                        <template v-if="!isIconMenu">
                            {{ "取 消" }}
                        </template>
                    </el-button>
                </template>
                <el-button :disabled="btnDisabled"
                           :icon="config.viewBtnIcon"
                           :size="config.controlSize"
                           :type="menuText('success')">
                    <template v-if="!isIconMenu">
                        {{ "查 看" }}
                    </template>
                </el-button>
                <el-button :disabled="btnDisabled"
                           :icon="config.editBtnIcon"
                           :size="config.controlSize"
                           :type="menuText('primary')"
                           @click="crud.onEdit(row, $index)">
                    <template v-if="!isIconMenu">
                        {{ "编 辑" }}
                    </template>
                </el-button>
                <el-button :disabled="btnDisabled"
                           :icon="config.copyBtnIcon"
                           :size="config.controlSize"
                           :type="menuText('primary')">
                    <template v-if="!isIconMenu">
                        {{ "复 制" }}
                    </template>
                </el-button>
                <el-button :disabled="btnDisabled"
                           :icon="config.delBtnIcon"
                           :size="config.controlSize"
                           :type="menuText('danger')"
                           @click="crud.onDel(row, $index)">
                    <template v-if="!isIconMenu">
                        {{ "删 除" }}
                    </template>
                </el-button>

            </template>
            <slot :disabled="crud.btnDisabled"
                  :index="$index"
                  :row="row"
                  :size="config.controlSize"
                  :type="menuText('primary')"
                  name="menu"></slot>
        </template>
    </el-table-column>
</template>

<script>

import config from "./config.js";
import {dateFormat} from "@/util/date";

export default {
    name: "column-menu",
    inject: ["crud"],
    computed: {
        menuType() {
            return this.tableOption.menuType || 'menu' || 'button';
        },
        isIconMenu() {
            return this.menuType === "icon"
        },
        isTextMenu() {
            return this.menuType === "text"
        },
        isMenu() {
            return this.menuType === "menu"
        },
    },
    data() {
        return {
            config: config,
            triggerFlag:false,
            arrKey:[],
            forbidKeyboard:false
        }
    },
    props: {
        tableOption: {
            type: Object,
            default: () => {
                return {};
            }
        }
    },
    methods: {
        menuText(value) {
            return ['text', 'menu'].includes(this.menuType) ? "text" : value;
        },
        onClearSearch() {
            this.tableOption.column.forEach(item => {
                item.$search = undefined;
            });
            this.onLoad();
        },
        executeCommand(cmd, row, index) {
            switch (parseInt(cmd)) {
                case 9999998: //编辑
                    this.crud.rowEdit(row, index);
                    break;
                case 9999999: //删除
                    this.crud.rowDel(row, index);
                    break;
                default:
                    this.crud.$emit("menu-command", parseInt(cmd), row, index);
                    break;
            }
        },
        onLoad() {
            let param = {};
            this.tableOption.column.forEach(col => {
                if (col.$search == undefined) {
                    return;
                }
                if (col.type == 'date-picker') {
                    param[col.prop + '_datege'] = col.$search[0];
                    // 计算结束时间
                    let end_date = new Date(col.$search[1]);
                    end_date.setTime(end_date.getTime() + 3600 * 1000 * 24 - 1);
                    param[col.prop + '_datele'] = dateFormat(end_date, 'yyyy-MM-dd hh:mm:ss');
                } else if (col.type == 'select-table-user') {
                    param[col.prop] = col.$search;
                } else if (col.type == 'select') {
                    param[col.prop + '_in'] = col.$search && col.$search.length > 0 ? col.$search.join(',') : undefined;
                } else {
                    param[col.prop + '_like'] = col.$search;
                }
            });
            this.$set(this.crud, 'queryParams', param);
            this.crud.onRefresh();
        }
    }
}
</script>

<style scoped>

</style>
